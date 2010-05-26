@ECHO OFF
:: Report statistics (incomplete)
::
:: Author: Miguel Pardal
:: Date:   2010-05-26

:: -----------------------------------------------------------------------------
:main

PUSHD src
CALL groovy ReportStatistics.groovy
POPD

:: -----------------------------------------------------------------------------
:end
