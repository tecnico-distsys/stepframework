package org.tripplanner.flight.perf.analyzer;

import org.apache.commons.cli.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;

import step.groovy.command.*;
import org.tripplanner.flight.perf.*;


/**
 *  Groovy command to calculate request statistics.
 *  Output is in CSV format.
 */
public class CSVSampleStatistics extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        CSVSampleStatistics instance = new CSVSampleStatistics();
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
    Boolean appendFlag;


    //
    //  Initialization
    //

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildInputOption());
        options.addOption(CommandHelper.buildOutputOption());
        options.addOption(CommandHelper.buildAppendOption());

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

        if (appendFlag == null) {
            appendFlag = CommandHelper.initBoolean(settings[CommandHelper.APPEND_LOPT], false);
        }

        return true;
    }


    //
    //  Runnable
    //

    @Override protected void cmdRun() {

        err.println("Running " + this.class.simpleName);

        CsvMapReader csvMR =
            new CsvMapReader(new FileReader(iFile), CsvPreference.STANDARD_PREFERENCE);

        def recordsHeaderList = CSVHelper.getRequestRecordHeaderList();
        def recordsHeaderArray = recordsHeaderList as String[];

        def numericRecordsHeaderList = CSVHelper.getRequestRecordNumericHeaderList();
        def numericRecordsHeaderArray = numericRecordsHeaderList as String[];

        int recordNr = 0;

        // ignore 1st line (headers)
        csvMR.read(recordsHeaderArray);

        // ignore 2nd line (first request - startup)
        //csvMR.read(recordsHeaderArray);
        //recordNr++;


        //
        //  Initialize statistics objects
        //

        def statsMap = [ : ];

        numericRecordsHeaderList.each{ header ->
            // DescriptiveStatistics stores values in memory and can compute percentiles
            statsMap[header] = new DescriptiveStatistics();
        }

        //
        // process records
        //
        def record = null;
        while ( (record = csvMR.read(recordsHeaderArray)) != null ) {
            recordNr++;

            // process record
            statsMap.keySet().each { key ->
                double value;
                // consider empty string as zero
                if (record[key].length() == 0)
                    value = 0.0;
                else
                    value = record[key].toDouble();
                statsMap[key].addValue(value);
            }

        }
        csvMR.close();


        //
        //  Define output file structure
        //
        ICsvMapWriter csvMW =
            new CsvMapWriter(new FileWriter(oFile, appendFlag), CsvPreference.STANDARD_PREFERENCE);

        // generate header list
        def headerList = CSVHelper.getSampleStatisticsHeaderList();
        def headerArray = headerList as String[];

        // write headers
        if (!appendFlag) {
            csvMW.writeHeader(headerArray);
        }

        // Compute statistics
        def resultMap = [ : ];

        statsMap.keySet().each { key ->
            double mean = statsMap[key].getMean();
            double std = statsMap[key].getStandardDeviation();

            double median = statsMap[key].getPercentile(50);
            double lowerQuartile = statsMap[key].getPercentile(25);
            double upperQuartile = statsMap[key].getPercentile(75);

            resultMap[key + "-mean"] = mean;
            resultMap[key + "-stdDev"] = std;
            resultMap[key + "-median"] = median;
            resultMap[key + "-lowerQ"] = lowerQuartile;
            resultMap[key + "-upperQ"] = upperQuartile;
        }

        // Write data
        csvMW.write(resultMap, headerArray);
        csvMW.close();

    }

}
