@ECHO OFF
:: Execute single test run
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

IF "%1"=="" GOTO error_nr
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

ECHO Starting test run %NR%
ECHO.

CALL init-db.bat

PUSHD ..
CALL ant start-server!
CALL ant deploy-flight
POPD

IF NOT EXIST %PERF_LOG_DIR% MKDIR %PERF_LOG_DIR%

PUSHD src
CALL groovy VirtualUser.groovy -i ..\%PERF_LOAD_DIR%\load-%NR%.obj -o ..\%PERF_LOG_DIR%\load-%NR%.out
POPD

PUSHD ..
CALL ant stop-server
POPD

PUSHD src
CALL groovy Perf4JAggregateContiguousEntries.groovy -i %CATALINA_HOME%\logs\flight-ws_perfLog.txt -o ..\%PERF_LOG_DIR%\flight-ws_perfLog-%NR%.txt
REM COPY %CATALINA_HOME%\logs\flight-ws_perfLog.txt ..\%PERF_LOG_DIR%\flight-ws_perfLog-%NR%.txt
POPD

COPY %CATALINA_HOME%\logs\flight-ws_log.txt %PERF_LOG_DIR%\flight-ws_log-%NR%.txt

ECHO Test run %NR% complete.

:: -----------------------------------------------------------------------------
:end
