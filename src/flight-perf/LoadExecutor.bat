@ECHO OFF
SETLOCAL
CALL env.bat set
CALL groovy LoadExecutor.groovy %*
ENDLOCAL