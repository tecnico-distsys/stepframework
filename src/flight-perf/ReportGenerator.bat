@ECHO OFF
SETLOCAL
CALL env.bat set
CALL groovy src\org\tripplanner\flight\perf\report_generator\ConfigInstanceGraphReport.groovy %*
CALL groovy src\org\tripplanner\flight\perf\report_generator\MonReport.groovy %*
CALL groovy src\org\tripplanner\flight\perf\report_generator\XMLSizeReport.groovy %*
CALL groovy src\org\tripplanner\flight\perf\report_generator\RequestTypeReport.groovy %*
CALL groovy src\org\tripplanner\flight\perf\report_generator\CacheReport.groovy %*
CALL groovy src\org\tripplanner\flight\perf\report_generator\LogLevelReport.groovy %*
ENDLOCAL
GOTO end
:end
