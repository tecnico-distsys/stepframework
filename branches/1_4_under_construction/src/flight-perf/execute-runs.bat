@ECHO OFF
REM Execute multiple test runs
REM

:begin
SETLOCAL

:check_args
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

PUSHD ..
REM Rebuild everything to assure that application is up-to-date at start of runs
REM (WSDL location embedded in code depends on the compilation files location)
CALL ant rebuild
POPD

FOR /L %%? IN (1, 1, %RUNS%) DO CALL execute-run.bat %%?

:end
