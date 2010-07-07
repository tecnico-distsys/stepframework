package org.tripplanner.flight.perf.analyzer;

import org.apache.commons.cli.*;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import step.groovy.command.*;
import org.tripplanner.flight.perf.*;


/**
 *  Groovy command to calculate request totals in layermon performance log file.
 *  Output is in CSV format.
 */
public class LayerMonRequestRecords extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        LayerMonRequestRecords instance = new LayerMonRequestRecords();
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

            if (line.length() == 0) {
                // record separator

                if (!totalMap["hibernate_read_time"])
                    totalMap["hibernate_read_time"] = 0L;
                if (!totalMap["hibernate_write_time"])
                    totalMap["hibernate_write_time"] = 0L;
                def totalHibernate = totalMap["hibernate_read_time"] + totalMap["hibernate_write_time"];
                assert totalHibernate >= 0L

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
                // process record item

                //
                //  Extract current line data
                //
                def lineMatcher = ( line =~ LayerMonHelper.PERF_LOG_LINE_REGEX );
                def lineMatcherResult = lineMatcher.matches();
                assert lineMatcherResult
    
                final def thread = lineMatcher.group(1);
                final def tag = lineMatcher.group(2);
                final def accTime = lineMatcher.group(3);
                final def context = lineMatcher.group(4);

                // handle time unit and convert to ms
                if (accTime.endsWith("ns")) {
                    // nanoseconds
                    accTime = accTime.substring(0, accTime.length()-2);
                    accTime = accTime as Long;
                    accTime = accTime / 1000L / 1000L;
                } else if(accTime.endsWith("us")) {
                    // microseconds
                    accTime = accTime.substring(0, accTime.length()-2);
                    accTime = accTime as Long;
                    accTime = accTime / 1000L;
                } else if(accTime.endsWith("ms")) {
                    accTime = accTime as Long;
                }

                // convert context to map
                context = getContextMap(context);

                //
                //  match tag
                //
                def tagMatcher = ( tag =~ "([A-Za-z\\-_]+)\\.?(.*)" );
                def tagMatchResult = tagMatcher.matches();
                assert tagMatchResult

                final def mainTag = tagMatcher.group(1);
                final def subTag = tagMatcher.group(2);

                def tagToUse = mainTag;

                // handle special cases
                switch (mainTag) {
                    case "hibernate":
                        if ("load".equals(subTag)) {
                            // read
                            tagToUse += "_read_time";
                        } else {
                            // write
                            tagToUse += "_write_time";
                        }
                        break;

                    case "si":
                        tagToUse += "_time";
                        totalMap["si_name"] = context["className"];
                        break;
                        
                    case "soap":
                        tagToUse += "_time";

                        // soap_name
                        def body = context["requestBodyName"];
                        def fault = context["responseFaultName"];
                        if (fault ==~ "null")
                            fault = null;
                        totalMap["soap_name"] = body + (fault == null ? "" : "." + fault);

                        totalMap["soap_request_logical_length"] = context["requestLogicalLength"];
                        totalMap["soap_request_max_depth"] = context["requestMaxDepth"];
                        totalMap["soap_request_node_count"] = context["requestNodeCount"];

                        totalMap["soap_response_logical_length"] = context["responseLogicalLength"];
                        totalMap["soap_response_max_depth"] = context["responseMaxDepth"];
                        totalMap["soap_response_node_count"] = context["responseNodeCount"];
                        break;

                    default:
                        // general case
                        tagToUse += "_time";
                }

                // make sure tag to use was initialized
                assert tagToUse
                
                // add value to total
                def total = totalMap[tagToUse];
                if (!total) total = 0;
                
                total += accTime;
                totalMap[tagToUse] = total;
            }
            
            // make sure no map keys were mistyped
            assert(totalMap.size() == headerArray.length);    
           
        }

        // close file
        writer.close();

    }

    //  Convert context string to map
    private def getContextMap(contextString) {
        def map = [ : ];
        if (contextString) {
            def list = contextString.split("\\,").toList();
            list.each { item ->
                def array = item.split(":");
                map.put(array[0], array[1]);
            }
        }
        return map;
    }

}
