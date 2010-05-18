@ECHO OFF
REM Execute single test run
REM

:begin
SETLOCAL

:check_args
SET LOGDIR=build\logs\%DATE%
IF NOT "%1"=="" SET LOGDIR=%1

IF "%2"=="" GOTO error_arg2
SET NR=%2

GOTO main

:error_arg2
ECHO Error: please provide test run number!
GOTO end

:main
ECHO Starting test run %NR%
ECHO.

REM ----------------------------------------------------------------------------

CALL resetdb.bat

PUSHD ..
CALL ant start-server!
CALL ant deploy-flight
POPD

MKDIR %LOGDIR%

CALL groovy VirtualUser.groovy -i build\load-%NR%.obj -o %LOGDIR%\load-%NR%.out

PUSHD ..
CALL ant stop-server
POPD

PUSHD %LOGDIR%
COPY %CATALINA_HOME%\logs\flight-ws_perfLog.txt .\flight-ws_perfLog-%NR%.txt
POPD

REM ----------------------------------------------------------------------------

ECHO Test run %NR% complete.
:end
