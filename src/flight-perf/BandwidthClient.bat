@ECHO OFF
SETLOCAL
CALL env.bat set
CALL groovy src\step\groovy\net\BandwidthClient.groovy %*
ENDLOCAL
