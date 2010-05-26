@ECHO OFF
:: Analyze single test run
::
:: Author: Miguel Pardal
:: Date:   2010-05-26

:: -----------------------------------------------------------------------------
:begin
SETLOCAL

:: -----------------------------------------------------------------------------
:check

IF "%PERF_LOAD_DIR%"=="" GOTO error_loaddir

IF "%PERF_LOG_DIR%"=="" GOTO error_logdir

IF "%1%"=="" GOTO error_nr
SET NR=%1

GOTO main

:error_loaddir
ECHO Error: environment variable PERF_LOAD_DIR is not set!
GOTO end

:error_logdir
ECHO Error: environment variable PERF_LOG_DIR is not set!
GOTO end

:error_nr
ECHO Error: please provide test run number!
GOTO usage

:usage
ECHO Usage: %0 test-run-number
GOTO end

:: -----------------------------------------------------------------------------
:main

ECHO Analyzing test run %NR%
ECHO.

PUSHD src
CALL groovy Perf4JRequestRecords.groovy -i ..\%PERF_LOG_DIR%\flight-ws_perfLog-%NR%.txt  -o ..\%PERF_LOG_DIR%\perfLog-requests-%NR%.csv
CALL groovy CSVRequestStatistics.groovy -i ..\%PERF_LOG_DIR%\perfLog-requests-%NR%.csv   -o ..\%PERF_LOG_DIR%\perfLog-statistics-%NR%.csv
POPD

ECHO Test run %NR% analyzed.

:: -----------------------------------------------------------------------------
:end
