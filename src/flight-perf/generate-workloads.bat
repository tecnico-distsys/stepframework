@ECHO OFF
REM Generate test workloads

:begin
SETLOCAL
SET CLASSPATH=%CLASSPATH%;..\..\framework\dist\stepframework.jar;..\..\flight-ws-cli\dist\flight-ws-cli.jar

:check
IF "%PERF_LOAD_DIR%"=="" GOTO error_loaddir

SET N=100
IF NOT "%1"=="" SET N=%1

SET MAXGROUP=10
IF NOT "%2"=="" SET MAXGROUP=%2

GOTO main

:error_loaddir
ECHO Error: environment variable PERF_LOAD_DIR is not set!
GOTO end

:usage
ECHO Usage: %0 (n=100) (maxgroup=10)
GOTO end

:main
IF "%PERF_INIT_DB%"=="false" GOTO init_db_done
CALL init-db.bat
:init_db_done

IF NOT EXIST %PERF_LOAD_DIR% MKDIR %PERF_LOAD_DIR%

PUSHD src
CALL groovy WorkloadGenerator.groovy -s -31       -n %N% -o ..\%PERF_LOAD_DIR%\load-1.obj   --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 6383      -n %N% -o ..\%PERF_LOAD_DIR%\load-2.obj   --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 175522    -n %N% -o ..\%PERF_LOAD_DIR%\load-3.obj   --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -2355     -n %N% -o ..\%PERF_LOAD_DIR%\load-4.obj   --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 6129      -n %N% -o ..\%PERF_LOAD_DIR%\load-5.obj   --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 834683468 -n %N% -o ..\%PERF_LOAD_DIR%\load-6.obj   --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 234423    -n %N% -o ..\%PERF_LOAD_DIR%\load-7.obj   --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 114563    -n %N% -o ..\%PERF_LOAD_DIR%\load-8.obj   --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -121234   -n %N% -o ..\%PERF_LOAD_DIR%\load-9.obj   --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 963       -n %N% -o ..\%PERF_LOAD_DIR%\load-10.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 12        -n %N% -o ..\%PERF_LOAD_DIR%\load-11.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 8907      -n %N% -o ..\%PERF_LOAD_DIR%\load-12.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 2357      -n %N% -o ..\%PERF_LOAD_DIR%\load-13.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 754       -n %N% -o ..\%PERF_LOAD_DIR%\load-14.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -3434     -n %N% -o ..\%PERF_LOAD_DIR%\load-15.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 94562     -n %N% -o ..\%PERF_LOAD_DIR%\load-16.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 34834267  -n %N% -o ..\%PERF_LOAD_DIR%\load-17.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 8999411   -n %N% -o ..\%PERF_LOAD_DIR%\load-18.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -237890   -n %N% -o ..\%PERF_LOAD_DIR%\load-19.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 54854     -n %N% -o ..\%PERF_LOAD_DIR%\load-20.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 102894    -n %N% -o ..\%PERF_LOAD_DIR%\load-21.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 5610      -n %N% -o ..\%PERF_LOAD_DIR%\load-22.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 993672    -n %N% -o ..\%PERF_LOAD_DIR%\load-23.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -23       -n %N% -o ..\%PERF_LOAD_DIR%\load-24.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 1231      -n %N% -o ..\%PERF_LOAD_DIR%\load-25.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 69736383  -n %N% -o ..\%PERF_LOAD_DIR%\load-26.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -3443     -n %N% -o ..\%PERF_LOAD_DIR%\load-27.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 56423211  -n %N% -o ..\%PERF_LOAD_DIR%\load-28.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 7854901   -n %N% -o ..\%PERF_LOAD_DIR%\load-29.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -84       -n %N% -o ..\%PERF_LOAD_DIR%\load-30.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 653200    -n %N% -o ..\%PERF_LOAD_DIR%\load-31.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -856      -n %N% -o ..\%PERF_LOAD_DIR%\load-32.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -88       -n %N% -o ..\%PERF_LOAD_DIR%\load-33.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 1675      -n %N% -o ..\%PERF_LOAD_DIR%\load-34.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 98465     -n %N% -o ..\%PERF_LOAD_DIR%\load-35.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 662921    -n %N% -o ..\%PERF_LOAD_DIR%\load-36.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -12       -n %N% -o ..\%PERF_LOAD_DIR%\load-37.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 3143      -n %N% -o ..\%PERF_LOAD_DIR%\load-38.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 874562353 -n %N% -o ..\%PERF_LOAD_DIR%\load-39.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -1154564  -n %N% -o ..\%PERF_LOAD_DIR%\load-40.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -345836   -n %N% -o ..\%PERF_LOAD_DIR%\load-41.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 9533      -n %N% -o ..\%PERF_LOAD_DIR%\load-42.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 6202111   -n %N% -o ..\%PERF_LOAD_DIR%\load-43.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 45937     -n %N% -o ..\%PERF_LOAD_DIR%\load-44.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -3898250  -n %N% -o ..\%PERF_LOAD_DIR%\load-45.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 342564    -n %N% -o ..\%PERF_LOAD_DIR%\load-46.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 567623400 -n %N% -o ..\%PERF_LOAD_DIR%\load-47.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 7645688   -n %N% -o ..\%PERF_LOAD_DIR%\load-48.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -974398   -n %N% -o ..\%PERF_LOAD_DIR%\load-49.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 655       -n %N% -o ..\%PERF_LOAD_DIR%\load-50.obj  --maxthinktime 0 --maxgroup %MAXGROUP% --errorprobability 0.20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
POPD

:end
