@ECHO OFF
SETLOCAL
CALL env.bat set
CALL groovy LoadGenerator.groovy %*
ENDLOCAL