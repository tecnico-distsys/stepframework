package org.tripplanner.flight.perf;

import step.groovy.Helper;
import org.tripplanner.flight.perf.report_generator.*;


/**
 *  Produce reports from analyzed test runs
 *
 *  @author Miguel Pardal
 */

 // command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "ReportGenerator")
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

def instanceDir = config.perf.flight.report.instanceDir;
def instanceFileNamePattern = ~config.perf.flight.report.instanceFileNameRegex;

def outputBaseDir = config.perf.flight.report.outputBaseDir;

def statsOutputBaseDir = config.perf.flight.stats.outputBaseDir;

// -----------------------------------------------------------------------------

def argv;

// iterate all report instances
instanceDir.eachFileMatch(instanceFileNamePattern) { file ->

    def reportConfig = Helper.parseConfig(file.path);
    reportConfig = statsConfig.perf.flight.report.instance;
    assert (reportConfig) : "Expecting flight report instance configuration file"

    final def SAMPLES = statsConfig.numberSamples;
    assert (SAMPLES >= 1)

    def runId = statsConfig.runId;
    def filterId = statsConfig.filterId;
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
        def sampleStatisticsFileName = config.perf.flight.stats.samplesFileName;
        def sampleStatisticsFile = new File(outputDir, sampleStatisticsFileName);

        for (int i=0; i < SAMPLES; i++) {
            def filteredRequestsFileName = String.format(config.perf.flight.stats.filteredRequestsFileNameFormat, i+1);
            def filteredRequestsFile = new File(outputDir, filteredRequestsFileName);

            argv = ["-i", filteredRequestsFile as String,
                    "-o", sampleStatisticsFile as String,
                    "--append", (i == 0 ? "false" : "true")];
            CSVSampleStatistics.main(argv as String[]);
        }

        println("compute overall statistics"); // ------------------------------
        def overallStatisticsFileName = config.perf.flight.stats.overallFileName;
        def overallStatisticsFile = new File(outputDir, overallStatisticsFileName);

        argv = ["-i", sampleStatisticsFile as String,
                "-o", overallStatisticsFile as String]
        CSVOverallStatistics.main(argv as String[]);

    } else {
        println("Skipping statistics " + statsId);
    }

}

// -----------------------------------------------------------------------------
