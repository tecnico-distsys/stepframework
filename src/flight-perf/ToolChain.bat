@ECHO OFF
ECHO *** Compiling ***
PUSHD ..
CALL ant rebuild
POPD
ECHO *** Load Generator ***
CALL LoadGenerator.bat   %*
ECHO *** Load Executor ***
CALL LoadExecutor.bat    %*
ECHO *** Analyzer ***
CALL Analyzer.bat        %*
ECHO *** Report Generator ***
CALL ReportGenerator.bat %*
