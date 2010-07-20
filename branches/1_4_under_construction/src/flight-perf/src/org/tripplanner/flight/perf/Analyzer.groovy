package org.tripplanner.flight.perf;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import step.groovy.Helper;
import org.tripplanner.flight.perf.analyzer.*;
import org.tripplanner.flight.perf.helper.*;


/**
 *  Analyze test runs
 */

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "Analyzer")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli.cfg(longOpt: "config", required: false, args: 1, "Specify master configuration file")
cli._(longOpt: "force", required: false, args: 0, "Force analysis")

def options = cli.parse(args)
assert (options)

if (options.help) {
    cli.usage();
    return;
}

// load initial configuration --------------------------------------------------
def configPath = "etc/config/Config.groovy";
if (options.cfg) configPath = options.cfg;

def config = Helper.parseConfig(configPath);
assert config.perf.flight : "Expecting flight performance configuration file"
Helper.configStringToFile(config);

// main ------------------------------------------------------------------------
println "----------------"
println "--- ANALYZER ---"
println "----------------"

final def outputBaseDir = config.perf.flight.stats.outputBaseDir;
final def runOutputBaseDir = config.perf.flight.run.outputBaseDir;

// iterate all stats instances
def instanceDir = config.perf.flight.stats.instanceDir;
def instanceFileNamePattern = ~config.perf.flight.stats.instanceFileNameRegex;
println("Looking for stats instances in " + instanceDir.canonicalPath);
instanceDir.eachFileMatch(instanceFileNamePattern) { file ->

    def statsConfig = Helper.parseConfig(file.path);
    statsConfig = statsConfig.perf.flight.stats.instance;
    assert (statsConfig) : "Expecting flight stats instance configuration file"

    final def statsId = statsConfig.id;

    final def samples = statsConfig.numberSamples;
    assert samples >= 1

    def runId = statsConfig.runId;
    def filterId = statsConfig.filterId;

    def runOutputDir = new File(runOutputBaseDir, runId);
    assert runOutputDir.exists() && runOutputDir.isDirectory()
    assert runOutputDir.listFiles().size() >= samples

    def outputDir = new File(outputBaseDir, statsId);

    // skip analysis if possible
    if (!options.force && outputDir.exists()) {
        println("Skipping stats '" + statsId + "' defined by file " + file.name);
        return;
    }

    // generate request records ------------------------------------------------
    if (!outputDir.exists()) outputDir.mkdir();
    assert outputDir.exists() && outputDir.isDirectory()

    println("Processing stats '" + statsId + "' defined by file " + file.name);
    println("Output directory: " + outputDir.canonicalPath);

    // define closure to process sample i
    def genRequestRecordsClosure = { i ->
        def perf4JLogFileName = String.format(config.perf.flight.run.outputPerf4JLogFileNameFormat, i+1);
        def perf4JLogFile = new File(runOutputDir, perf4JLogFileName);

        def eventMonLogFileName = String.format(config.perf.flight.run.outputEventMonLogFileNameFormat, i+1);
        def eventMonLogFile = new File(runOutputDir, eventMonLogFileName);

        def layerMonLogFileName = String.format(config.perf.flight.run.outputLayerMonLogFileNameFormat, i+1);
        def layerMonLogFile = new File(runOutputDir, layerMonLogFileName);

        def requestsFileName = String.format(config.perf.flight.stats.requestsFileNameFormat, i+1);
        def requestsFile = new File(outputDir, requestsFileName);

        if (perf4JLogFile.exists()) {
            Perf4JRequestRecords.main(
                [
                "-i", perf4JLogFile.canonicalPath,
                "-o", requestsFile.canonicalPath
                ] as String[]
            );
        } else if(eventMonLogFile.exists()) {
            EventMonRequestRecords.main(
                [
                "-i", eventMonLogFile.canonicalPath,
                "-o", requestsFile.canonicalPath
                ] as String[]
            );
        } else if(layerMonLogFile.exists()) {
            LayerMonRequestRecords.main(
                [
                "-i", layerMonLogFile.canonicalPath,
                "-o", requestsFile.canonicalPath
                ] as String[]
            );
        } else {
            assert false : "Did not recognize any performance log files in " + runOutputDir.canonicalPath;
        }
    }
    println("Generate request records");
    // create a closure to process each sample
    def genRequestRecordsClosureArray = Helper.indexCurryClosureArray(genRequestRecordsClosure, samples);
    // execute closures in parallel
    Helper.multicoreExecute(genRequestRecordsClosureArray);
    // -------------------------------------------------------------------------


    // filter records ----------------------------------------------------------
    def filterClosure = statsConfig.filterClosure;
    if (filterClosure) {

        // define closure to process sample i
        def filterRecordsClosure = { i ->
            def requestsFileName = String.format(config.perf.flight.stats.requestsFileNameFormat, i+1);
            def requestsFile = new File(outputDir, requestsFileName);

            def unfilteredRequestsFile = new File(requestsFile.canonicalPath + ".unfiltered");
            def ant = new AntBuilder();
            ant.move(file: requestsFile.canonicalPath,
                     tofile: unfilteredRequestsFile.canonicalPath)

            // read headers
            CsvListReader csvLR = new CsvListReader(new FileReader(unfilteredRequestsFile), CsvPreference.STANDARD_PREFERENCE);
            def csvHeaders = csvLR.read();
            csvLR.close();

            // write headers
            CsvListWriter csvLW = new CsvListWriter(new FileWriter(requestsFile), CsvPreference.STANDARD_PREFERENCE);
            csvLW.write(csvHeaders);
            csvLW.close();

            // read records
            CsvMapReader csvMR = new CsvMapReader(new FileReader(unfilteredRequestsFile), CsvPreference.STANDARD_PREFERENCE);
            CsvMapWriter csvMW = new CsvMapWriter(new FileWriter(requestsFile, true), CsvPreference.STANDARD_PREFERENCE);

            def csvHeadersArray = csvHeaders as String[];
            // ignore 1st line (headers)
            csvMR.read(csvHeadersArray);

            def record;
            // read and write record
            while ((record = csvMR.read(csvHeadersArray)) != null) {
                if (filterClosure(record)) {
                    csvMW.write(record, csvHeadersArray);
                }
            }
            // close files
            csvMR.close();
            csvMW.close();
        }

        println("Filter request records");
        // create a closure to process each sample
        def filterRecordsClosureArray = Helper.indexCurryClosureArray(filterRecordsClosure, samples);
        // execute closures in parallel
        Helper.multicoreExecute(filterRecordsClosureArray);
    }
    // -------------------------------------------------------------------------

    // adjust hibernate times --------------------------------------------------
    if (statsConfig.adjustHibernateTimes) {

        // define closure to process sample i
        def adjustHibernateTimesClosure = { i ->
            def unadjustedRequestsFile = new File(requestsFile.canonicalPath + ".unadjusted");

            def ant = new AntBuilder();
            ant.move(file: requestsFile.canonicalPath,
                     tofile: unadjustedRequestsFile.canonicalPath)

            CSVAdjustHibernateRecords.main(
                [
                "-i", unadjustedRequestsFile.canonicalPath,
                "-o", requestsFile.canonicalPath
                ] as String[]
            );
        }

        println("Adjust Hibernate times");
        // create a closure to process each sample
        def adjustHibernateTimesClosureArray = Helper.indexCurryClosureArray(adjustHibernateTimesClosure, samples);
        // execute closures in parallel
        Helper.multicoreExecute(adjustHibernateTimesClosureArray);
    }
    // -------------------------------------------------------------------------


    // Compute sample statistics -----------------------------------------------
    def sampleStatsFileName = config.perf.flight.stats.samplesFileName;
    def sampleStatsFile = new File(outputDir, sampleStatsFileName);

    def sampleStatsTextFileName = config.perf.flight.stats.samplesTextFileName;
    def sampleStatsTextFile = new File(outputDir, sampleStatsTextFileName);

    // loop sample files one at a time, because results are appended to same file
    println("Compute sample statistics");
    for (int i = 0; i < samples; i++) {

        def requestsFileName = String.format(config.perf.flight.stats.requestsFileNameFormat, i+1);
        def requestsFile = new File(outputDir, requestsFileName);

        CSVSampleStatistics.main(
            [
            "-i", requestsFile.canonicalPath,
            "-o", sampleStatsFile.canonicalPath,
            "-n", i+1 as String,
            "--append", (i == 0 ? "false" : "true")
            ] as String[]
        );

        CSVSampleStatistics.main(
            [
            "-i", requestsFile.canonicalPath,
            "-o", sampleStatsTextFile.canonicalPath,
            "-n", i+1 as String,
            "--append", (i == 0 ? "false" : "true"),
            "--format", "text"
            ] as String[]
        );
    }
    // -------------------------------------------------------------------------


    // compute overall statistics ----------------------------------------------
    println("Compute overall statistics");

    def overallStatsFileName = config.perf.flight.stats.overallFileName;
    def overallStatsFile = new File(outputDir, overallStatsFileName);

    CSVOverallStatistics.main(
        [
        "-i", sampleStatsFile.canonicalPath,
        "-o", overallStatsFile.canonicalPath
        ] as String[]
    );

    def overallStatsTextFileName = config.perf.flight.stats.overallTextFileName;
    def overallStatsTextFile = new File(outputDir, overallStatsTextFileName);

    CSVOverallStatistics.main(
        [
        "-i", sampleStatsFile.canonicalPath,
        "-o", overallStatsTextFile.canonicalPath,
        "--format", "text"
        ] as String[]
    );
    // -------------------------------------------------------------------------


    // count virtual user outputs for additional statistics --------------------
    println("Compute virtual user output statistics");

    def virtualUserOutputSamplesFileName = config.perf.flight.stats.virtualUserOutputSamplesFileName;
    def virtualUserOutputSamplesFile = new File(outputDir, virtualUserOutputSamplesFileName);

    def virtualUserOutputSamplesTextFileName = config.perf.flight.stats.virtualUserOutputSamplesTextFileName;
    def virtualUserOutputSamplesTextFile = new File(outputDir, virtualUserOutputSamplesTextFileName);


    def virtualUserOutputOverallFileName = config.perf.flight.stats.virtualUserOutputOverallFileName;
    def virtualUserOutputOverallFile = new File(outputDir, virtualUserOutputOverallFileName);

    def virtualUserOutputOverallTextFileName = config.perf.flight.stats.virtualUserOutputOverallTextFileName;
    def virtualUserOutputOverallTextFile = new File(outputDir, virtualUserOutputOverallTextFileName);


    def runOutputFileNameFormat = config.perf.flight.run.outputFileNameFormat;

    def runNumberSamples = statsConfig.numberSamples;
    def runNumberUsersPerSample = statsConfig.numberUsersPerSample;
    if (!runNumberUsersPerSample) runNumberUsersPerSample = 1;  // default value

    VirtualUserOutputStats.main(
        [
        "-sCSV", virtualUserOutputSamplesFile.canonicalPath,
        "-sTXT", virtualUserOutputSamplesTextFile.canonicalPath,
        "-oCSV", virtualUserOutputOverallFile.canonicalPath,
        "-oTXT", virtualUserOutputOverallTextFile.canonicalPath,
        "--rundir", runOutputDir.canonicalPath,
        "--format", runOutputFileNameFormat,
        "--number", runNumberSamples as String,
        "--users", runNumberUsersPerSample as String
        ] as String[]
    );

    // -------------------------------------------------------------------------

}
