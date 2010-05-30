/**
 *  (Re)create and populate database
 *
 *  @author Miguel Pardal
 */

// load configuration ----------------------------------------------------------
def config = ConfigHelper.parseMany("Shared.config.groovy", "Domain.config.groovy");

assert (config.flight.shared) : "Expecting flight shared configuration file"
assert (config.flight.domain) : "Expecting flight domain configuration file"

// -----------------------------------------------------------------------------

// (re)create tables
ShellHelper.exec("../flight-ws", "ant -e hibernatetool")


def argv;

// delete data
argv = ["-p", config.flight.domain.databasePropertiesFilePath]
DeleteDB.main(argv as String[])

// load data
argv = ["-p", config.flight.domain.databasePropertiesFilePath]
LoadFlightManager.main(argv as String[])

argv = ["-p", config.flight.domain.databasePropertiesFilePath,
        "-s", config.flight.domain.airports.randomSeed as String,
        "--file", new File(config.flight.shared.dir.domain, config.flight.domain.airports.fileName) as String,
        "--maxcost", config.flight.domain.airports.maxCost as String]
LoadAirports.main(argv as String[])

argv = ["-p", config.flight.domain.databasePropertiesFilePath,
        "-s", config.flight.domain.airplanes.randomSeed as String,
        "--file", new File(config.flight.shared.dir.domain, config.flight.domain.airplanes.fileName) as String,
        "--maxcost", config.flight.domain.airplanes.maxCost as String]
LoadAirplanes.main(argv as String[])

argv = ["-p", config.flight.domain.databasePropertiesFilePath,
        "-s", config.flight.domain.flights.randomSeed as String,
        "--maxcost", config.flight.domain.flights.maxCost as String,
        "--profit", config.flight.domain.flights.profit as String,
        "--number", config.flight.domain.flights.number as String,
        "--maxgroup", config.flight.domain.flights.maxGroup as String]
LoadFlights.main(argv as String[])

// -----------------------------------------------------------------------------
