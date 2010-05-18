@ECHO OFF
REM Execute multiple test runs
REM

SETLOCAL

SET RUNS=31
IF NOT "%1"=="" SET RUNS=%1

FOR /L %%? IN (1, 1, %RUNS%) DO CALL run.bat %%?
