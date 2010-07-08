package org.tripplanner.flight.perf;

import java.util.concurrent.*;

import step.groovy.Helper;
import org.tripplanner.flight.perf.load_generator.*;


/**
 *  Generate test workloads
 *
 *  @author Miguel Pardal
 */

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "LoadGenerator")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli._(longOpt: "force", required: false, args: 0, "Force load generation")
cli._(longOpt: "nodomaingen", required: false, args: 0, "Prevent domain data generation")
cli._(longOpt: "poolsize", required: false, args: 1, "Thread pool size for parallel tasks")

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

def instanceDir = config.perf.flight.load.instanceDir;
def instanceFileNamePattern = ~config.perf.flight.load.instanceFileNameRegex;

def outputBaseDir = config.perf.flight.load.outputBaseDir;

// -----------------------------------------------------------------------------

// define thread pool size
final def poolSize;
if (options.poolsize)
    poolSize = options.poolsize as Integer;
else
    poolSize = Runtime.getRuntime().availableProcessors();
assert poolSize > 0

def generateDomainData = true;
if (options.nodomaingen) generateDomainData = false;

// iterate all load instances
instanceDir.eachFileMatch(instanceFileNamePattern) { file ->

    def loadConfig = Helper.parseConfig(file.path);
    loadConfig = loadConfig.perf.flight.load.instance;
    assert (loadConfig) : "Expecting flight load instance configuration file"

    def loadId = loadConfig.id;
    assert(loadId ==~ "[A-Za-z0-9]+") : "Invalid load identifier"

    def outputDir = new File(outputBaseDir, loadId);

    // check if generation is necessary
    if (options.force ||
        !outputDir.exists()) {

        if (!outputDir.exists())
            outputDir.mkdir();
        assert (outputDir.exists() && outputDir.isDirectory())

        println("Processing load instance defined by " + file.name);
        println("Output directory: " + outputDir.canonicalPath);

        // regenerate domain data
        if (generateDomainData) {
            def argv = [  ];
            DomainDataGenerator.main(argv as String[]);
            generateDomainData = false;
        }

        final def SAMPLES = loadConfig.numberSamples;
        assert (SAMPLES >= 1)

        final def SEED_LIST = loadConfig.randomSeedList;
        assert (SEED_LIST instanceof java.util.List)
        assert (SEED_LIST.size() >= SAMPLES) : "Not enough seeds for desired number of samples"

        // closures implement Runnable and as such can be used by java.util.concurrent.Executors
        def genLoadClosure = { i ->
            def outputFileName = String.format(config.perf.flight.load.outputFileNameFormat, i+1);
            def outputFile = new File(outputDir, outputFileName);

            println("Generating sample " + (i+1) + " to " + outputFile.getCanonicalPath());

            // generate workload
            def argv = [
                "-s", SEED_LIST[i] as String,
                "-n", loadConfig.numberSessions as String,
                "-o", outputFile as String,
                "--maxthinktime", loadConfig.maxThinkTime as String,
                "--maxgroup", loadConfig.maxGroup as String,
                "--errorprobability", loadConfig.errorProbability as String,
                "-p", config.perf.flight.databasePropertiesFile.absolutePath,
                "--names", config.perf.flight.domain.namesFile.absolutePath,
                "--surnames", config.perf.flight.domain.surnamesFile.absolutePath
                        ]
            WorkloadGenerator.main(argv as String[]);
        }

        // create thread pool
        def executorService = Executors.newFixedThreadPool(poolSize);
        def futureList = [ ]
        for (int i=0; i < SAMPLES; i++) {
            // submit task
            futureList.add(executorService.submit(genLoadClosure.curry(i)));
                    // curry() creates a new closure with resolved parameters
        }
        assert futureList.size() == SAMPLES
        // close service
        executorService.shutdown();
        // await future results
        def exceptionMap = [ : ]
        for (int i=0; i < SAMPLES; i++) {
            try {
                futureList.get(i).get();
            } catch(ExecutionException ee) {
                // collect exception
                exceptionMap.put(i, ee.getCause());
            }
        }
        // assert no exceptions occurred before proceeding
        assert exceptionMap.isEmpty()

    } else {
        println("Skipping " + file.name);
    }

}

// -----------------------------------------------------------------------------
