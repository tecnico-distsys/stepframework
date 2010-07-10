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
cli.cfg(longOpt: "config", required: false, args: 1, "Specify master configuration file")
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
def configPath = "etc/config/Config.groovy";
if (options.cfg) configPath = options.cfg;

def config = Helper.parseConfig(configPath);
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

println("Looking for run instances in " + instanceDir.canonicalPath);
println("(files matching (" + instanceFileNamePattern + ")");
println("");

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
    assert loadOutputDir.exists() && loadOutputDir.isDirectory()
    assert loadOutputDir.listFiles().size() >= SAMPLES

    def outputDir = new File(outputBaseDir, runId);

    // check if execution is necessary
    if (options.force ||
        !outputDir.exists()) {

        if (!outputDir.exists())
            outputDir.mkdir();
        assert outputDir.exists() && outputDir.isDirectory()

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

        // output run information
        def runInfoFile = new File(outputDir, config.perf.flight.run.outputRunInfoFileName);
        def runInfoPrintStream = new PrintStream(new FileOutputStream(runInfoFile));

        runInfoPrintStream.printf("Load identifier '%s'%n", loadId);
        runInfoPrintStream.printf("Config identifier '%s'%n", configId);
        runInfoPrintStream.printf("%ntoString: %s%n", runConfig.toString());


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

        // apply configuration files
        def configFilesDirNameList = runConfig.applyConfigList;
        assert configFilesDirNameList
        configFilesDirNameList.each { configFilesDirName ->

            def configFilesDir = new File(configFilesBaseDir, configFilesDirName);
            assert configFilesDir.exists() && configFilesDir.isDirectory()

            println("Applying " + configFilesDirName + " configuration files");
            applyConfigClosure(configFilesDir, tempSourceCodeDir);
        }


        println("compile"); // -------------------------------------------------
        Helper.exec(tempSourceCodeDir.absolutePath, "ant rebuild", ["CLASSPATH" : ""] );

        println("generate domain data"); // ------------------------------------
        if (generateDomainData) {
            DomainDataGenerator.main(
                [
                "-cfg", configPath
                ] as String[]
            );
            generateDomainData = false;
        }


        // println("generate domain data"); // ------------------------------------
        def virtualUserClosure = { i ->
            // file containing requests to send
            def loadFileName = String.format(config.perf.flight.load.outputFileNameFormat, i+1);
            def loadFile = new File(loadOutputDir, loadFileName);

            // file where standard output will be saved
            def outputFileName = String.format(config.perf.flight.run.outputFileNameFormat, i+1);
            def outputFile = new File(outputDir, outputFileName);

            // execute workload ------------------------------------------------
            VirtualUser.main(
                [
                "-i", loadFile.absolutePath,
                "-o", outputFile.absolutePath,
                "--endpoint", config.perf.flight.run.endpoint as String
                ] as String[]
            );
        }

        // force web service class loading now to and avoid race condition at ClassLoader later
        def service = new org.tripplanner.flight.wsdl.FlightService();
        def port = service.getFlightPort();
        def StubUtil = new step.framework.ws.StubUtil();


        // define number of simultaneous users
        final def USERS;
        if (runConfig.numberUsers)
            USERS = runConfig.numberUsers as Integer;
        else
            USERS = 1;  // default value

        assert USERS >= 1
        assert USERS <= SAMPLES


        // loop samples --------------------------------------------------------
        int processedSamples = 0;
        int runCount = 0;
        while (processedSamples < SAMPLES) {
            processedSamples += USERS;
            runCount++;

            // delete data generated by requests -------------------------------
            DeleteDB.main(
                [
                "-p", config.perf.flight.databasePropertiesFile.absolutePath,
                "--quick", "true"
                ] as String[]
            );

            // start a clean server --------------------------------------------
            Helper.exec(tempSourceCodeDir.absolutePath, "ant start-server!", ["CLASSPATH" : ""]);

            // deploy flight web service ---------------------------------------
            Helper.exec(tempSourceCodeDir.absolutePath, "ant deploy-flight", ["CLASSPATH" : ""]);


            // -----------------------------------------------------------------
            // create a closure for each user
            Closure[] closureArray = new Closure[USERS];
            for (int i=0; i < USERS; i++) {
                closureArray[i] = virtualUserClosure.curry(processedSamples - USERS + i);
            }
            // execute closures in parallel
            Helper.parallelExecute(USERS, closureArray);
            // -----------------------------------------------------------------

            // stop server -----------------------------------------------------
            Helper.exec(tempSourceCodeDir.absolutePath, "ant stop-server", ["CLASSPATH" : ""])

            // retrieve CATALINA_HOME path from environment variables
            def env = System.getenv();
            def catalinaHomePath = env["CATALINA_HOME"];
            assert catalinaHomePath
            def catalinaHomeDir = new File(catalinaHomePath);
            def catalinaLogsDir = new File(catalinaHomeDir, "logs");
            def catalinaTempDir = new File(catalinaHomeDir, "temp");

            // save functional log data ----------------------------------------
            def sourceLogFileName = config.perf.flight.run.logFileName;
            def sourceLogFile = new File(catalinaLogsDir, sourceLogFileName);
            assert sourceLogFile.exists()

            def targetLogSizeFileName = String.format(config.perf.flight.run.outputLogSizeFileNameFormat, runCount);
            def targetLogSizeFile = new File(outputDir, targetLogSizeFileName);

            // save size in file
            def targetLogSizeFileWriter = new FileWriter(targetLogSizeFile);
            targetLogSizeFileWriter.write(sourceLogFile.length() as String);
            targetLogSizeFileWriter.close();

            // save log contents
            if (runConfig.saveLog) {
                def targetLogFileName = String.format(config.perf.flight.run.outputLogFileNameFormat, runCount);
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

                    def outputPerf4JLogFileName = String.format(config.perf.flight.run.outputPerf4JLogFileNameFormat, runCount);
                    def outputPerf4JLogFile = new File(outputDir, outputPerf4JLogFileName);

                    if (runConfig.perf4j.aggregateContiguousEntries) {
                        println("Aggregate performance log");
                        Perf4JAggregateContiguousEntries.main(
                            [
                            "-i", perf4JLogFile.absolutePath,
                            "-o", outputPerf4JLogFile.absolutePath
                            ] as String[]
                        );
                    } else {
                        println("Copy performance log");
                        ant.move(file: perf4JLogFile.absolutePath, tofile: outputPerf4JLogFile.absolutePath)
                    }
                    break;
                case "eventmon":
                    def eventMonLogDir = catalinaTempDir;
                    def eventMonLogFileNamePattern = ~config.perf.flight.run.eventMonLogFileNameRegex;

                    def outputEventMonLogFileName = String.format(config.perf.flight.run.outputEventMonLogFileNameFormat, runCount);
                    def outputEventMonLogFile = new File(outputDir, outputEventMonLogFileName);

                    println("Merge thread files into single log");
                    eventMonLogDir.eachFileMatch(eventMonLogFileNamePattern) { eventMonFile ->
                        MonAppendLog.main(
                            [
                            "-i", eventMonFile.absolutePath,
                            "-o", outputEventMonLogFile.absolutePath,
                            "-regex", config.perf.flight.run.eventMonLogFileNameRegex as String
                            ] as String[]
                        );
                    }
                    break;
                case "layermon":
                    def layerMonLogDir = catalinaTempDir;
                    def layerMonLogFileNamePattern = ~config.perf.flight.run.layerMonLogFileNameRegex;

                    def outputLayerMonLogFileName = String.format(config.perf.flight.run.outputLayerMonLogFileNameFormat, runCount);
                    def outputLayerMonLogFile = new File(outputDir, outputLayerMonLogFileName);

                    println("Merge thread files into single log");
                    layerMonLogDir.eachFileMatch(layerMonLogFileNamePattern) { layerMonFile ->
                        MonAppendLog.main(
                            [
                            "-i", layerMonFile.absolutePath,
                            "-o", outputLayerMonLogFile.absolutePath,
                            "-regex", config.perf.flight.run.layerMonLogFileNameRegex as String
                            ] as String[]
                        );
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
