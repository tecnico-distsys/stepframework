@ECHO OFF
:: Generate all sets of test workloads
::
:: Author: Miguel Pardal
:: Date:   2010-05-26

:: -----------------------------------------------------------------------------
:begin
SETLOCAL
SET ECHO=

:: -----------------------------------------------------------------------------
:check
IF "%PERF_LOAD_DIR%"=="" GOTO error_loaddir

GOTO main

:error_loaddir
ECHO Error: environment variable PERF_LOAD_DIR is not set!
GOTO end

:: -----------------------------------------------------------------------------
:main

:: n stands for number, mg stands for maxgroup

SET N=1
SET MG=5
IF NOT EXIST %PERF_LOAD_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG%
    %ECHO% CALL generate-workloads.bat %N% %MG%
)

SET N=50
SET MG=10
IF NOT EXIST %PERF_LOAD_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG%
    %ECHO% CALL generate-workloads.bat %N% %MG%
)

SET N=50
SET MG=100
IF NOT EXIST %PERF_LOAD_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG%
    %ECHO% CALL generate-workloads.bat %N% %MG%
)

SET N=50
SET MG=500
IF NOT EXIST %PERF_LOAD_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG%
    %ECHO% CALL generate-workloads.bat %N% %MG%
)

SET N=50
SET MG=1000
IF NOT EXIST %PERF_LOAD_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG%
    %ECHO% CALL generate-workloads.bat %N% %MG%
)

SET N=100
SET MG=10
IF NOT EXIST %PERF_LOAD_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG%
    %ECHO% CALL generate-workloads.bat %N% %MG%
)

SET N=100
SET MG=100
IF NOT EXIST %PERF_LOAD_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG%
    %ECHO% CALL generate-workloads.bat %N% %MG%
)

SET N=100
SET MG=500
IF NOT EXIST %PERF_LOAD_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG%
    %ECHO% CALL generate-workloads.bat %N% %MG%
)

SET N=100
SET MG=1000
IF NOT EXIST %PERF_LOAD_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG%
    %ECHO% CALL generate-workloads.bat %N% %MG%
)

:: -----------------------------------------------------------------------------
:end
