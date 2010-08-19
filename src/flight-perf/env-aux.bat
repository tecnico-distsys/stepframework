@ECHO OFF
:: Use batch parameters to get fully qualified path name.
:: Unfortunately parameters do not work with %VAR% only with parameters %1
:: http://www.microsoft.com/resources/documentation/windows/xp/all/proddocs/en-us/percent.mspx?mfr=true
SET STEP_SRC_HOME=%~f1