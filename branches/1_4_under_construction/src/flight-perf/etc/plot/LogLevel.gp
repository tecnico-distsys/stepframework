# clear previous settings
reset

# name
name = "LogLevel"

# set style
set style data histogram
#set style histogram errorbars
#set style histogram rowstacked
set style fill pattern 1 border lt -1
set boxwidth 0.75 relative

set logscale y

# decoration
set grid
set xlabel "Logging level"
set ylabel "Average request processing time (ms)"
set key outside right
unset key

# plot
plot name . ".dat" using ($2):xtic(1)

# configure output
set terminal png size 1024,768
set output name . ".png"
replot

set terminal latex
set output name . ".tex"
replot

set terminal pdf
set output name . ".pdf"
replot
