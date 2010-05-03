@ECHO OFF
CALL groovy RequestTotals.groovy -i %CATALINA_HOME%\logs\flight-ws_perfLog.txt
