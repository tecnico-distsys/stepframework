@ECHO OFF
:: Plot graphs using gnuplot (http://www.gnuplot.info/)
::
:: Author: Miguel Pardal
:: Date:   2010-05-26

:: -----------------------------------------------------------------------------
:begin
SETLOCAL

:: -----------------------------------------------------------------------------
:check
IF "%PERF_PLOT_DIR%"=="" GOTO error_plotdir

GOTO main

:error_plotdir
ECHO Error: environment variable PERF_PLOT_DIR is not set!
GOTO end

:: -----------------------------------------------------------------------------
:main

PUSHD plot
gnuplot *.gp
MOVE *.png ..\%PERF_PLOT_DIR%
MOVE *.tex ..\%PERF_PLOT_DIR%
MOVE *.pdf ..\%PERF_PLOT_DIR%
POPD

:: -----------------------------------------------------------------------------
:end
