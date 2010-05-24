@ECHO OFF
REM

SETLOCAL
SET CLASSPATH=%CLASSPATH%;%STEP_HOME%\lib\SuperCSV-1.52.jar;%STEP_HOME%\lib\commons-math-2.1.jar

PUSHD src
CALL groovy ReportStatistics.groovy
POPD
