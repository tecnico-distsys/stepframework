@ECHO OFF
REM Initialization procedure.
REM Define environment variables used by other batch files.

SET LOAD_DIR=build\load
SET LOG_DIR=build\log
SET PLOT_DIR=build\plot

IF NOT "%1"=="" (
    SET LOAD_DIR=%LOAD_DIR%\%1
    SET LOG_DIR=%LOG_DIR%\%1
    SET PLOT_DIR=%PLOT_DIR%\%1
)

ECHO Flight WS Performance Environment set:
ECHO LOAD_DIR=%LOAD_DIR%
ECHO LOG_DIR=%LOG_DIR%
ECHO PLOT_DIR=%PLOT_DIR%
