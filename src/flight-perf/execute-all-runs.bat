@ECHO OFF
REM Generate all test workloads

:begin
SETLOCAL

IF "%1"=="" GOTO end
GOTO %1

:baseline
CALL set-env.bat n100mg10 n100mg10
CALL execute-runs.bat 50

:xml
CALL set-env.bat n100mg100 n100mg100
CALL execute-runs.bat 50

CALL set-env.bat n100mg500 n100mg500
CALL execute-runs.bat 50

CALL set-env.bat n100mg1000 n100mg1000
CALL execute-runs.bat 50

:log
COPY ..\flight-ws\src\log4j.xml ..\flight-ws\src\log4j.xml.bak
COPY config\log-level-debug\log4j.xml ..\flight-ws\src\log4j.xml
CALL set-env.bat n100mg10 n100mg10_log-level-debug
CALL execute-runs.bat 50
DEL ..\flight-ws\src\log4j.xml
COPY ..\flight-ws\src\log4j.xml.bak ..\flight-ws\src\log4j.xml
DEL ..\flight-ws\src\log4j.xml.bak

COPY ..\flight-ws\src\log4j.xml ..\flight-ws\src\log4j.xml.bak
COPY config\log-level-error\log4j.xml ..\flight-ws\src\log4j.xml
CALL set-env.bat n100mg10 n100mg10_log-level-error
CALL execute-runs.bat 50
DEL ..\flight-ws\src\log4j.xml
COPY ..\flight-ws\src\log4j.xml.bak ..\flight-ws\src\log4j.xml
DEL ..\flight-ws\src\log4j.xml.bak

COPY ..\flight-ws\src\log4j.xml ..\flight-ws\src\log4j.xml.bak
COPY config\log-level-info\log4j.xml ..\flight-ws\src\log4j.xml
CALL set-env.bat n100mg10 n100mg10_log-level-info
CALL execute-runs.bat 50
DEL ..\flight-ws\src\log4j.xml
COPY ..\flight-ws\src\log4j.xml.bak ..\flight-ws\src\log4j.xml
DEL ..\flight-ws\src\log4j.xml.bak

COPY ..\flight-ws\src\log4j.xml ..\flight-ws\src\log4j.xml.bak
COPY config\log-level-off\log4j.xml ..\flight-ws\src\log4j.xml
CALL set-env.bat n100mg10 n100mg10_log-level-off
CALL execute-runs.bat 50
DEL ..\flight-ws\src\log4j.xml
COPY ..\flight-ws\src\log4j.xml.bak ..\flight-ws\src\log4j.xml
DEL ..\flight-ws\src\log4j.xml.bak

COPY ..\flight-ws\src\log4j.xml ..\flight-ws\src\log4j.xml.bak
COPY config\log-level-trace\log4j.xml ..\flight-ws\src\log4j.xml
CALL set-env.bat n100mg10 n100mg10_log-level-trace
CALL execute-runs.bat 50
DEL ..\flight-ws\src\log4j.xml
COPY ..\flight-ws\src\log4j.xml.bak ..\flight-ws\src\log4j.xml
DEL ..\flight-ws\src\log4j.xml.bak

COPY ..\flight-ws\src\log4j.xml ..\flight-ws\src\log4j.xml.bak
COPY config\log-level-warn\log4j.xml ..\flight-ws\src\log4j.xml
CALL set-env.bat n100mg10 n100mg10_log-level-warn
CALL execute-runs.bat 50
DEL ..\flight-ws\src\log4j.xml
COPY ..\flight-ws\src\log4j.xml.bak ..\flight-ws\src\log4j.xml
DEL ..\flight-ws\src\log4j.xml.bak


:hibernate_cache

:cipher

:end
