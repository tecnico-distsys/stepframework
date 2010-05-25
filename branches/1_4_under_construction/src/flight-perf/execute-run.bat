@ECHO OFF
REM Execute single test run
REM

:begin
SETLOCAL

:check
IF "%PERF_LOAD_DIR%"=="" GOTO error_loaddir

IF "%PERF_LOG_DIR%"=="" GOTO error_logdir

IF "%1"=="" GOTO error_arg1
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
ECHO Starting test run %NR%
ECHO.

REM ----------------------------------------------------------------------------

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
POPD

REM ----------------------------------------------------------------------------

ECHO Test run %NR% complete.
:end
