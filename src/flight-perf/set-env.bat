@ECHO OFF
:: Define environment variables required by other batch files.
::
:: Author: Miguel Pardal
:: Date:   2010-05-26

:: -----------------------------------------------------------------------------
:main

:: classpath for groovy scripts
SET CLASSPATH=%CLASSPATH%;..\..\framework\dist\stepframework.jar;..\..\flight-ws-cli\dist\flight-ws-cli.jar;%STEP_HOME%\lib\SuperCSV-1.52.jar;%STEP_HOME%\lib\commons-math-2.1.jar

:: locations
SET PERF_LOAD_DIR=build\load
SET PERF_LOG_DIR=build\log
SET PERF_PLOT_DIR=build\plot

IF NOT "%1"=="" (
    SET PERF_LOAD_DIR=%PERF_LOAD_DIR%\%1
)

IF NOT "%2"=="" (
    SET PERF_LOG_DIR=%PERF_LOG_DIR%\%2
)

IF NOT "%3"=="" (
    SET PERF_PLOT_DIR=%PERF_PLOT_DIR%\%3
)

:: Show values
ECHO Performance environment variables:
ECHO.
ECHO CLASSPATH=%CLASSPATH%
ECHO.
ECHO PERF_LOAD_DIR=%PERF_LOAD_DIR%
ECHO PERF_LOG_DIR=%PERF_LOG_DIR%
ECHO PERF_PLOT_DIR=%PERF_PLOT_DIR%
ECHO.

:: -----------------------------------------------------------------------------
:end
