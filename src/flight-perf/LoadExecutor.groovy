/**
 *  Execute test workloads
 *
 *  @author Miguel Pardal
 */

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "LoadExecutor")
cli._(longOpt: "force", required: false, args: 0, "Force load generation")
cli._(longOpt: "nocompile", required: false, args: 0, "Prevent compilation")
cli._(longOpt: "nodomaingen", required: false, args: 0, "Prevent domain data generation")

def options = cli.parse(args)
assert (options)

// load initial configuration --------------------------------------------------
def config = ConfigHelper.parseMany("Shared.config.groovy", "Domain.config.groovy");

assert (config.flight.shared) : "Expecting flight shared configuration file"
assert (config.flight.domain) : "Expecting flight domain configuration file"

def loadDir = new File(config.flight.shared.dir.load);
assert (loadDir.exists() && loadDir.isDirectory())

def runDir = new File(config.flight.shared.dir.run);
assert (runDir.exists() && runDir.isDirectory())

def configDir = new File(config.flight.shared.dir.config);
assert (configDir.exists() && configDir.isDirectory())

def runFilePattern = ~config.flight.shared.pattern.run;
assert (runFilePattern)

// -----------------------------------------------------------------------------

def argv;
def compileApplication = true;
def generateDomainData = true;

if (options.nocompile) compileApplication = false;
if (options.nodomaingen) generateDomainData = false;

// execute workloads for each modified run file
runDir.eachFileMatch(runFilePattern) { file ->
    def runConfig = ConfigHelper.parseMany(file.path);
    assert (runConfig.flight.run) : "Expecting flight run configuration file"

    final def SAMPLES = runConfig.flight.run.numberSamples;
    assert (SAMPLES >= 1)

    def loadId = runConfig.flight.run.loadId;
    def configId = runConfig.flight.run.configId;
    def runId = loadId + "_" + configId;

    def selectedLoadDir = new File(loadDir, loadId);
    assert (selectedLoadDir.exists() && selectedLoadDir.isDirectory()): "Load not found"
    assert (selectedLoadDir.listFiles().size() >= SAMPLES): "Not enough load samples"

    def sourceFile = file;
    def targetDir = new File(runDir, runId);

    // check if execution is necessary
    if (options.force ||
        !targetDir.exists() || 
        targetDir.listFiles().size() == 0) {

        if (!targetDir.exists())
            targetDir.mkdir();
        assert (targetDir.exists() && targetDir.isDirectory())

        println("Processing run " + runId);

        // compile
        if (compileApplication) {
            ShellHelper.exec("..", "ant rebuild")
            compileApplication = false;
        }

        for (int i=0; i < SAMPLES; i++) {
            def loadFileName = String.format(config.flight.shared.format.load, i+1);
            def loadFile = new File(selectedLoadDir, loadFileName);

            def outputFileName = String.format(config.flight.shared.format.runOutput, i+1);
            def outputFile = new File(new File(config.flight.shared.dir.run, runId), outputFileName);

            // regenerate domain data
            if (generateDomainData) {
                argv = [ ];
                DomainDataGenerator.main(argv as String[]);
            }

            // start a clean server
            ShellHelper.exec("..", "ant start-server!")

            // deploy flight web service
            ShellHelper.exec("..", "ant deploy-flight")

            println("Executing sample " + (i+1) + " to " + outputFile.getCanonicalPath());

            // execute workload ------------------------------------------------
            argv = ["-i", loadFile as String,
                    "-o", outputFile as String,
                    "--endpoint", config.flight.shared.endpoint.run as String]
            VirtualUser.main(argv as String[]);
            // -----------------------------------------------------------------

            // stop server
            ShellHelper.exec("..", "ant stop-server")

            // retrieve CATALINA_HOME path from environment variables
            def env = System.getenv();
            def catalinaHomePath = env["CATALINA_HOME"];
            assert (catalinaHomePath)
            def catalinaHomeDir = new File(catalinaHomePath);
            def catalinaLogsDir = new File(catalinaHomeDir, "logs");
            assert (catalinaLogsDir.exists() && catalinaLogsDir.isDirectory())

            // copy (aggregate) performance log
            def sourcePerfLogFileName = config.flight.shared.fileName.runPerfLog;
            def sourcePerfLogFile = new File(catalinaLogsDir, sourcePerfLogFileName);
            assert (sourcePerfLogFile.exists())

            def targetPerfLogFileName = String.format(config.flight.shared.format.runPerfLog, i+1);
            def targetPerfLogFile = new File(targetDir, targetPerfLogFileName);

            if (runConfig.flight.run.aggregatePerfLog) {
                // aggregate
                argv = ["-i", sourcePerfLogFile as String,
                        "-o", targetPerfLogFile as String]
                Perf4JAggregateContiguousEntries.main(argv as String[]);
            } else {
                // copy
                def ant = new AntBuilder();
                ant.sequential {
                    ant.copy(file: sourcePerfLogFile as String, tofile: targetPerfLogFile as String)
                }
            }

            if (runConfig.flight.run.saveLog) {
                // copy log
                def sourceLogFileName = config.flight.shared.fileName.runLog;
                def sourceLogFile = new File(catalinaLogsDir, sourceLogFileName);
                assert (sourceLogFile.exists())

                def targetLogFileName = String.format(config.flight.shared.format.runLog, i+1);
                def targetLogFile = new File(targetDir, targetLogFileName);

                def ant = new AntBuilder();
                ant.sequential {
                    ant.copy(file: sourceLogFile as String, tofile: targetLogFile as String)
                }
            }

        }

    } else {
        println("Skipping run " + runId);
    }

}

// -----------------------------------------------------------------------------
