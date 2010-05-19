@ECHO OFF
REM Execute single test run
REM

:begin
SETLOCAL
SET CLASSPATH=%CLASSPATH%;..\..\framework\dist\stepframework.jar;..\..\flight-ws-cli\dist\flight-ws-cli.jar

:check_args
IF "%1"=="" GOTO error_arg1
SET NR=%1

GOTO main

:error_arg1
ECHO Error: please provide test run number!
GOTO usage

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
IF NOT EXIST build\logs MKDIR build\logs

PUSHD src
CALL groovy VirtualUser.groovy -i ..\build\loads\load-%NR%.obj -o ..\build\logs\load-%NR%.out
POPD

PUSHD ..
CALL ant stop-server
POPD

PUSHD build\logs
COPY %CATALINA_HOME%\logs\flight-ws_perfLog.txt .\flight-ws_perfLog-%NR%.txt
POPD

REM ----------------------------------------------------------------------------

ECHO Test run %NR% complete.
:end
