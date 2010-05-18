@ECHO OFF
REM (Re)create and populate database
REM

PUSHD ..
CALL ant hibernatetool-flight
POPD

CALL loaddb.bat
