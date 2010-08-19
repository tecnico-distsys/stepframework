@ECHO OFF
SETLOCAL
CALL env.bat set
CALL groovy src\org\tripplanner\flight\perf\LoadGenerator.groovy %*
ENDLOCAL
