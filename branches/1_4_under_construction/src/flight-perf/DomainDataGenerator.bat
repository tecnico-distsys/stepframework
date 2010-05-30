@ECHO OFF
SETLOCAL
CALL env.bat set
CALL groovy DomainDataGenerator.groovy %*
ENDLOCAL