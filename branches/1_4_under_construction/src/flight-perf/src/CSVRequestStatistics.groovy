/**
 *  Groovy command to calculate request statistics.
 *  Output is in CSV format.
 */

import org.apache.commons.cli.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;


public class CSVRequestStatistics extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        CSVRequestStatistics instance = new CSVRequestStatistics();
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

        CsvMapReader csvMR = new CsvMapReader(new FileReader(iFile), CsvPreference.STANDARD_PREFERENCE);

        def recordsHeaderList = CSVHelper.getRequestRecordsHeaderList();
        def recordsHeaderArray = CSVHelper.headerListToArray(recordsHeaderList);

        int recordNr = 0;

        // ignore 1st line (headers)
        csvMR.read(recordsHeaderArray);

        // ignore 2nd line (first request - startup)
        csvMR.read(recordsHeaderArray);
        recordNr++;


        //
        //  Initialize statistics objects
        //

        def statsMap = [ : ];


        recordsHeaderList.each{ header ->
            if (!header.endsWith("-meta")) {
                // DescriptiveStatistics stores values in memory, but can compute percentiles
                statsMap[header] = new DescriptiveStatistics();
            }
        }

        //
        // process records
        //
        def record = null;
        while ( (record = csvMR.read(recordsHeaderArray)) != null ) {
            recordNr++;

            // process record
            recordsHeaderList.each{ header ->
                if (!header.endsWith("-meta")) {
                    double value = record[header].toDouble();
                    statsMap[header].addValue(value);
                }
            }

        }
        csvMR.close();


        //
        //  Define output file structure
        //
        ICsvMapWriter csvMW = new CsvMapWriter(new FileWriter(oFile), CsvPreference.STANDARD_PREFERENCE);

        // generate header list
        def headerList = [ ];

        recordsHeaderList.each{ header ->
            if (!header.endsWith("-meta")) {
                headerList.add(header + "-mean");
                headerList.add(header + "-stdDev");
                headerList.add(header + "-median");
                headerList.add(header + "-lowerQ");
                headerList.add(header + "-upperQ");
            }
        }

        def headerArray = CSVHelper.headerListToArray(headerList);

        // write headers
        csvMW.writeHeader(headerArray);


        // Compute statistics
        def resultMap = [ : ];

        recordsHeaderList.each{ header ->
            if (!header.endsWith("-meta")) {
                double mean = statsMap[header].getMean();
                double std = statsMap[header].getStandardDeviation();

                double median = statsMap[header].getPercentile(50);
                double lowerQuartile = statsMap[header].getPercentile(25);
                double upperQuartile = statsMap[header].getPercentile(75);

                resultMap[header + "-mean"] = mean;
                resultMap[header + "-stdDev"] = std;
                resultMap[header + "-median"] = median;
                resultMap[header + "-lowerQ"] = lowerQuartile;
                resultMap[header + "-upperQ"] = upperQuartile;
            }
        }

        // Write data
        csvMW.write(resultMap, headerArray);
        csvMW.close();

    }

}
