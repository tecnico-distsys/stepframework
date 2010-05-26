@ECHO OFF
:: Execute multiple test runs
::
:: Author: Miguel Pardal
:: Date:   2010-05-26

:: -----------------------------------------------------------------------------
:begin
SETLOCAL

:: -----------------------------------------------------------------------------
:check

IF "%1"=="" GOTO error_runs
SET RUNS=%1

GOTO main

:error_runs
ECHO Error: please provide total number of runs!
GOTO usage

:usage
ECHO Usage: %0 total-runs
GOTO end

:: -----------------------------------------------------------------------------
:main

PUSHD ..
CALL ant rebuild
POPD

:: Main loop
FOR /L %%? IN (1, 1, %RUNS%) DO CALL execute-run.bat %%?

:: -----------------------------------------------------------------------------
:end
