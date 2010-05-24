@ECHO OFF
REM Execute multiple test runs
REM

:begin
SETLOCAL

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

:main

IF "%PERF_COMPILE_FIRST_TIME%"=="false" GOTO compile_first_time_done
PUSHD ..
REM Rebuild everything to assure that application is up-to-date at start of runs
REM (WSDL location embedded in code depends on the compilation files location)
CALL ant rebuild
POPD
:compile_first_time_done

REM Main loop
FOR /L %%? IN (1, 1, %RUNS%) DO CALL execute-run.bat %%?

:end
