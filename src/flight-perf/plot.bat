@ECHO OFF
REM Plot graphs using gnuplot (http://www.gnuplot.info/)

:begin
SETLOCAL

:check
IF "%PLOT_DIR%"=="" GOTO error_plotdir
GOTO main

:error_plotdir
ECHO Error: environment variable PLOT_DIR is not set!
GOTO end

:main
PUSHD plot
gnuplot *.gp
MOVE *.png ..\%PLOT_DIR%
MOVE *.tex ..\%PLOT_DIR%
MOVE *.pdf ..\%PLOT_DIR%
POPD

:end
