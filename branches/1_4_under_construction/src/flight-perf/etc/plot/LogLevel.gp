# clear previous settings
reset

# name
name="LogLevel"

# set style
set style line 1 lc rgb 'grey20'
set style line 2 lc rgb 'grey30'
set style line 3 lc rgb 'grey40'
set style line 4 lc rgb 'grey50'
set style line 5 lc rgb 'grey60'
set style line 6 lc rgb 'grey70'
set style increment user

set style data histogram
set style fill pattern 1 border -1
#set style fill solid
set boxwidth 0.75 relative

set logscale y

# decoration
set grid
set xlabel "Logging level"
set ylabel "Average request processing time (ms)"
set key outside right
unset key

# plot -------------------------------------------------------------------------
plot name . ".dat" using ($2):xtic(1)


# text output
set table name . ".gptable"
replot
unset table

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
