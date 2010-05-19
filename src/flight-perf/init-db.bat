@ECHO OFF
REM (Re)create and populate database
REM

SETLOCAL

PUSHD ..
CALL ant hibernatetool-flight
POPD

PUSHD src
CALL groovy DeleteDB.groovy          -p db.properties
CALL groovy LoadFlightManager.groovy -p db.properties
CALL groovy LoadAirports.groovy      -p db.properties -s -1974194697 --file ..\data\airports.csv --maxcost 150000
CALL groovy LoadAirplanes.groovy     -p db.properties -s  -926783273 --file ..\data\fleet-BA.csv --maxcost 100000
CALL groovy LoadFlights.groovy       -p db.properties -s   808451668                             --maxcost   1500 --profit 0.20 --number 5640 --maxgroup 20
REM                                                                                                                             = 235 * 0.80 * 30
REM                                                                                                                             = fleet-size * use-rate * number-of-days
POPD
