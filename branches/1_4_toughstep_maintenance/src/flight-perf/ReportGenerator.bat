@ECHO OFF
SETLOCAL
CALL env.bat set

IF "%1"=="" (
    ECHO Calling all report generators...
)

IF NOT "%1"=="" (
    ECHO Calling %1 report generator...
    SET JUST_ONE_PLEASE=true
    GOTO %1
)

:graph
CALL groovy src\org\tripplanner\flight\perf\report_generator\ConfigInstanceGraphReport.groovy %*
IF "%JUST_ONE_PLEASE%"=="true" GOTO end

:breakdown
CALL groovy src\org\tripplanner\flight\perf\report_generator\BreakdownReport.groovy %*
IF "%JUST_ONE_PLEASE%"=="true" GOTO end

:mon
CALL groovy src\org\tripplanner\flight\perf\report_generator\MonReport.groovy %*
IF "%JUST_ONE_PLEASE%"=="true" GOTO end

:xml
CALL groovy src\org\tripplanner\flight\perf\report_generator\XMLSizeReport.groovy %*
IF "%JUST_ONE_PLEASE%"=="true" GOTO end

:requests
CALL groovy src\org\tripplanner\flight\perf\report_generator\RequestTypeReport.groovy %*
IF "%JUST_ONE_PLEASE%"=="true" GOTO end

:cache
CALL groovy src\org\tripplanner\flight\perf\report_generator\CacheReport.groovy %*
IF "%JUST_ONE_PLEASE%"=="true" GOTO end

:logs
CALL groovy src\org\tripplanner\flight\perf\report_generator\LogLevelReport.groovy %*
IF "%JUST_ONE_PLEASE%"=="true" GOTO end

:users
CALL groovy src\org\tripplanner\flight\perf\report_generator\UsersReport.groovy %*
IF "%JUST_ONE_PLEASE%"=="true" GOTO end

:usersrand
CALL groovy src\org\tripplanner\flight\perf\report_generator\UsersRandomReservationNumberReport.groovy %*
IF "%JUST_ONE_PLEASE%"=="true" GOTO end

ENDLOCAL
GOTO end
:end
