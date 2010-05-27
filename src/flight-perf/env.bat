@ECHO OFF
:: Manage environment variables required by other batch files.
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
ECHO Usage: %0 print/clear/set (load) (log) (plot)
GOTO end

:: -----------------------------------------------------------------------------
:main

:print
ECHO Performance environment variables:
ECHO.
ECHO CLASSPATH=%CLASSPATH%
ECHO.
ECHO PERF_LOAD_DIR=%PERF_LOAD_DIR%
ECHO PERF_LOG_DIR=%PERF_LOG_DIR%
ECHO PERF_PLOT_DIR=%PERF_PLOT_DIR%
ECHO.

GOTO end

:set

SET CLASSPATH=%CLASSPATH%;..\..\framework\dist\stepframework.jar;..\..\flight-ws-cli\dist\flight-ws-cli.jar;%STEP_HOME%\lib\SuperCSV-1.52.jar;%STEP_HOME%\lib\commons-math-2.1.jar

SET PERF_LOAD_DIR=build\load
SET PERF_LOG_DIR=build\log
SET PERF_PLOT_DIR=build\plot

IF NOT "%2"=="" (
    SET PERF_LOAD_DIR=%PERF_LOAD_DIR%\%2
)

IF NOT "%3"=="" (
    SET PERF_LOG_DIR=%PERF_LOG_DIR%\%3
)

IF NOT "%4"=="" (
    SET PERF_PLOT_DIR=%PERF_PLOT_DIR%\%4
)

GOTO print

:clear

SET CLASSPATH=

SET PERF_LOAD_DIR=
SET PERF_LOG_DIR=
SET PERF_PLOT_DIR=

GOTO end


:: -----------------------------------------------------------------------------
:end
