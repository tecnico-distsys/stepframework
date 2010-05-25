@ECHO OFF
REM

PUSHD src
CALL groovy ReportStatistics.groovy
POPD
