package org.tripplanner.flight.perf;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import step.groovy.Helper;
import org.tripplanner.flight.perf.analyzer.*;


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
                "-i", perf4JLogFile.absolutePath,
                "-o", requestsFile.absolutePath
                ] as String[]
            );
        } else if(eventMonLogFile.exists()) {
            EventMonRequestRecords.main(
                [
                "-i", eventMonLogFile.absolutePath,
                "-o", requestsFile.absolutePath
                ] as String[]
            );
        } else if(layerMonLogFile.exists()) {
            LayerMonRequestRecords.main(
                [
                "-i", layerMonLogFile.absolutePath,
                "-o", requestsFile.absolutePath
                ] as String[]
            );
        } else {
            assert false : "Did not recognize any performance log files in " + runOutputDir.absolutePath;
        }
    }
    println("Generate request records");
    // create a closure to process each sample
    def genRequestRecordsClosureArray = Helper.indexCurryClosureArray(genRequestRecordsClosure, samples);
    // execute closures in parallel
    Helper.multicoreExecute(genRequestRecordsClosureArray);
    // ---------------------------------------------------------------------


    // filter records ------------------------------------------------------
    def filterClosure = statsConfig.filterClosure;
    if (filterClosure) {

        // define closure to process sample i
        def filterRecordsClosure = { i ->
            def requestsFileName = String.format(config.perf.flight.stats.requestsFileNameFormat, i+1);
            def requestsFile = new File(outputDir, requestsFileName);

            def unfilteredRequestsFile = new File(requestsFile.absolutePath + ".unfiltered");
            def ant = new AntBuilder();
            ant.move(file: requestsFile.absolutePath,
                     tofile: unfilteredRequestsFile.absolutePath)

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
    // ---------------------------------------------------------------------

    // adjust hibernate times ----------------------------------------------
    if (statsConfig.adjustHibernateTimes) {

        // define closure to process sample i
        def adjustHibernateTimesClosure = { i ->
            def unadjustedRequestsFile = new File(requestsFile.absolutePath + ".unadjusted");

            def ant = new AntBuilder();
            ant.move(file: requestsFile.absolutePath,
                     tofile: unadjustedRequestsFile.absolutePath)

            CSVAdjustHibernateRecords.main(
                [
                "-i", unadjustedRequestsFile.absolutePath,
                "-o", requestsFile.absolutePath
                ] as String[]
            );
        }

        println("Adjust Hibernate times");
        // create a closure to process each sample
        def adjustHibernateTimesClosureArray = Helper.indexCurryClosureArray(adjustHibernateTimesClosure, samples);
        // execute closures in parallel
        Helper.multicoreExecute(adjustHibernateTimesClosureArray);
    }
    // ---------------------------------------------------------------------


    // Compute sample statistics -------------------------------------------
    def sampleStatsFileName = config.perf.flight.stats.samplesFileName;
    def sampleStatsFile = new File(outputDir, sampleStatsFileName);

    def sampleStatsTextFileName = config.perf.flight.stats.samplesTextFileName;
    def sampleStatsTextFile = new File(outputDir, sampleStatsTextFileName);

    def sampleStatsClosure = { i ->
        def requestsFileName = String.format(config.perf.flight.stats.requestsFileNameFormat, i+1);
        def requestsFile = new File(outputDir, requestsFileName);

        CSVSampleStatistics.main(
            [
            "-i", requestsFile.absolutePath,
            "-o", sampleStatsFile.absolutePath,
            "-n", i+1 as String,
            "--append", (i == 0 ? "false" : "true")
            ] as String[]
        );

        CSVSampleStatistics.main(
            [
            "-i", requestsFile.absolutePath,
            "-o", sampleStatsTextFile.absolutePath,
            "-n", i+1 as String,
            "--append", (i == 0 ? "false" : "true"),
            "--format", "text"
            ] as String[]
        );
    }

    println("Compute sample statistics");
    // create a closure to process each sample
    def sampleStatsClosureArray = Helper.indexCurryClosureArray(sampleStatsClosure, samples);
    // execute closures in parallel
    Helper.multicoreExecute(sampleStatsClosureArray);
    // ---------------------------------------------------------------------


    // compute overall statistics ------------------------------------------
    println("Compute overall statistics");

    def overallStatsFileName = config.perf.flight.stats.overallFileName;
    def overallStatsFile = new File(outputDir, overallStatsFileName);

    CSVOverallStatistics.main(
        [
        "-i", sampleStatsFile as String,
        "-o", overallStatsFile as String
        ] as String[]
    );

    def overallStatsTextFileName = config.perf.flight.stats.overallTextFileName;
    def overallStatsTextFile = new File(outputDir, overallStatsTextFileName);

    CSVOverallStatistics.main(
        [
        "-i", sampleStatsFile as String,
        "-o", overallStatsTextFile as String,
        "--format", "text"
        ] as String[]
    );
    // ---------------------------------------------------------------------

}
