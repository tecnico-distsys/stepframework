@ECHO OFF
REM Execute single test run
REM

:begin
SETLOCAL
SET CLASSPATH=%CLASSPATH%;..\..\framework\dist\stepframework.jar;..\..\flight-ws-cli\dist\flight-ws-cli.jar

:check
IF "%LOAD_DIR%"=="" GOTO error_loaddir

IF "%LOG_DIR%"=="" GOTO error_logdir

IF "%1"=="" GOTO error_arg1
SET NR=%1

GOTO main

:error_arg1
ECHO Error: please provide test run number!
GOTO usage

:error_loaddir
ECHO Error: environment variable LOAD_DIR is not set!
GOTO end

:error_logdir
ECHO Error: environment variable LOG_DIR is not set!
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

IF NOT EXIST build MKDIR build
IF NOT EXIST %LOG_DIR% MKDIR %LOG_DIR%

PUSHD src
CALL groovy VirtualUser.groovy -i ..\%LOAD_DIR%\load-%NR%.obj -o ..\%LOG_DIR%\load-%NR%.out
POPD

PUSHD ..
CALL ant stop-server
POPD

PUSHD %LOG_DIR%
COPY %CATALINA_HOME%\logs\flight-ws_perfLog.txt .\flight-ws_perfLog-%NR%.txt
POPD

REM ----------------------------------------------------------------------------

ECHO Test run %NR% complete.
:end
