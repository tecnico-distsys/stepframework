package org.tripplanner.flight.perf.analyzer;


import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import org.tripplanner.flight.perf.helper.*;

println("Processing virtual user output log files");

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "VirtualUserOutputStats")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli.sCSV(longOpt: "samplesCSV", required: true, args: 1, "Output samples file in CSV format")
cli.sTXT(longOpt: "samplesTXT", required: true, args: 1, "Output samples file in TXT format")
cli.oCSV(longOpt: "overallCSV", required: true, args: 1, "Output overall file in CSV format")
cli.oTXT(longOpt: "overallTXT", required: true, args: 1, "Output overall file in TXT format")
cli.rd(longOpt: "rundir", required: true, args: 1, "Run files directory")
cli.f(longOpt: "format", required: true, args: 1, "Run files format")
cli.n(longOpt: "number", required: true, args: 1, "Number of samples")
cli.u(longOpt: "users", required: true, args: 1, "Number of users per sample")

def options = cli.parse(args)
assert (options)

if (options.help) {
    cli.usage();
    return;
}

// access and validate option values -------------------------------------------

final def runDir = new File(options.rd);

final def runOutputFileNameFormat = options.f;
final def runNumberSamples = options.n as Long
assert runNumberSamples > 0

final def runNumberUsersPerSample = options.u as Long
assert runNumberUsersPerSample > 0

def vuoSamplesFile = new File(options.sCSV);
def vuoSamplesTextFile = new File(options.sTXT);

def vuoOverallFile = new File(options.oCSV);
def vuoOverallTextFile = new File(options.oTXT);

println("runDir: " + runDir);


// main ------------------------------------------------------------------------

final def searchRegex = "Search flights.+";
final def createRegex = "Create (\\d+) reservations.+";

final def caughtRegex = "Caught (.+?): (.+?)\\. Proceeding";

def appendFlag = false;
def appendTextFlag = false;

def sampleIdx = 0;
def runIdx = 0;

def statsMap = [ : ];

