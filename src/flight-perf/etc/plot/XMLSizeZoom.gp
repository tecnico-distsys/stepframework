# clear previous settings
reset

# name
name = "XMLSizeZoom"

# set style
set style line 1 lc rgb 'grey20'
set style line 2 lc rgb 'grey30'
set style line 3 lc rgb 'grey40'
set style line 4 lc rgb 'grey50'
set style line 5 lc rgb 'grey60'
set style line 6 lc rgb 'grey70'
set style increment user

set style data histogram
set style histogram rowstacked
set style fill pattern 1 border lt -1
set boxwidth 0.75 relative

# decoration
set grid
set xlabel "Average total XML logical length (characters)"
set ylabel "Average request processing time (ms)"
set key outside right

# plot -------------------------------------------------------------------------
plot "XMLSize.dat" using ($3):xtic(1) title "Web Service", '' u 2 t "Web"


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
