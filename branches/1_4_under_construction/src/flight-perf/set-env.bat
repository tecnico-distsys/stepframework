@ECHO OFF
REM Initialization procedure.
REM Define environment variables used by other batch files.

SET CLASSPATH=%CLASSPATH%;..\..\framework\dist\stepframework.jar;..\..\flight-ws-cli\dist\flight-ws-cli.jar;%STEP_HOME%\lib\SuperCSV-1.52.jar;%STEP_HOME%\lib\commons-math-2.1.jar

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

ECHO Flight WS Performance Environment:
ECHO.
ECHO Locations:
ECHO PERF_LOAD_DIR=%PERF_LOAD_DIR%
ECHO PERF_LOG_DIR=%PERF_LOG_DIR%
ECHO PERF_PLOT_DIR=%PERF_PLOT_DIR%
ECHO.
