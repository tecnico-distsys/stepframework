@ECHO OFF
REM Execute single test run
REM

:begin
SETLOCAL

:check_args
IF "%1%"=="" GOTO error_arg1
GOTO main

:error_arg1
ECHO Error: please provide test run number!
GOTO end

:main
ECHO Starting test run %1
ECHO.

REM ----------------------------------------------------------------------------

CALL resetdb.bat

PUSHD ..
CALL ant start-server!
CALL ant deploy-flight
POPD

SET LOGDIR=build\logs\%DATE%
MKDIR %LOGDIR%

CALL groovy VirtualUser.groovy -i build\load-%1.obj -o %LOGDIR%\load-%1.out

PUSHD ..
CALL ant stop-server
POPD

PUSHD %LOGDIR%
COPY %CATALINA_HOME%\logs\flight-ws_perfLog.txt .\flight-ws_perfLog-%1.txt
POPD

REM ----------------------------------------------------------------------------

ECHO Test run %1 complete.
:end
