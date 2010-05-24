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
CALL groovy LoadAirports.groovy      -p db.properties -s    -8354 --file ..\data\airports.csv --maxcost 150000
CALL groovy LoadAirplanes.groovy     -p db.properties -s   926783 --file ..\data\fleet-BA.csv --maxcost 100000
CALL groovy LoadFlights.groovy       -p db.properties -s  8845868                             --maxcost   1500 --profit 0.20 --number 5640 --maxgroup 100
REM                                                                                                                             = 235 * 0.80 * 30
REM                                                                                                                             = fleet-size * use-rate * number-of-days
POPD
