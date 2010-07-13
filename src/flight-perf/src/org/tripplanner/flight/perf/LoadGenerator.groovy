package org.tripplanner.flight.perf;

import step.groovy.Helper;
import org.tripplanner.flight.perf.load_generator.*;


/**
 *  Generate test workloads
 */

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "LoadGenerator")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli.cfg(longOpt: "config", required: false, args: 1, "Specify master configuration file")
cli._(longOpt: "force", required: false, args: 0, "Force load generation")
cli._(longOpt: "nodomaingen", required: false, args: 0, "Prevent domain data generation")

def options = cli.parse(args)
assert (options)

if (options.help) {
    cli.usage();
    return;
}

// load initial configuration --------------------------------------------------
def configPath = "etc/config/Config.groovy";
if (options.cfg) configPath = options.cfg;

final def config = Helper.parseConfig(configPath);
assert (config.perf.flight) : "Expecting flight performance configuration file"
Helper.configStringToFile(config);

// main ------------------------------------------------------------------------
println "**********************"
println "*** LOAD GENERATOR ***"
println "**********************"

final def outputBaseDir = config.perf.flight.load.outputBaseDir;

def generateDomainData = true;
if (options.nodomaingen) generateDomainData = false;

// iterate all load instances
final def instanceDir = config.perf.flight.load.instanceDir;
final def instanceFileNamePattern = ~config.perf.flight.load.instanceFileNameRegex;
println("Looking for load instances in " + instanceDir.canonicalPath);
instanceDir.eachFileMatch(instanceFileNamePattern) { file ->

    def loadConfig = Helper.parseConfig(file.path);
    loadConfig = loadConfig.perf.flight.load.instance;
    assert loadConfig : "Expecting flight load instance configuration file"

    final def loadId = loadConfig.id;

    def outputDir = new File(outputBaseDir, loadId);

    // skip generation if possible
    if (!options.force && outputDir.exists()) {
        println("Skipping load '" + loadId + "' defined by file " + file.name);
        return;
    }

    // generate load samples ---------------------------------------------------
    if (!outputDir.exists()) outputDir.mkdir();
    assert outputDir.exists() && outputDir.isDirectory()

    println("Processing load '" + loadId + "' defined by file " + file.name);
    println("Output directory: " + outputDir.canonicalPath);

    // regenerate domain data
    if (generateDomainData) {
        DomainDataGenerator.main(
            [
            "-cfg", configPath
            ] as String[]
        );
        generateDomainData = false;
    }

    final def samples = loadConfig.numberSamples;
    assert samples >= 1

    final def seedList = loadConfig.randomSeedList;
    assert seedList instanceof java.util.List
    assert seedList.size() >= samples

    // define closure to process sample i
    def genLoadClosure = { i ->
        def outputFileName = String.format(config.perf.flight.load.outputFileNameFormat, i+1);
        def outputFile = new File(outputDir, outputFileName);

        println("Generating sample " + (i+1) + " to " + outputFile.getCanonicalPath());

        // generate workload
        WorkloadGenerator.main(
            [
            "-s", seedList[i] as String,
            "-n", loadConfig.numberSessions as String,
            "-o", outputFile as String,
            "--maxthinktime", loadConfig.maxThinkTime as String,
            "--maxgroup", loadConfig.maxGroup as String,
            "--errorprobability", loadConfig.errorProbability as String,
            "-p", config.perf.flight.databasePropertiesFile.absolutePath,
            "--names", config.perf.flight.domain.namesFile.absolutePath,
            "--surnames", config.perf.flight.domain.surnamesFile.absolutePath
            ] as String[]
        );
    }

    // create one closure for each sample to process
    // closures implement Runnable and as such can be used by java.util.concurrent.Executors
    def genLoadClosureArray = Helper.indexCurryClosureArray(genLoadClosure, samples);
    // execute closures in parallel
    Helper.multicoreExecute(genLoadClosureArray);

}
