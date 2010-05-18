@ECHO OFF
REM Execute multiple test runs
REM

:begin
SETLOCAL

:check_args
SET RUNS=31
IF NOT "%1"=="" SET RUNS=%1
GOTO main

:main
FOR /L %%? IN (1, 1, %RUNS%) DO CALL run.bat %%?

:end
