package org.tripplanner.flight.perf.analyzer;

import org.apache.commons.cli.*;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import step.groovy.command.*;
import org.tripplanner.flight.perf.*;


/**
 *  Groovy command to calculate request totals in perf4j performance log file.
 *  Output is in CSV format.
 */
public class Perf4JRequestRecords extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        Perf4JRequestRecords instance = new Perf4JRequestRecords();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    //
    //  Members
    //
    File iFile;
    File oFile;


    //
    //  Initialization
    //

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildInputOption());
        options.addOption(CommandHelper.buildOutputOption());

        return options;
    }

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (iFile == null) {
            def iValue = settings[CommandHelper.INPUT_LOPT];
            if (iValue == null) {
                err.println("Input file is missing!");
                return false;
            } else {
                iFile = new File(iValue);
            }
        }

        if (oFile == null) {
            def oValue = settings[CommandHelper.OUTPUT_LOPT];
            if (oValue == null) {
                err.println("Output file is missing!");
                return false;
            } else {
                oFile = new File(oValue);
            }
        }

        return true;
    }


    //
    //  Runnable
    //

    @Override protected void cmdRun() {

        err.println("Running " + this.class.simpleName);


        //
        //  Define output file structure
        //
        ICsvMapWriter writer = new CsvMapWriter(new FileWriter(oFile), CsvPreference.STANDARD_PREFERENCE);

        // write headers
        final def headerList = CSVHelper.getRequestRecordHeaderList();
        final String[] headerArray = headerList as String[];
        writer.writeHeader(headerArray);

        final def numericHeaderList = CSVHelper.getRequestRecordNumericHeaderList();
        final String[] numericHeaderArray = numericHeaderList as String[];


        //
        //  process records
        //

        // init map with all entries empty
        def totalMap = [ : ];
        headerList.each { key ->
            totalMap[key] = "";
        }
        numericHeaderList.each{ key ->
            totalMap[key] = 0L;
        }

        def iStream = new FileInputStream(iFile);
        iStream.eachLine { line, number ->
            assert number > 0

            //
            //  Extract current line data
            //
            def lineMatcher = ( line =~ Perf4JHelper.PERF_LOG_LINE_REGEX );
            def lineMatcherResult = lineMatcher.matches();
            assert lineMatcherResult

            final def start = Long.parseLong(lineMatcher.group(1));
            final def time = Long.parseLong(lineMatcher.group(2));
            final def tag = lineMatcher.group(3);
            final def message = lineMatcher.group(4);

            // process line

            if(tag ==~ "filter") {
                // process start of new request (implicit end of previous request)

                totalMap["filter_time"] = time;

                if (!totalMap["hibernate_read_time"])
                    totalMap["hibernate_read_time"] = 0;
                if (!totalMap["hibernate_write_time"])
                    totalMap["hibernate_write_time"] = 0;
                def totalHibernate = totalMap["hibernate_read_time"] + totalMap["hibernate_write_time"];
                assert (totalHibernate >= 0)

                // warn about bad time data quality
                if (totalMap["filter_time"] < totalMap["soap_time"] ||
                    totalMap["soap_time"] < totalMap["wsi_time"] ||
                    totalMap["wsi_time"] < totalMap["si_time"] ||
                    totalMap["si_time"] < totalHibernate) {
                    println "Warning: Record ending in line " + number + " has inconsistent times.";
                }

                // dump previous request data
                writer.write(totalMap, headerArray);

                // reset map
                totalMap = [ : ];
                headerList.each { key ->
                    totalMap[key] = "";
                }
                numericHeaderList.each{ key ->
                    totalMap[key] = 0L;
                }

            } else {
                // process request slice

                // match tag
                def tagMatcher = ( tag =~ "([A-Za-z\\-_]+)\\.?(.*)" );
                def tagMatchResult = tagMatcher.matches();
                assert(tagMatchResult);

                final def mainTag = tagMatcher.group(1);
                final def metaTag = tagMatcher.group(2);

                def recordTagToUse = mainTag;

                // handle special cases
                if ("hibernate".equals(mainTag)) {

                    if ("load".equals(metaTag)) {
                        // read
                        recordTagToUse += "_read_time";
                    } else {
                        // write
                        recordTagToUse += "_write_time";
                    }

                } else if ("si".equals(mainTag)) {
                    recordTagToUse += "_time";
                    totalMap["si_name"] = metaTag;

                } else if ("soap".equals(mainTag)) {
                    recordTagToUse += "_time";
                    totalMap["soap_name"] = metaTag;

                    // match message
                    def messageMatcher = ( message =~ "request ll:(\\d*+) nc:(\\d+) md:(\\d+) response ll:(\\d*+) nc:(\\d+) md:(\\d+)" );
                    def messageMatchResult = messageMatcher.matches();
                    assert(messageMatchResult);

                    // retrieve XML metrics
                    def value;

                    value = Long.parseLong(messageMatcher.group(1));
                    totalMap["soap_request_logical_length"] = value;

                    value = Long.parseLong(messageMatcher.group(2));
                    totalMap["soap_request_node_count"] = value;

                    value = Long.parseLong(messageMatcher.group(3));
                    totalMap["soap_request_max_depth"] = value;

                    value = Long.parseLong(messageMatcher.group(4));
                    totalMap["soap_response_logical_length"] = value;

                    value = Long.parseLong(messageMatcher.group(5));
                    totalMap["soap_response_node_count"] = value;

                    value = Long.parseLong(messageMatcher.group(6));
                    totalMap["soap_response_max_depth"] = value;

                } else {
                    // general case
                    recordTagToUse += "_time";
                }

                assert(recordTagToUse); // make sure tag to use was initialized

                // add value to total
                def total = totalMap[recordTagToUse];
                if (!total) total = 0;
                total += time;
                totalMap[recordTagToUse] = total;

            }

            assert(totalMap.size() == headerArray.length);    // make sure no map keys were mistyped
        }

        // close file
        writer.close();

    }

}
