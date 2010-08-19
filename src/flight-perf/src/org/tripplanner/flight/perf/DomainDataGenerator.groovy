package org.tripplanner.flight.perf;

import step.groovy.Helper;
import org.tripplanner.flight.perf.domain_data_generator.*;


/**
 *  (Re)create and populate database
 */

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "DomainDataGenerator")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli.cfg(longOpt: "config", required: false, args: 1, "Specify master configuration file")
cli._(longOpt: "noschemagen", required: false, args: 0, "Prevent database schema generation")

def options = cli.parse(args)
assert (options)

if (options.help) {
    cli.usage();
    return;
}

// load configuration ----------------------------------------------------------
def configPath = "etc/config/Config.groovy";
if (options.cfg) configPath = options.cfg;

def config = Helper.parseConfig(configPath);
assert (config.perf.flight) : "Expecting flight performance configuration file"
Helper.configStringToFile(config);

// -----------------------------------------------------------------------------

def generateSchema = true;
if (options.noschemagen) generateSchema = false;


if (generateSchema) {
    def flightCodeDir = new File("../flight-ws");
    assert flightCodeDir.exists() && flightCodeDir.isDirectory()

    // (re)create tables
    Helper.exec(flightCodeDir.absolutePath,
                "ant -e hibernatetool",
                ["CLASSPATH" : ""]) // classpath environment variable
                                    // is cleared to avoid conflicts
}

// delete data
DeleteDB.main(
    ["-p", config.perf.flight.databasePropertiesFile.absolutePath]
    as String[]
)

// load data
LoadFlightManager.main(
    ["-p", config.perf.flight.databasePropertiesFile.absolutePath]
    as String[]
)

LoadAirports.main(
    ["-p", config.perf.flight.databasePropertiesFile.absolutePath,
     "-s", config.perf.flight.domain.airports.randomSeed as String,
     "--file", config.perf.flight.domain.airports.file.absolutePath,
     "--maxcost", config.perf.flight.domain.airports.maxCost as String]
    as String[]
)

LoadAirplanes.main(
    ["-p", config.perf.flight.databasePropertiesFile.absolutePath,
     "-s", config.perf.flight.domain.airplanes.randomSeed as String,
     "--file", config.perf.flight.domain.airplanes.file.absolutePath,
     "--maxcost", config.perf.flight.domain.airplanes.maxCost as String]
    as String[]
)

LoadFlights.main(
    ["-p", config.perf.flight.databasePropertiesFile.absolutePath,
     "-s", config.perf.flight.domain.flights.randomSeed as String,
     "--maxcost", config.perf.flight.domain.flights.maxCost as String,
     "--profit", config.perf.flight.domain.flights.profit as String,
     "--number", config.perf.flight.domain.flights.number as String,
     "--maxgroup", config.perf.flight.domain.flights.maxGroup as String]
    as String[]
)

// -----------------------------------------------------------------------------
