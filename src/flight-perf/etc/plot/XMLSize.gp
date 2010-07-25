# clear previous settings
reset

# name
name = "XMLSize"

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
#set style fill solid
set boxwidth 0.75 relative

# decoration
set grid
set xlabel "Average total XML logical length (characters)"
set ylabel "Average request processing time (ms)"
set key outside right

# plot -------------------------------------------------------------------------
plot name . ".dat" using ($7):xtic(1) title "Hibernate Reads", "" using 6 title "Hibernate Writes", "" using 5 title "Hibernate Engine", "" using 4 title "Service", "" u 3 t "Web Service", "" u 2 t "Web"


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
