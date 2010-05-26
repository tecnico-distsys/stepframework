@ECHO OFF
:: Execute all sets of test workloads
::
:: Author: Miguel Pardal
:: Date:   2010-05-26

:: -----------------------------------------------------------------------------
:begin
SETLOCAL
SET ECHO=

SET LOG4J_FILE_NAME=log4j.xml
SET LOG4J_FILE=..\flight-ws\src\%LOG4J_FILE_NAME%
SET LOG4J_FILE_BAK=%LOG4J_FILE%.bak

SET CONFIG_DIR=config

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
ECHO Error: argument number-of-runs is missing!
GOTO usage

:usage
ECHO Usage: %0 number-of-runs
GOTO end

:: - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
:replace_log4j
%ECHO% COPY %LOG4J_FILE% %LOG4J_FILE_BAK%
%ECHO% COPY %CONFIG_DIR%\%CONFIG%\%LOG4J_FILE_NAME% %LOG4J_FILE%
:: - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
GOTO :EOF

:: - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
:restore_log4j
%ECHO% DEL %LOG4J_FILE%
%ECHO% COPY %LOG4J_FILE_BAK% %LOG4J_FILE%
%ECHO% DEL %LOG4J_FILE_BAK%
:: - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
GOTO :EOF



:: -----------------------------------------------------------------------------
:main

SET N=1
SET MG=5
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%
    %ECHO% CALL execute-runs.bat %RUNS%
)

SET N=50
SET MG=10
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%
    %ECHO% CALL execute-runs.bat %RUNS%
)

SET N=50
SET MG=100
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%
    %ECHO% CALL execute-runs.bat %RUNS%
)

SET N=50
SET MG=500
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%
    %ECHO% CALL execute-runs.bat %RUNS%
)

SET N=50
SET MG=1000
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%
    %ECHO% CALL execute-runs.bat %RUNS%
)

SET N=100
SET MG=10
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%
    %ECHO% CALL execute-runs.bat %RUNS%
)

SET N=100
SET MG=100
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%
    %ECHO% CALL execute-runs.bat %RUNS%
)

SET N=100
SET MG=500
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%
    %ECHO% CALL execute-runs.bat %RUNS%
)

SET N=100
SET MG=1000
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG% (
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%
    %ECHO% CALL execute-runs.bat %RUNS%
)

:: -----------------------------------------------------------------------------
:: log configuration runs

SET N=50
SET MG=10

SET CONFIG=log-level-off
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG%_%CONFIG% (
    CALL :replace_log4j
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%_%CONFIG%
    %ECHO% CALL execute-runs.bat %RUNS%
    CALL :restore_log4j
)

SET CONFIG=log-level-error
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG%_%CONFIG% (
    CALL :replace_log4j
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%_%CONFIG%
    %ECHO% CALL execute-runs.bat %RUNS%
    CALL :restore_log4j
)

SET CONFIG=log-level-warn
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG%_%CONFIG% (
    CALL :replace_log4j
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%_%CONFIG%
    %ECHO% CALL execute-runs.bat %RUNS%
    CALL :restore_log4j
)

SET CONFIG=log-level-info
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG%_%CONFIG% (
    CALL :replace_log4j
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%_%CONFIG%
    %ECHO% CALL execute-runs.bat %RUNS%
    CALL :restore_log4j
)

SET CONFIG=log-level-debug
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG%_%CONFIG% (
    CALL :replace_log4j
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%_%CONFIG%
    %ECHO% CALL execute-runs.bat %RUNS%
    CALL :restore_log4j
)

SET CONFIG=log-level-trace
IF NOT EXIST %PERF_LOG_DIR%\n%N%mg%MG%_%CONFIG% (
    CALL :replace_log4j
    %ECHO% CALL set-env.bat n%N%mg%MG% n%N%mg%MG%_%CONFIG%
    %ECHO% CALL execute-runs.bat %RUNS%
    CALL :restore_log4j
)


:: -----------------------------------------------------------------------------
:end
