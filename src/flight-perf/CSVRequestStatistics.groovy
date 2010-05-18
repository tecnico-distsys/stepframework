/**
 *  Groovy command to calculate request totals in perf4j performance log file.
 *  Output is in CSV format.
 */

import org.apache.commons.cli.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;


public class RequestTotals extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        RequestTotals instance = new RequestTotals();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    //
    //  Members
    //
    InputStream i;
    File file;


    //
    //  Initialization
    //

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildInputOption());
        options.addOption(CommandHelper.buildFileOption());

        return options;
    }

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (i == null) {
            def iValue = settings[CommandHelper.INPUT_LOPT];
            if (iValue == null) {
                i = System.in;
            } else {
                i = new FileInputStream(iValue);
            }
        }

        if (file == null) {
            def fileValue = settings[CommandHelper.FILE_LOPT];
            if (fileValue == null) {
                err.println("File is missing!");
                return false;
            } else {
                file = new File(fileValue);
            }
        }

        return true;
    }


    //
    //  Runnable
    //

    @Override protected void cmdRun() {

        err.println("Running " + this.class.simpleName);

        def csvFile = new File("build\\logs\\18-05-2010\\requests-4.csv");

        //
        //  Read headers from 1st line of file
        //
        def csvFileReader = new FileReader(csvFile);
        CsvListReader csvLR = new CsvListReader(csvFileReader, CsvPreference.STANDARD_PREFERENCE);

        def headerList = csvLR.read();
        csvLR.close();

        // create header array
        String[] headerArray = new String[headerList.size];
        for (int i = 0; i < headerArray.length; i++) {
            headerArray[i] = (String) headerList[i];
        }


        //
        //  Process rows
        //

        csvFileReader = new FileReader(csvFile);
        CsvMapReader csvMR = new CsvMapReader(csvFileReader, CsvPreference.STANDARD_PREFERENCE);

        // ignore 1st line (headers)
        int recordNr = 0;
        csvMR.read(headerArray);
        recordNr++;

        // ignore 2nd line (first request)
        csvMR.read(headerArray);
        recordNr++;


        // Get a DescriptiveStatistics instance (values are stored in memory)
        DescriptiveStatistics stats = new DescriptiveStatistics();


        // process records
        def record = null;
        while ( (record = csvMR.read(headerArray)) != null ) {
            recordNr++;

            // process record
            double value = record["filter"].toDouble();
            stats.addValue(value);

            double mean = stats.getMean();
            double std = stats.getStandardDeviation();
            double cov = std / mean;
            //printf("cov %.2f %n", cov);

        }
        csvMR.close();


        // Compute statistics
        double mean = stats.getMean();
        double std = stats.getStandardDeviation();

        double median = stats.getPercentile(50);
        double lowerQuartile = stats.getPercentile(25);
        double upperQuartile = stats.getPercentile(75);

        println("Results:");
        printf("mean %.2f , std dev %.2f %n", mean, std);
        printf("lower quartile %.2f , median %.2f , upper quartile %.2f %n",
            lowerQuartile, median, upperQuartile);

    }

}
