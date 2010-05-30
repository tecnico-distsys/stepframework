/**
 *  Groovy script to calculate and report statistics
 */

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;


def logDirPath = "../build/logs";
def logDir = new File(logDirPath);
assert(logDir.exists() && logDir.isDirectory());

// init record fields
def recordsHeaderList = CSVHelper.getRequestRecordsHeaderList();
def recordsHeaderArray = CSVHelper.headerListToArray(recordsHeaderList);

// init global statistics
def globalStatsMap = [ : ];
recordsHeaderList.each{ header ->
    if (!header.endsWith("-meta")) {
        // DescriptiveStatistics stores values in memory, but can compute percentiles
        globalStatsMap[header] = new DescriptiveStatistics();
    }
}

def sampleCount = 0;
logDir.eachFile{ file ->
    if (file.name ==~ "perfLog-requests-\\d+\\.csv") {
        sampleCount++;

        println("Statistics for sample " + sampleCount + " taken from " + file.name + ":");

        // init sample statistics
        def statsMap = [ : ];
        recordsHeaderList.each{ header ->
            if (!header.endsWith("-meta")) {
                statsMap[header] = new DescriptiveStatistics();
            }
        }

        //
        //  read sample data file
        //
        CsvMapReader csvMR = new CsvMapReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);


        // ignore 1st line (headers)
        csvMR.read(recordsHeaderArray);
        // ignore 2nd line (first request - startup)
        csvMR.read(recordsHeaderArray);

        // process remaining records
        def record = null;
        while ( (record = csvMR.read(recordsHeaderArray)) != null ) {
            recordsHeaderList.each{ header ->
                if (!header.endsWith("-meta")) {
                    double value = record[header].toDouble();
                    statsMap[header].addValue(value);
                }
            }
        }
        csvMR.close();

        // compute sample statistics
        recordsHeaderList.each{ header ->
            if (!header.endsWith("-meta")) {
                double mean = statsMap[header].getMean();
                double std = statsMap[header].getStandardDeviation();

                double median = statsMap[header].getPercentile(50);
                double lowerQuartile = statsMap[header].getPercentile(25);
                double upperQuartile = statsMap[header].getPercentile(75);

                printf("%s mean %.2f _ stdDev %.2f _ lowQ %.2f _ median %.2f _ upperQ %.2f %n",
                    header, mean, std, lowerQuartile, median, upperQuartile);

                // add mean to global statistics
                globalStatsMap[header].addValue(mean);
            }
        }
        // separating line
        println();

    }
} // eachFile

println("*** Overall statistics ***:");
recordsHeaderList.each{ header ->
    if (!header.endsWith("-meta")) {
        double mean = globalStatsMap[header].getMean();
        double std = globalStatsMap[header].getStandardDeviation();
        printf("%s overall mean %.2f _ overall stdDev %.2f %n", header, mean, std);

        // calculate confidence interval - population can be normal or non-normal - std
        // lo = mean - c * s / sqrt(n)
        // hi = mean + c * s / sqrt(n)
        double n = sampleCount;
        double sqrt_n = Math.sqrt(sampleCount);
        double std_over_sqrt_n = std / sqrt_n;

        // c values are fixed for confidence levels
        def cMap = [ 90:1.64 , 95:1.96, 99:2.58 ];
        def loMap = [ : ];
        def hiMap = [ : ];

        cMap.keySet().each{ key ->
            double lo = mean - cMap[key] * std_over_sqrt_n;
            double hi = mean + cMap[key] * std_over_sqrt_n;
            loMap[key] = lo;
            hiMap[key] = hi;
            printf("%s mean confidence interval [ %.2f _ %.2f ] with %d percent confidence (width %.2f) %n", header, loMap[key], hiMap[key], key, hiMap[key]-loMap[key]);
        }
    }
}

println();
