@ECHO OFF
REM Analyze single test run
REM

:begin
SETLOCAL
SET CLASSPATH=%CLASSPATH%;%STEP_HOME%\lib\SuperCSV-1.52.jar;%STEP_HOME%\lib\commons-math-2.1.jar

:check_args
IF "%1%"=="" GOTO error_arg1
SET NR=%1

GOTO main

:error_arg1
ECHO Error: please provide test run number!
GOTO usage

:usage
ECHO Usage: %0 test-run-number
GOTO end

:main
ECHO Analyzing test run %NR%
ECHO.

REM ----------------------------------------------------------------------------

PUSHD src

IF NOT EXIST ..\build\logs\perfLog-aggregate-%NR%.txt (
    CALL groovy Perf4JAggregateContiguousEntries.groovy -i ..\build\logs\flight-ws_perfLog-%NR%.txt  -o ..\build\logs\perfLog-aggregate-%NR%.txt
)

CALL groovy Perf4JRequestRecords.groovy             -i ..\build\logs\perfLog-aggregate-%NR%.txt  -o ..\build\logs\perfLog-requests-%NR%.csv
CALL groovy CSVRequestStatistics.groovy             -i ..\build\logs\perfLog-requests-%NR%.csv   -o ..\build\logs\perfLog-statistics-%NR%.csv

POPD

REM ----------------------------------------------------------------------------

ECHO Test run %NR% analyzed.
:end
