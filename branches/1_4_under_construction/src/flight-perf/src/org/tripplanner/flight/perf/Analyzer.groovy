package org.tripplanner.flight.perf;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import step.groovy.Helper;
import org.tripplanner.flight.perf.analyzer.*;


/**
 *  Analyze test runs
 *
 *  @author Miguel Pardal
 */

 // command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "Analyzer")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli._(longOpt: "force", required: false, args: 0, "Force analysis")

def options = cli.parse(args)
assert (options)

if (options.help) {
    cli.usage();
    return;
}

// load initial configuration --------------------------------------------------
def config = Helper.parseConfig("etc/config/Config.groovy");
assert (config.perf.flight) : "Expecting flight performance configuration file"
Helper.configStringToFile(config);

def instanceDir = config.perf.flight.stats.instanceDir;
def instanceFileNamePattern = ~config.perf.flight.stats.instanceFileNameRegex;

def outputBaseDir = config.perf.flight.stats.outputBaseDir;

def runOutputBaseDir = config.perf.flight.run.outputBaseDir;

// -----------------------------------------------------------------------------

def argv;

// iterate all stats instances
instanceDir.eachFileMatch(instanceFileNamePattern) { file ->

    def statsConfig = Helper.parseConfig(file.path);
    statsConfig = statsConfig.perf.flight.stats.instance;
    assert (statsConfig) : "Expecting flight stats instance configuration file"

    final def SAMPLES = statsConfig.numberSamples;
    assert (SAMPLES >= 1)

    def runId = statsConfig.runId;
    assert(runId ==~ "[A-Za-z0-9_]+") : "Invalid run identifier"

    def filterId = statsConfig.filterId;
    assert(filterId ==~ "[A-Za-z0-9]*") : "Invalid filter identifier"

    def statsId = runId + "_" + filterId;

    def runOutputDir = new File(runOutputBaseDir, runId);
    assert (runOutputDir.exists() && runOutputDir.isDirectory()): "Run not found"
    assert (runOutputDir.listFiles().size() >= SAMPLES): "Not enough run samples"

    def outputDir = new File(outputBaseDir, statsId);

    // check if execution is necessary
    if (options.force ||
        !outputDir.exists() ||
        outputDir.listFiles().size() == 0) {

        if (!outputDir.exists())
            outputDir.mkdir();
        assert (outputDir.exists() && outputDir.isDirectory())

        println("Processing statistics " + statsId);
        println("Output directory: " + outputDir.canonicalPath);

        println("generate request records"); // --------------------------------
        for (int i=0; i < SAMPLES; i++) {
            def perfLogFileName = String.format(config.perf.flight.run.outputPerfLogFileNameFormat, i+1);
            def perfLogFile = new File(runOutputDir, perfLogFileName);

            def requestsFileName = String.format(config.perf.flight.stats.requestsFileNameFormat, i+1);
            def requestsFile = new File(outputDir, requestsFileName);

            argv = ["-i", perfLogFile as String,
                    "-o", requestsFile as String]
            Perf4JRequestRecords.main(argv as String[]);


            // filter --------------------------------------------------------------
            def filteredRequestsFileName = String.format(config.perf.flight.stats.filteredRequestsFileNameFormat, i+1);
            def filteredRequestsFile = new File(outputDir, filteredRequestsFileName);

            // read headers
            CsvListReader csvLR = new CsvListReader(new FileReader(requestsFile), CsvPreference.STANDARD_PREFERENCE);
            def csvHeaders = csvLR.read();
            csvLR.close();

            // write headers
            CsvListWriter csvLW = new CsvListWriter(new FileWriter(filteredRequestsFile), CsvPreference.STANDARD_PREFERENCE);
            csvLW.write(csvHeaders);
            csvLW.close();

            // read records
            CsvMapReader csvMR = new CsvMapReader(new FileReader(requestsFile), CsvPreference.STANDARD_PREFERENCE);
            CsvMapWriter csvMW = new CsvMapWriter(new FileWriter(filteredRequestsFile, true), CsvPreference.STANDARD_PREFERENCE);

            def csvHeadersArray = csvHeaders as String[];
            // ignore 1st line (headers)
            csvMR.read(csvHeadersArray);

            // define filter closure
            def filterClosure = statsConfig.filterClosure;
            if (!filterClosure) filterClosure = { return true; }

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
            // -----------------------------------------------------------------
        }

        println("compute sample statistics"); // -------------------------------
        def sampleStatsFileName = config.perf.flight.stats.samplesFileName;
        def sampleStatsFile = new File(outputDir, sampleStatsFileName);

        def sampleStatsTextFileName = config.perf.flight.stats.samplesTextFileName;
        def sampleStatsTextFile = new File(outputDir, sampleStatsTextFileName);

        for (int i=0; i < SAMPLES; i++) {
            def filteredRequestsFileName = String.format(config.perf.flight.stats.filteredRequestsFileNameFormat, i+1);
            def filteredRequestsFile = new File(outputDir, filteredRequestsFileName);

            argv = ["-i", filteredRequestsFile as String,
                    "-o", sampleStatsFile as String,
                    "-n", i+1 as String,
                    "--append", (i == 0 ? "false" : "true")];
            CSVSampleStatistics.main(argv as String[]);

            argv = ["-i", filteredRequestsFile as String,
                    "-o", sampleStatsTextFile as String,
                    "-n", i+1 as String,
                    "--append", (i == 0 ? "false" : "true"),
                    "--format", "text"];
            CSVSampleStatistics.main(argv as String[]);
        }

        println("compute overall statistics"); // ------------------------------
        def overallStatsFileName = config.perf.flight.stats.overallFileName;
        def overallStatsFile = new File(outputDir, overallStatsFileName);

        argv = ["-i", sampleStatsFile as String,
                "-o", overallStatsFile as String]
        CSVOverallStatistics.main(argv as String[]);

        def overallStatsTextFileName = config.perf.flight.stats.overallTextFileName;
        def overallStatsTextFile = new File(outputDir, overallStatsTextFileName);

        argv = ["-i", sampleStatsFile as String,
                "-o", overallStatsTextFile as String,
                "--format", "text"];
        CSVOverallStatistics.main(argv as String[]);

    } else {
        println("Skipping statistics " + statsId);
    }

}

// -----------------------------------------------------------------------------
