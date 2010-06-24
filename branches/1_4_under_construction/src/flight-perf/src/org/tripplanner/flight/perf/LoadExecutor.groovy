package org.tripplanner.flight.perf;

import step.groovy.Helper;
import org.tripplanner.flight.perf.load_executor.*;
import org.tripplanner.flight.perf.analyzer.*;


/**
 *  Execute test workloads
 *
 *  @author Miguel Pardal
 */

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "LoadExecutor")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli._(longOpt: "force", required: false, args: 0, "Force load execution")
cli._(longOpt: "nodomaingen", required: false, args: 0, "Prevent domain data generation")

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

def instanceDir = config.perf.flight.run.instanceDir;
def instanceFileNamePattern = ~config.perf.flight.run.instanceFileNameRegex;

def configFilesBaseDir = config.perf.flight.run.configFilesBaseDir;

def outputBaseDir = config.perf.flight.run.outputBaseDir;

def loadOutputBaseDir = config.perf.flight.load.outputBaseDir;


// -----------------------------------------------------------------------------

def argv;

def ant = new AntBuilder();

def compileApplication = true;
def generateDomainData = true;

if (options.nodomaingen) generateDomainData = false;

// iterate all run instances
instanceDir.eachFileMatch(instanceFileNamePattern) { file ->

    def runConfig = Helper.parseConfig(file.path);
    runConfig = runConfig.perf.flight.run.instance;
    assert (runConfig) : "Expecting flight run instance configuration file"

    final def SAMPLES = runConfig.numberSamples;
    assert (SAMPLES >= 1)

    def loadId = runConfig.loadId;
    assert(loadId ==~ "[A-Za-z0-9]+") : "Invalid load identifier"

    def configId = runConfig.configId;
    assert(configId ==~ "[A-Za-z0-9]+") : "Invalid config identifier"

    def runId = loadId + "_" + configId;

    def loadOutputDir = new File(loadOutputBaseDir, loadId);
    assert (loadOutputDir.exists() && loadOutputDir.isDirectory()): "Load not found"
    assert (loadOutputDir.listFiles().size() >= SAMPLES): "Not enough load samples"

    def configFilesDir;
    if (!configId) {
        configFilesDir = new File(configFilesBaseDir, config.perf.flight.run.defaultConfigId);
    } else {
        configFilesDir = new File(configFilesBaseDir, configId);
    }
    assert (configFilesDir.exists() && configFilesDir.isDirectory()): "Configuration files not found"

    def outputDir = new File(outputBaseDir, runId);

    // check if execution is necessary
    if (options.force ||
        !outputDir.exists() ||
        outputDir.listFiles().size() == 0) {

        if (!outputDir.exists())
            outputDir.mkdir();
        assert (outputDir.exists() && outputDir.isDirectory())

        println("Processing run " + runId);
        println("Output directory: " + outputDir.canonicalPath);

        println("apply configuration"); // -------------------------------------
        def sourceDir = configFilesDir;
        def targetDir = config.perf.flight.sourceCodeDir;

        sourceDir.eachFileRecurse { sourceFile ->
            // ignore directories
            if (sourceFile.isDirectory()) return;
            // ignore files contained in SVN directories
            if (sourceFile.absolutePath.indexOf(".svn") != -1) return;

            def targetFile = new File(targetDir.absolutePath +
                sourceFile.absolutePath.substring(sourceDir.absolutePath.size()))

            if (targetFile.exists()) {
                ant.sequential {
                    // create backup of target file
                    ant.move(file: targetFile.absolutePath,
                             tofile: targetFile.absolutePath + ".bak",
                             overwrite: "true")
                    // replace it with a copy of source file
                    ant.copy(file: sourceFile.absolutePath,
                             tofile: targetFile.absolutePath,
                             overwrite: "true")
                }
            }
        }

        println("compile"); // -------------------------------------------------
        Helper.exec("..", "ant rebuild", ["CLASSPATH" : ""] );

        // for each sample -----------------------------------------------------
        for (int i=0; i < SAMPLES; i++) {
            // file containing requests to send
            def loadFileName = String.format(config.perf.flight.load.outputFileNameFormat, i+1);
            def loadFile = new File(loadOutputDir, loadFileName);

            // file where standard output will be saved
            def outputFileName = String.format(config.perf.flight.run.outputFileNameFormat, i+1);
            def outputFile = new File(outputDir, outputFileName);

            // regenerate domain data ------------------------------------------
            if (generateDomainData) {
                argv = [ ];
                DomainDataGenerator.main(argv as String[]);
            }

            // start a clean server --------------------------------------------
            Helper.exec("..", "ant start-server!", ["CLASSPATH" : ""]);

            // deploy flight web service ---------------------------------------
            Helper.exec("..", "ant deploy-flight", ["CLASSPATH" : ""]);

            println("Executing sample " + (i+1) + " to " + outputFile.getCanonicalPath());

            // execute workload ------------------------------------------------
            argv = ["-i", loadFile as String,
                    "-o", outputFile as String,
                    "--endpoint", config.perf.flight.run.endpoint as String]
            VirtualUser.main(argv as String[]);

            // stop server -----------------------------------------------------
            Helper.exec("..", "ant stop-server", ["CLASSPATH" : ""])

            // retrieve CATALINA_HOME path from environment variables
            def env = System.getenv();
            def catalinaHomePath = env["CATALINA_HOME"];
            assert (catalinaHomePath)
            def catalinaHomeDir = new File(catalinaHomePath);
            def catalinaLogsDir = new File(catalinaHomeDir, "logs");
            assert (catalinaLogsDir.exists() && catalinaLogsDir.isDirectory())

            // aggregate or copy performance log -------------------------------
            def sourcePerfLogFileName = config.perf.flight.run.perfLogFileName;
            def sourcePerfLogFile = new File(catalinaLogsDir, sourcePerfLogFileName);
            assert (sourcePerfLogFile.exists())

            def targetPerfLogFileName = String.format(config.perf.flight.run.outputPerfLogFileNameFormat, i+1);
            def targetPerfLogFile = new File(outputDir, targetPerfLogFileName);

            if (runConfig.aggregatePerfLog) {
                println("aggregate performance log");
                argv = ["-i", sourcePerfLogFile as String,
                        "-o", targetPerfLogFile as String]
                Perf4JAggregateContiguousEntries.main(argv as String[]);
            } else {
                println("copy performance log");
                ant.sequential {
                    ant.copy(file: sourcePerfLogFile as String, tofile: targetPerfLogFile as String)
                }
            }

            // copy functional log ---------------------------------------------
            if (runConfig.saveLog) {
                println("copy functional log");
                def sourceLogFileName = config.perf.flight.run.logFileName;
                def sourceLogFile = new File(catalinaLogsDir, sourceLogFileName);
                assert (sourceLogFile.exists())

                def targetLogFileName = String.format(config.perf.flight.run.outputLogFileNameFormat, i+1);
                def targetLogFile = new File(outputDir, targetLogFileName);

                ant.sequential {
                    ant.copy(file: sourceLogFile as String, tofile: targetLogFile as String)
                }
            }

        } // for each sample

        println("restore configuration"); // -----------------------------------
        sourceDir.eachFileRecurse { sourceFile ->
            // ignore directories
            if (sourceFile.isDirectory()) return;
            // ignore files contained in SVN directories
            if (sourceFile.absolutePath.indexOf(".svn") != -1) return;

            def targetFile = new File(targetDir.absolutePath +
                sourceFile.absolutePath.substring(sourceDir.absolutePath.size()))

            if (targetFile.exists()) {
                ant.sequential {
                    // restore target file from backup
                    ant.move(file: targetFile.absolutePath + ".bak",
                             tofile: targetFile.absolutePath,
                             overwrite: "true")
                    // delete backup file
                    ant.delete(file: targetFile.absolutePath + ".bak")
                }
            }
        }

    } else {
        println("Skipping run " + runId);
    }

}

// -----------------------------------------------------------------------------
