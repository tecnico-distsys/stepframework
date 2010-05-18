@ECHO OFF
REM Analyze single test run
REM

:begin
SETLOCAL

:check_args
IF "%1"=="" GOTO error_arg1
SET LOGDIR=%1

IF "%2%"=="" GOTO error_arg2
SET NR=%2

GOTO main

:error_arg1
ECHO Error: please provide file location!
GOTO end

:error_arg2
ECHO Error: please provide test run number!
GOTO end

:main
ECHO Analyzing test run %NR%
ECHO.

REM ----------------------------------------------------------------------------

CALL set-env.bat

CALL groovy Perf4JAggregateContiguousEntries.groovy -i %LOGDIR%\flight-ws_perfLog-%NR%.txt -o %LOGDIR%\aggregate-%NR%.txt

CALL groovy Perf4JRequestTotals.groovy -i %LOGDIR%\aggregate-%NR%.txt -file %LOGDIR%\requests-%NR%.csv


REM ----------------------------------------------------------------------------

ECHO Test run %NR% analyzed.
:end
