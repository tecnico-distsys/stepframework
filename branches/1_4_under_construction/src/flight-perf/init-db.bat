@ECHO OFF
:: (Re)create and populate database
::
:: Author: Miguel Pardal
:: Date:   2010-05-26

:: -----------------------------------------------------------------------------
:begin
SETLOCAL

:: -----------------------------------------------------------------------------
:main

PUSHD ..
CALL ant hibernatetool-flight
POPD

PUSHD src
CALL groovy DeleteDB.groovy          -p db.properties
CALL groovy LoadFlightManager.groovy -p db.properties
CALL groovy LoadAirports.groovy      -p db.properties -s    -8354 --file ..\data\airports.csv --maxcost 150000
CALL groovy LoadAirplanes.groovy     -p db.properties -s   926783 --file ..\data\fleet-BA.csv --maxcost 100000
CALL groovy LoadFlights.groovy       -p db.properties -s  8845868                             --maxcost   1500 --profit 0.20 --number 5640 --maxgroup 100
POPD
::                                                                                                                             = 235 * 0.80 * 30
::                                                                                                                             = fleet-size * use-rate * number-of-days

:: -----------------------------------------------------------------------------
:end
