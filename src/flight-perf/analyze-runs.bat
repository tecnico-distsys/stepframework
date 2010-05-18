@ECHO OFF
REM Analyze multiple test runs
REM

:begin
SETLOCAL

:check_args
IF "%1"=="" GOTO error_arg1
SET LOGDIR=%1

SET RUNS=31
IF NOT "%2"=="" SET RUNS=%1

GOTO main

:error_arg1
ECHO Error: please provide files location!
GOTO end

:main
FOR /L %%? IN (1, 1, %RUNS%) DO CALL analyze-run.bat %LOGDIR% %%?

:end
