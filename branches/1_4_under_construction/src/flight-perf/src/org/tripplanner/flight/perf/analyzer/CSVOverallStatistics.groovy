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
public class CSVOverallStatistics extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        CSVOverallStatistics instance = new CSVOverallStatistics();
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

    String format;


    //
    //  Initialization
    //

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildInputOption());
        options.addOption(CommandHelper.buildOutputOption());
        options.addOption(CommandHelper.buildFormatOption());

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

        if (format == null) {
            def fmtValue = settings[CommandHelper.FORMAT_LOPT];
            format = fmtValue;
        }

        return true;
    }


    //
    //  Runnable
    //

    @Override protected void cmdRun() {

        err.print("Running " + this.class.simpleName);
        if (format == null)
            err.println ""
        else
            err.println(", output format " + format);

        CsvMapReader csvMR =
            new CsvMapReader(new FileReader(iFile), CsvPreference.STANDARD_PREFERENCE);

        def requestRecordNumericHeaderList = CSVHelper.getRequestRecordNumericHeaderList();
        def requestRecordNumericHeaderArray = requestRecordNumericHeaderList as String[];

        def sampleStatisticsHeaderList = CSVHelper.getSampleStatisticsHeaderList();
        def sampleStatisticsHeaderArray = sampleStatisticsHeaderList as String[];


        // ignore 1st line (headers)
        csvMR.read(sampleStatisticsHeaderArray);

        int sampleNr = 0;


        //
        //  Initialize statistics objects
        //

        def statsMap = [ : ];
        requestRecordNumericHeaderList.each{ header ->
            // DescriptiveStatistics stores values in memory and can compute percentiles
            statsMap[header] = new DescriptiveStatistics();
        }

        //
        // process records
        //
        def sample = null;
        while ( (sample = csvMR.read(sampleStatisticsHeaderArray)) != null ) {
            sampleNr++;

            // process record
            statsMap.keySet().each{ key ->
                def meanKey = key + "-mean";
                double value = sample[meanKey].toDouble();
                statsMap[key].addValue(value);
            }

        }
        csvMR.close();


        // Compute statistics
        def resultMap = [ : ];

        statsMap.keySet().each { key ->
            double mean = statsMap[key].getMean();
            double std = statsMap[key].getStandardDeviation();

            resultMap[key + "-mean"] = mean;
            resultMap[key + "-stdDev"] = std;

            // compute confidence interval error

            // lo = mean - c * s / sqrt(n)
            // hi = mean + c * s / sqrt(n)

            // c values are fixed for confidence levels
            final def c_MAP = [ 90:1.64 , 95:1.96, 99:2.58 ];

            double n = sampleNr;
            double sqrt_n = Math.sqrt(n);
            double std_over_sqrt_n = std / sqrt_n;

            c_MAP.keySet().each{ ckey ->
                resultMap[key + "-mean-error-" + ckey + "pctconf"] = c_MAP[ckey] * std_over_sqrt_n;
            }
        }

        if (format != null && format ==~ "(?i)te?xt") {
            // text format

            def o = new PrintStream(oFile);
            o.println("Overall statistics");
            o.println();

            resultMap.keySet().sort().each {
                o.println(it + " = " + resultMap[it]);
            }

            o.println();
            o.close();

        } else {
            // default - CSV format

            // define output file structure
            ICsvMapWriter csvMW =
                new CsvMapWriter(new FileWriter(oFile), CsvPreference.STANDARD_PREFERENCE);

            // generate header list
            def headerList = CSVHelper.getOverallStatisticsHeaderList();;
            def headerArray = headerList as String[];

            // write headers
            csvMW.writeHeader(headerArray);

            // Write data
            csvMW.write(resultMap, headerArray);
            csvMW.close();
        }

    }

}
