@ECHO OFF
REM Generate test workloads

SETLOCAL
SET CLASSPATH=%CLASSPATH%;..\..\framework\dist\stepframework.jar;..\..\flight-ws-cli\dist\flight-ws-cli.jar

CALL init-db.bat

IF NOT EXIST build MKDIR build
IF NOT EXIST build\loads MKDIR build\loads

PUSHD src
CALL groovy WorkloadGenerator.groovy -s -31       -n 100  -o ..\build\loads\load-1.obj   --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 6383      -n 100  -o ..\build\loads\load-2.obj   --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 175522    -n 100  -o ..\build\loads\load-3.obj   --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -2355     -n 100  -o ..\build\loads\load-4.obj   --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 6129      -n 100  -o ..\build\loads\load-5.obj   --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 834683468 -n 100  -o ..\build\loads\load-6.obj   --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 234423    -n 100  -o ..\build\loads\load-7.obj   --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 114563    -n 100  -o ..\build\loads\load-8.obj   --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -121234   -n 100  -o ..\build\loads\load-9.obj   --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 963       -n 100  -o ..\build\loads\load-10.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 12        -n 100  -o ..\build\loads\load-11.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 8907      -n 100  -o ..\build\loads\load-12.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 2357      -n 100  -o ..\build\loads\load-13.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 754       -n 100  -o ..\build\loads\load-14.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -3434     -n 100  -o ..\build\loads\load-15.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 94562     -n 100  -o ..\build\loads\load-16.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 34834267  -n 100  -o ..\build\loads\load-17.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 8999411   -n 100  -o ..\build\loads\load-18.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -237890   -n 100  -o ..\build\loads\load-19.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 54854     -n 100  -o ..\build\loads\load-20.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 102894    -n 100  -o ..\build\loads\load-21.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 5610      -n 100  -o ..\build\loads\load-22.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 993672    -n 100  -o ..\build\loads\load-23.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -23       -n 100  -o ..\build\loads\load-24.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 1231      -n 100  -o ..\build\loads\load-25.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 69736383  -n 100  -o ..\build\loads\load-26.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -3443     -n 100  -o ..\build\loads\load-27.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 56423211  -n 100  -o ..\build\loads\load-28.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 7854901   -n 100  -o ..\build\loads\load-29.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s -84       -n 100  -o ..\build\loads\load-30.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 653200    -n 100  -o ..\build\loads\load-31.obj  --maxthinktime 0 --maxgroup 20 -p db.properties --names ..\data\names.txt --surnames ..\data\surnames.txt
POPD
