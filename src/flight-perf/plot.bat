@ECHO OFF
REM Plot graphs using gnuplot (http://www.gnuplot.info/)

:begin
SETLOCAL

:check
IF "%PERF_PLOT_DIR%"=="" GOTO error_plotdir
GOTO main

:error_plotdir
ECHO Error: environment variable PERF_PLOT_DIR is not set!
GOTO end

:main
PUSHD plot
gnuplot *.gp
MOVE *.png ..\%PERF_PLOT_DIR%
MOVE *.tex ..\%PERF_PLOT_DIR%
MOVE *.pdf ..\%PERF_PLOT_DIR%
POPD

:end
