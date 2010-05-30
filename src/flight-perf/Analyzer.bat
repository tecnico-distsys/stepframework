@ECHO OFF
SETLOCAL
CALL env.bat set
CALL groovy Analyzer.groovy %*
ENDLOCAL