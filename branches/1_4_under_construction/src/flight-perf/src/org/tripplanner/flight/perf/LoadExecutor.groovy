package org.tripplanner.flight.perf;

import step.groovy.Helper;
import org.tripplanner.flight.perf.domain_data_generator.*;
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
cli._(longOpt: "keeptemps", required: false, args: 0, "Do not delete temporary files")

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
    assert(loadId ==~ "[A-Za-z0-9]+")

    def configId = runConfig.configId;
    assert(configId ==~ "[A-Za-z0-9]*")

    def runId = loadId + "_" + configId;

    def loadOutputDir = new File(loadOutputBaseDir, loadId);
    assert (loadOutputDir.exists() && loadOutputDir.isDirectory())
    assert (loadOutputDir.listFiles().size() >= SAMPLES)

    def defaultConfigFilesDir = new File(configFilesBaseDir, config.perf.flight.run.defaultConfigId);
    assert (defaultConfigFilesDir.exists() && defaultConfigFilesDir.isDirectory())

    def configFilesDir = null;
    if (configId) {
        configFilesDir = new File(configFilesBaseDir, configId);
        assert (configFilesDir.exists() && configFilesDir.isDirectory())
    }

    def outputDir = new File(outputBaseDir, runId);

    // check if execution is necessary
    if (options.force ||
        !outputDir.exists()) {

        if (!outputDir.exists())
            outputDir.mkdir();
        assert (outputDir.exists() && outputDir.isDirectory())

        println("Processing run " + runId);
        println("Output directory: " + outputDir.canonicalPath);

        // output system information
        def sysInfoFile = new File(outputDir, config.perf.flight.run.outputSysInfoFileName);
        def sysInfoPrintStream = new PrintStream(new FileOutputStream(sysInfoFile));

        sysInfoPrintStream.printf("%nEnvironment:%n%n");
        System.getenv().each { key, value ->
            sysInfoPrintStream.printf("%s = %s%n", key, value);
        }
        sysInfoPrintStream.printf("%nSystem properties:%n%n");
        System.getProperties().each { key, value ->
            sysInfoPrintStream.printf("%s = %s%n", key, value);
        }

        // create temporary directory
        def tempSourceCodeDir = File.createTempFile("step_" + runId, "");
        tempSourceCodeDir.delete();
        tempSourceCodeDir.mkdir();

        // copy source code
        println("Copying source code to temporary location " + tempSourceCodeDir.absolutePath);
        ant.copy(todir: tempSourceCodeDir.absolutePath) {
            ant.fileset(dir: config.perf.flight.sourceCodeDir.absolutePath) {
                ant.exclude(name: "**/build/**/*")
                ant.exclude(name: "**/dist/**/*")
                ant.exclude(name: "flight-perf/**/*")
                /* svn directories are part of Ant's default excludes */
            }
        }

        // define apply configuration closure
        def applyConfigClosure = { sourceDir, targetDir ->
            sourceDir.eachFileRecurse { sourceFile ->
                // ignore directories
                if (sourceFile.isDirectory()) return;
                // ignore files contained in SVN directories
                if (sourceFile.absolutePath.indexOf(".svn") != -1) return;

                def targetFile = new File(targetDir.absolutePath +
                    sourceFile.absolutePath.substring(sourceDir.absolutePath.size()))

                if (targetFile.exists()) {
                    // create backup of target file
                    ant.move(file: targetFile.absolutePath,
                             tofile: targetFile.absolutePath + ".bak",
                             overwrite: "true")
                }
                // overwrite file
                ant.copy(file: sourceFile.absolutePath,
                         tofile: targetFile.absolutePath,
                         overwrite: "true")
            }
        }

        // apply default configuration
        println("Applying default configuration");
        applyConfigClosure(defaultConfigFilesDir, tempSourceCodeDir);

        // apply specific configuration
        if (configFilesDir != null) {
            println("Applying specific configuration from " + configFilesDir.absolutePath);
            applyConfigClosure(configFilesDir, tempSourceCodeDir);
        }

        println("compile"); // -------------------------------------------------
        Helper.exec(tempSourceCodeDir.absolutePath, "ant rebuild", ["CLASSPATH" : ""] );

        println("generate domain data"); // ------------------------------------
        if (generateDomainData) {
            def argv = [ ];
            DomainDataGenerator.main(argv as String[]);
            generateDomainData = false;
        }

        // for each sample -----------------------------------------------------
        for (int i=0; i < SAMPLES; i++) {
            // file containing requests to send
            def loadFileName = String.format(config.perf.flight.load.outputFileNameFormat, i+1);
            def loadFile = new File(loadOutputDir, loadFileName);

            // file where standard output will be saved
            def outputFileName = String.format(config.perf.flight.run.outputFileNameFormat, i+1);
            def outputFile = new File(outputDir, outputFileName);

            // delete data generated by requests -------------------------------
            def argv = ["-p", config.perf.flight.databasePropertiesFile.absolutePath,
                        "--quick", "true"];
            DeleteDB.main(argv as String[]);

            // start a clean server --------------------------------------------
            Helper.exec(tempSourceCodeDir.absolutePath, "ant start-server!", ["CLASSPATH" : ""]);

            // deploy flight web service ---------------------------------------
            Helper.exec(tempSourceCodeDir.absolutePath, "ant deploy-flight", ["CLASSPATH" : ""]);

            println("Executing sample " + (i+1) + " to " + outputFile.getCanonicalPath());

            // execute workload ------------------------------------------------
            def argv = ["-i", loadFile.absolutePath,
                        "-o", outputFile.absolutePath,
                        "--endpoint", config.perf.flight.run.endpoint as String]
            VirtualUser.main(argv as String[]);

            // stop server -----------------------------------------------------
            Helper.exec(tempSourceCodeDir.absolutePath, "ant stop-server", ["CLASSPATH" : ""])

            // retrieve CATALINA_HOME path from environment variables
            def env = System.getenv();
            def catalinaHomePath = env["CATALINA_HOME"];
            assert (catalinaHomePath)
            def catalinaHomeDir = new File(catalinaHomePath);
            def catalinaLogsDir = new File(catalinaHomeDir, "logs");
            def catalinaTempDir = new File(catalinaHomeDir, "temp");

            // save functional log data ----------------------------------------
            def sourceLogFileName = config.perf.flight.run.logFileName;
            def sourceLogFile = new File(catalinaLogsDir, sourceLogFileName);
            assert (sourceLogFile.exists())

            def targetLogSizeFileName = String.format(config.perf.flight.run.outputLogSizeFileNameFormat, i+1);
            def targetLogSizeFile = new File(outputDir, targetLogSizeFileName);

            // save size in file
            def targetLogSizeFileWriter = new FileWriter(targetLogSizeFile);
            targetLogSizeFileWriter.write(sourceLogFile.length() as String);
            targetLogSizeFileWriter.close();

            // save log contents
            if (runConfig.saveLog) {
                def targetLogFileName = String.format(config.perf.flight.run.outputLogFileNameFormat, i+1);
                def targetLogFile = new File(outputDir, targetLogFileName);

                ant.copy(file: sourceLogFile.absolutePath, tofile: targetLogFile.absolutePath)
            }

            // save performance log data ---------------------------------------

            def format = runConfig.perfLogFormat;

            switch (format) {
                case "perf4j":
                    // aggregate or copy performance log -----------------------
                    def perf4JLogFile = new File(catalinaLogsDir, config.perf.flight.run.perf4JLogFileName);
                    assert perf4JLogFile.exists()

                    def outputPerf4JLogFileName = String.format(config.perf.flight.run.outputPerf4JLogFileNameFormat, i+1);
                    def outputPerf4JLogFile = new File(outputDir, outputPerf4JLogFileName);

                    if (runConfig.perf4j.aggregateContiguousEntries) {
                        println("aggregate performance log");
                        def argv = ["-i", perf4JLogFile.absolutePath,
                                    "-o", outputPerf4JLogFile.absolutePath]
                        Perf4JAggregateContiguousEntries.main(argv as String[]);
                    } else {
                        println("copy performance log");
                        ant.move(file: perf4JLogFile.absolutePath, tofile: outputPerf4JLogFile.absolutePath)
                    }
                    break;
                case "eventmon":
                    def eventMonLogDir = catalinaTempDir;
                    def eventMonLogFileNamePattern = ~config.perf.flight.run.eventMonLogFileNameRegex;

                    def outputEventMonLogFileName = String.format(config.perf.flight.run.outputEventMonLogFileNameFormat, i+1);
                    def outputEventMonLogFile = new File(outputDir, outputEventMonLogFileName);

                    println("merge thread files into single log");
                    eventMonLogDir.eachFileMatch(eventMonLogFileNamePattern) { eventMonFile ->
                        def argv = ["-i", eventMonFile.absolutePath,
                                    "-o", outputEventMonLogFile.absolutePath,
                                    "-regex", config.perf.flight.run.eventMonLogFileNameRegex as String ]
                        MonAppendLog.main(argv as String[]);
                    }
                    break;
                case "layermon":
                    def layerMonLogDir = catalinaTempDir;
                    def layerMonLogFileNamePattern = ~config.perf.flight.run.layerMonLogFileNameRegex;

                    def outputLayerMonLogFileName = String.format(config.perf.flight.run.outputLayerMonLogFileNameFormat, i+1);
                    def outputLayerMonLogFile = new File(outputDir, outputLayerMonLogFileName);

                    println("merge thread files into single log");
                    layerMonLogDir.eachFileMatch(layerMonLogFileNamePattern) { layerMonFile ->
                        def argv = ["-i", layerMonFile.absolutePath,
                                    "-o", outputLayerMonLogFile.absolutePath,
                                    "-regex", config.perf.flight.run.layerMonLogFileNameRegex as String ]
                        MonAppendLog.main(argv as String[]);
                    }
                    break;
                default:
                    assert false : "Unsupported performance log format " + format
            }

        } // for each sample

        // delete temporary source code directory
        if (!options.keeptemps) {
            ant.delete(dir: tempSourceCodeDir, deleteonexit: "true")
        }

    } else {
        println("Skipping run " + runId);
    }

}

// -----------------------------------------------------------------------------
