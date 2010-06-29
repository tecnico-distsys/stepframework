@ECHO OFF
SETLOCAL
CALL env.bat set
CALL groovy src\org\tripplanner\flight\perf\report_generator\AggReport.groovy %*
ENDLOCAL
