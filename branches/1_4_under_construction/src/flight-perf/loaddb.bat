@ECHO OFF
REM Load database generating random data with specified seed
CALL groovy LoadDB.groovy -s 20120313 -p db.properties
