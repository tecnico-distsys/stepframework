@ECHO OFF
:: Manage environment variables.
::
:: Author: Miguel Pardal
:: Date:   2010-05-26

:: -----------------------------------------------------------------------------
:begin

:: -----------------------------------------------------------------------------
:check

IF "%1"=="" GOTO print
IF "%1"=="print" GOTO print

IF "%1"=="set" GOTO set

IF "%1"=="clear" GOTO clear

GOTO error_action

:error_action
ECHO Error: argument action %1 not recognized!
GOTO usage

:usage
ECHO Usage: %0 print/clear/set
GOTO end

:: -----------------------------------------------------------------------------
:main

:print
ECHO Performance environment variables:
ECHO.
ECHO STEP_SRC_HOME=%STEP_SRC_HOME%
ECHO CLASSPATH=%CLASSPATH%
ECHO.

GOTO end

:set

:: SET STEP_SRC_HOME to absolute path
CALL env-aux.bat ..\..

SET CLASSPATH=%CLASSPATH%;%STEP_HOME%\lib\SuperCSV-1.52.jar;%STEP_HOME%\lib\commons-math-2.1.jar
SET CLASSPATH=%CLASSPATH%;%STEP_SRC_HOME%\src\framework\dist\stepframework.jar;%STEP_SRC_HOME%\src\flight-ws-cli\dist\flight-ws-cli.jar
SET CLASSPATH=%CLASSPATH%;%STEP_SRC_HOME%\src\flight-perf\src

GOTO end

:clear

SET CLASSPATH=

GOTO end


:: -----------------------------------------------------------------------------
:end
