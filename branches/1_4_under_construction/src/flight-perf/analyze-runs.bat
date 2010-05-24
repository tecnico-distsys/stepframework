@ECHO OFF
REM Analyze multiple test runs
REM

:begin
SETLOCAL
SET CLASSPATH=%CLASSPATH%;%STEP_HOME%\lib\SuperCSV-1.52.jar;%STEP_HOME%\lib\commons-math-2.1.jar

:check
IF "%1"=="" GOTO error_arg1
SET RUNS=%1

GOTO main

:error_arg1
ECHO Error: please provide total number of runs!
GOTO usage

:usage
ECHO Usage: %0 total-runs
GOTO end

:main
FOR /L %%? IN (1, 1, %RUNS%) DO CALL analyze-run.bat %%?

:end
