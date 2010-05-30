/**
 *  Generate test workloads
 *
 *  @author Miguel Pardal
 */

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "LoadGenerator")
cli._(longOpt: "force", required: false, args: 0, "Force load generation")
cli._(longOpt: "nodomaingen", required: false, args: 0, "Prevent domain data generation")

def options = cli.parse(args)
assert (options)

// load initial configuration --------------------------------------------------
def config = ConfigHelper.parseMany("Shared.config.groovy", "Domain.config.groovy");

assert (config.flight.shared) : "Expecting flight shared configuration file"
assert (config.flight.domain) : "Expecting flight domain configuration file"

def loadDir = new File(config.flight.shared.dir.load);
assert (loadDir.exists() && loadDir.isDirectory())

def loadFilePattern = ~config.flight.shared.pattern.load;
assert (loadFilePattern)

// -----------------------------------------------------------------------------

def argv;
def generateDomainData = true;

if (options.nodomaingen) generateDomainData = false;

// generate workloads for each modified config file
loadDir.eachFileMatch(loadFilePattern) { file ->
    def loadConfig = ConfigHelper.parseMany(file.path);
    assert (loadConfig.flight.load) : "Expecting flight load configuration file"

    def sourceFile = file;
    def targetDir = new File(loadDir, loadConfig.flight.load.id);


    // check if generation is necessary
    if (options.force ||
        !targetDir.exists() ||
        targetDir.listFiles().size() == 0) {

        if (!targetDir.exists())
            targetDir.mkdir();
        assert (targetDir.exists() && targetDir.isDirectory())

        println("Processing " + file.name);

        // regenerate domain data
        if (generateDomainData) {
            argv = [ ];
            DomainDataGenerator.main(argv as String[]);
            generateDomainData = false;
        }

        final def SAMPLES = loadConfig.flight.load.numberSamples;
        assert (SAMPLES >= 1)

        final def SEED_LIST = loadConfig.flight.load.randomSeedList;
        assert (SEED_LIST instanceof java.util.List)
        assert (SEED_LIST.size() >= SAMPLES) : "Not enough seeds for desired number of samples"

        for (int i=0; i < SAMPLES; i++) {
            def outputFileName = String.format(config.flight.shared.format.load, i+1);
            def outputFile = new File(new File(config.flight.shared.dir.load, loadConfig.flight.load.id), outputFileName);

            println("Generating sample " + (i+1) + " to " + outputFile.getCanonicalPath());

            // generate workload
            argv = ["-s", SEED_LIST[i] as String,
                    "-n", loadConfig.flight.load.numberSessions as String,
                    "-o", outputFile as String,
                    "--maxthinktime", loadConfig.flight.load.maxThinkTime as String,
                    "--maxgroup", loadConfig.flight.load.maxGroup as String,
                    "--errorprobability", loadConfig.flight.load.errorProbability as String,
                    "-p", config.flight.domain.databasePropertiesFilePath,
                    "--names", new File(config.flight.shared.dir.domain, config.flight.domain.namesFileName) as String,
                    "--surnames", new File(config.flight.shared.dir.domain, config.flight.domain.surnamesFileName) as String]
            WorkloadGenerator.main(argv as String[]);
        }

    } else {
        println("Skipping " + sourceFile.name);
    }

}

// -----------------------------------------------------------------------------