while (sampleIdx < runNumberSamples) {
    sampleIdx++;
    printf("Processing sample %d%n", sampleIdx);

    // reset counts for this sample
    def countMap = [ : ]

    while (runIdx < sampleIdx * runNumberUsersPerSample) {
        runIdx++;
        printf("Counting requests and exceptions for run %d that is part of sample %d%n", runIdx, sampleIdx);

        def runFileName = String.format(runOutputFileNameFormat, runIdx);
        def runFile = new File(runDir, runFileName)
        assert runFile.exists()

        // recognize lines
        println("Run file: " + runFile.canonicalPath);

        runFile.eachLine { line, number ->

            // ~ creates a Pattern from String
            // =~ creates a Matcher, and in a boolean context, it's "true" if it has at least one match, "false" otherwise.
            // ==~ tests, if String matches the pattern

            if (line ==~ searchRegex) {
                // count search
                final def key = "request-searchFlights";
                if (!countMap[key]) countMap[key] = 0;
                countMap[key]++;

                return;
            }

            def createMatcher = (line =~ createRegex);
            if (createMatcher.matches()) {
                def nrReservations = createMatcher.group(1);
                if (nrReservations ==~ "1") {
                    // count create reservation
                    final def key = "request-createSingleReservation";
                    if (!countMap[key]) countMap[key] = 0;
                    countMap[key]++;

                } else {
                    // count create reservations
                    final def key = "request-createMultipleReservations";
                    if (!countMap[key]) countMap[key] = 0;
                    countMap[key]++;

                }

                return;
            }

            def caughtMatcher = (line =~ caughtRegex);
            if (caughtMatcher.matches()) {
                def exName = caughtMatcher.group(1);
                def exMsg = caughtMatcher.group(2);

                def classSimpleNameMatcher = (exName =~ "(.*)\\.(.*)")
                def classSimpleNameMatchResult = classSimpleNameMatcher.matches();
                assert classSimpleNameMatchResult
                def exPackage = classSimpleNameMatcher.group(1);
                def exSimpleName = classSimpleNameMatcher.group(2);

                // count exception type
                final def key = "exception-" + exSimpleName;
                if (!countMap[key]) countMap[key] = 0;
                countMap[key]++;

                return;
            }

        }

    }

    // make sure at least one match was made
    def totalLinesMatched = 0;
    countMap.each { key, value ->
        totalLinesMatched += value;
    }
    assert totalLinesMatched > 0 : "No lines matched in virtual user output files! Make sure the correct files were processed"


    // write data after all run output files of the sample have been processed
    if (true) {
        // text format

        def o = new FileWriter(vuoSamplesTextFile, appendTextFlag);
        def eol = System.getProperty("line.separator");

        if (!appendTextFlag) {
            appendTextFlag = true;

            o.write("Sample request and exception counts"); o.write(eol);
            o.write(eol);
        }

        o.write("Sample #" + sampleIdx); o.write(eol);
        countMap.keySet().sort().each {
            o.write(it + " = " + countMap[it]); o.write(eol);
        }

        o.write(eol);
        o.close();

    }

    if (true) {
        // CSV format

        //  Define output file structure
        ICsvMapWriter csvMW =
            new CsvMapWriter(new FileWriter(vuoSamplesFile, appendFlag), CsvPreference.STANDARD_PREFERENCE);

        // generate header list
        def headerList = CSVHelper.getVirtualUserOutputSampleCountsHeaderList();
        def headerArray = headerList as String[];

        // ensure all headers exist and have a default value
        headerList.each { header ->
            if (!countMap[header]) countMap[header] = 0
        }

        // write headers
        if (!appendFlag) {
            appendFlag = true;
            csvMW.writeHeader(headerArray);
        }

        // Write data
        csvMW.write(countMap, headerArray);
        csvMW.close();
    }


    // add to stats
    countMap.each { key, value ->
        def stats = statsMap[key];
        if (stats == null) {
            // DescriptiveStatistics stores values in memory and can compute percentiles
            stats = new DescriptiveStatistics();
            statsMap[key] = stats;
        }
        stats.addValue(value as Double);
    }

}

// Compute statistics
def resultMap = [ : ];

statsMap.each { key, value ->
    double mean = value.getMean();
    double std = value.getStandardDeviation();

    resultMap[key + "-mean"] = mean;
    resultMap[key + "-stdDev"] = std;

    // compute confidence interval error

    // lo = mean - c * s / sqrt(n)
    // hi = mean + c * s / sqrt(n)

    // c values are fixed for confidence levels
    final def c_MAP = [ 90:1.64 , 95:1.96, 99:2.58 ];

    double n = runNumberSamples;
    double sqrt_n = Math.sqrt(n);
    double std_over_sqrt_n = std / sqrt_n;

    c_MAP.keySet().each{ ckey ->
        resultMap[key + "-mean-error-" + ckey + "pctconf"] = c_MAP[ckey] * std_over_sqrt_n;
    }
}

if (true) {
    // text format

    def o = new PrintStream(vuoOverallTextFile);
    o.println("Overall statistics of request and exception counts");
    o.println();

    resultMap.keySet().sort().each {
        o.println(it + " = " + resultMap[it]);
    }

    o.println();
    o.close();
}

if (true) {
    // CSV format

    // define output file structure
    ICsvMapWriter csvMW =
        new CsvMapWriter(new FileWriter(vuoOverallFile), CsvPreference.STANDARD_PREFERENCE);

    // generate header list
    def headerList = CSVHelper.getVirtualUserOutputOverallCountsHeaderList();;
    def headerArray = headerList as String[];

    // ensure all headers exist and have a default value
    headerList.each { header ->
        if (!resultMap[header]) resultMap[header] = 0
    }

    // write headers
    csvMW.writeHeader(headerArray);

    // Write data
    csvMW.write(resultMap, headerArray);
    csvMW.close();
}

// -----------------------------------------------------------------------------
