@ECHO OFF
SETLOCAL
CALL env.bat set
CALL groovy src\step\groovy\net\BandwidthServer.groovy %*
ENDLOCAL
