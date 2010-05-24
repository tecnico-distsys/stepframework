@ECHO OFF
REM Analyze single test run
REM

:begin
SETLOCAL
SET CLASSPATH=%CLASSPATH%;%STEP_HOME%\lib\SuperCSV-1.52.jar;%STEP_HOME%\lib\commons-math-2.1.jar

:check
IF "%PERF_LOAD_DIR%"=="" GOTO error_loaddir

IF "%PERF_LOG_DIR%"=="" GOTO error_logdir

IF "%1%"=="" GOTO error_arg1
SET NR=%1

GOTO main

:error_arg1
ECHO Error: please provide test run number!
GOTO usage

:error_loaddir
ECHO Error: environment variable PERF_LOAD_DIR is not set!
GOTO end

:error_logdir
ECHO Error: environment variable PERF_LOG_DIR is not set!
GOTO end

:usage
ECHO Usage: %0 test-run-number
GOTO end

:main
ECHO Analyzing test run %NR%
ECHO.

REM ----------------------------------------------------------------------------

PUSHD src

CALL groovy Perf4JRequestRecords.groovy             -i ..\%PERF_LOG_DIR%\flight-ws_perfLog-%NR%.txt  -o ..\%PERF_LOG_DIR%\perfLog-requests-%NR%.csv
CALL groovy CSVRequestStatistics.groovy             -i ..\%PERF_LOG_DIR%\perfLog-requests-%NR%.csv   -o ..\%PERF_LOG_DIR%\perfLog-statistics-%NR%.csv

POPD

REM ----------------------------------------------------------------------------

ECHO Test run %NR% analyzed.
:end
