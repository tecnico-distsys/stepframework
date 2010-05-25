@ECHO OFF
REM Generate all test workloads

:begin
SETLOCAL

REM n stands for number, mg stands for maxgroup

CALL set-env.bat n1mg10
CALL generate-workloads.bat 1 10

CALL set-env.bat n100mg10
CALL generate-workloads.bat 100 10

CALL set-env.bat n100mg100
CALL generate-workloads.bat 100 100

CALL set-env.bat n100mg500
CALL generate-workloads.bat 100 500

CALL set-env.bat n100mg1000
CALL generate-workloads.bat 100 1000

:end
