/**
 *  Analyze test runs
 *
 *  @author Miguel Pardal
 */
 
 // command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "Analyzer")
cli._(longOpt: "force", required: false, args: 0, "Force load generation")

def options = cli.parse(args)
assert (options)

// load initial configuration --------------------------------------------------
def config = ConfigHelper.parseMany("Shared.config.groovy", "Domain.config.groovy");

assert (config.flight.shared) : "Expecting flight shared configuration file"
assert (config.flight.domain) : "Expecting flight domain configuration file"

def runDir = new File(config.flight.shared.dir.run);
assert (runDir.exists() && runDir.isDirectory())

def statisticsDir = new File(config.flight.shared.dir.statistics);
assert (statisticsDir.exists() && statisticsDir.isDirectory())

def statisticsFilePattern = ~config.flight.shared.pattern.statistics;
assert (statisticsFilePattern)

// -----------------------------------------------------------------------------

def argv;

// execute workloads for each modified run file
statisticsDir.eachFileMatch(statisticsFilePattern) { file ->
    def statisticsConfig = ConfigHelper.parseMany(file.path);
    assert (statisticsConfig.flight.statistics) : "Expecting flight statistics configuration file"

    final def SAMPLES = statisticsConfig.flight.statistics.numberSamples;
    assert (SAMPLES >= 1)

    def runId = statisticsConfig.flight.statistics.runId;
    def filterId = statisticsConfig.flight.statistics.filterId;
    def statisticsId = runId + "_" + filterId;

    def selectedRunDir = new File(runDir, runId);
    assert (selectedRunDir.exists() && selectedRunDir.isDirectory()): "Run not found"
    assert (selectedRunDir.listFiles().size() >= SAMPLES): "Not enough run samples"

    def sourceFile = file;
    def targetDir = new File(statisticsDir, statisticsId);

    // check if execution is necessary
    if (options.force ||
        !targetDir.exists() ||
        targetDir.listFiles().size() == 0) {

        if (!targetDir.exists())
            targetDir.mkdir();
        assert (targetDir.exists() && targetDir.isDirectory())

        println("Processing statistics " + statisticsId);

        for (int i=0; i < SAMPLES; i++) {
            def perfLogFileName = String.format(config.flight.shared.format.runPerfLog, i+1);
            def perfLogFile = new File(selectedRunDir, perfLogFileName);

            def requestsFileName = String.format(config.flight.shared.format.requests, i+1);
            def requestsFile = new File(targetDir, requestsFileName);

            // generate request records
            argv = ["-i", perfLogFile as String,
                    "-o", requestsFile as String]
            Perf4JRequestRecords.main(argv as String[]);

        }


        def sampleStatisticsFileName = config.flight.shared.fileName.sampleStatistics;
        def sampleStatisticsFile = new File(targetDir, sampleStatisticsFileName);

        for (int i=0; i < SAMPLES; i++) {
            def requestsFileName = String.format(config.flight.shared.format.requests, i+1);
            def requestsFile = new File(targetDir, requestsFileName);

            // compute sample statistics
            argv = ["-i", requestsFile as String,
                    "-o", sampleStatisticsFile as String,
                    "--append", (i == 0 ? "false" : "true")];
            CSVSampleStatistics.main(argv as String[]);
        }


        def overallStatisticsFileName = config.flight.shared.fileName.overallStatistics;
        def overallStatisticsFile = new File(targetDir, overallStatisticsFileName);

        // compute overall statistics
        argv = ["-i", sampleStatisticsFile as String,
                "-o", overallStatisticsFile as String]
        CSVOverallStatistics.main(argv as String[]);

    } else {
        println("Skipping statistics " + statisticsId);
    }

}

// -----------------------------------------------------------------------------
