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
assert (config.perf.flight) : "Expecting flight performance configuration file"
Helper.configStringToFile(config);

def instanceDir = config.perf.flight.stats.instanceDir;
def instanceFileNamePattern = ~config.perf.flight.stats.instanceFileNameRegex;

def outputBaseDir = config.perf.flight.stats.outputBaseDir;

def runOutputBaseDir = config.perf.flight.run.outputBaseDir;

// -----------------------------------------------------------------------------


// iterate all stats instances
instanceDir.eachFileMatch(instanceFileNamePattern) { file ->

    def statsConfig = Helper.parseConfig(file.path);
    statsConfig = statsConfig.perf.flight.stats.instance;
    assert (statsConfig) : "Expecting flight stats instance configuration file"

    final def SAMPLES = statsConfig.numberSamples;
    assert (SAMPLES >= 1)

    def runId = statsConfig.runId;
    assert (runId ==~ "[A-Za-z0-9_]+")

    def filterId = statsConfig.filterId;
    assert (filterId ==~ "[A-Za-z0-9]*")

    def statsId = runId + "_" + filterId;

    def runOutputDir = new File(runOutputBaseDir, runId);
    assert (runOutputDir.exists() && runOutputDir.isDirectory())
    assert (runOutputDir.listFiles().size() >= SAMPLES)

    def outputDir = new File(outputBaseDir, statsId);

    // check if execution is necessary
    if (options.force ||
        !outputDir.exists()) {

        if (!outputDir.exists())
            outputDir.mkdir();
        assert (outputDir.exists() && outputDir.isDirectory())

        println("Processing statistics " + statsId);
        println("Output directory: " + outputDir.canonicalPath);

        // generate request records --------------------------------------------
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
        def genRequestRecordsClosureArray = Helper.indexCurryClosureArray(genRequestRecordsClosure, SAMPLES);
        // execute closures in parallel
        Helper.multicoreExecute(genRequestRecordsClosureArray);
        // ---------------------------------------------------------------------


        // filter records ------------------------------------------------------
        def filterClosure = statsConfig.filterClosure;
        if (filterClosure) {

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
            def filterRecordsClosureArray = Helper.indexCurryClosureArray(filterRecordsClosure, SAMPLES);
            // execute closures in parallel
            Helper.multicoreExecute(filterRecordsClosureArray);
        }
        // ---------------------------------------------------------------------

        // adjust hibernate times ----------------------------------------------
        if (statsConfig.adjustHibernateTimes) {

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
            def adjustHibernateTimesClosureArray = Helper.indexCurryClosureArray(adjustHibernateTimesClosure, SAMPLES);
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
        def sampleStatsClosureArray = Helper.indexCurryClosureArray(sampleStatsClosure, SAMPLES);
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

    } else {
        println("Skipping statistics " + statsId);
    }

}

// -----------------------------------------------------------------------------
