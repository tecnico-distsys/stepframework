package org.tripplanner.flight.perf;

import step.groovy.Helper;
import org.tripplanner.flight.perf.domain_data_generator.*;


/**
 *  (Re)create and populate database
 */

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "DomainDataGenerator")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli._(longOpt: "noschemagen", required: false, args: 0, "Prevent database schema generation")

def options = cli.parse(args)
assert (options)

if (options.help) {
    cli.usage();
    return;
}

// load configuration ----------------------------------------------------------
def config = Helper.parseConfig("etc/config/Config.groovy");
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

def argv;

// delete data
argv = ["-p", config.perf.flight.databasePropertiesFile.absolutePath]
DeleteDB.main(argv as String[])

// load data
argv = ["-p", config.perf.flight.databasePropertiesFile.absolutePath]
LoadFlightManager.main(argv as String[])

argv = ["-p", config.perf.flight.databasePropertiesFile.absolutePath,
        "-s", config.perf.flight.domain.airports.randomSeed as String,
        "--file", config.perf.flight.domain.airports.file.absolutePath,
        "--maxcost", config.perf.flight.domain.airports.maxCost as String]
LoadAirports.main(argv as String[])

argv = ["-p", config.perf.flight.databasePropertiesFile.absolutePath,
        "-s", config.perf.flight.domain.airplanes.randomSeed as String,
        "--file", config.perf.flight.domain.airplanes.file.absolutePath,
        "--maxcost", config.perf.flight.domain.airplanes.maxCost as String]
LoadAirplanes.main(argv as String[])

argv = ["-p", config.perf.flight.databasePropertiesFile.absolutePath,
        "-s", config.perf.flight.domain.flights.randomSeed as String,
        "--maxcost", config.perf.flight.domain.flights.maxCost as String,
        "--profit", config.perf.flight.domain.flights.profit as String,
        "--number", config.perf.flight.domain.flights.number as String,
        "--maxgroup", config.perf.flight.domain.flights.maxGroup as String]
LoadFlights.main(argv as String[])

// -----------------------------------------------------------------------------
