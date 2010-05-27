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

IF "%PERF_LOAD_DIR%"=="" GOTO error_loaddir

IF "%PERF_LOG_DIR%"=="" GOTO error_logdir

IF "%1"=="" GOTO error_runs
SET RUNS=%1

GOTO main

:error_loaddir
ECHO Error: environment variable PERF_LOAD_DIR is not set!
GOTO end

:error_logdir
ECHO Error: environment variable PERF_LOG_DIR is not set!
GOTO end

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
