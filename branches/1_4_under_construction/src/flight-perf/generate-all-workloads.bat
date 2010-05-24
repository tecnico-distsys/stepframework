@ECHO OFF
REM Generate all test workloads

:begin
SETLOCAL

% n stands for number, mg stands for maxgroup

CALL set-env.bat n1mg10
CALL generate-workloads.bat 1 10

CALL set-env.bat n100mg10 100 10
CALL generate-workloads.bat

CALL set-env.bat n100mg100 100 100
CALL generate-workloads.bat

CALL set-env.bat n100mg500 100 500
CALL generate-workloads.bat

CALL set-env.bat n100mg1000 100 1000
CALL generate-workloads.bat

:end
