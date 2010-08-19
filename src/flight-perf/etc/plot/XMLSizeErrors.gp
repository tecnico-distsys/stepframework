# clear previous settings
reset

# name
name = "XMLSizeErrors"

# set style
set style line 1 lc rgb 'grey80'
set style line 2 lc rgb 'grey50'
set style line 3 lc rgb 'grey20'
#set style line 4 lc rgb 'grey50'
#set style line 5 lc rgb 'grey60'
#set style line 6 lc rgb 'grey70'
set style increment user

#set style fill pattern 1 border lt -1
set style fill solid

# decoration
set grid
set xlabel "Average total XML logical length (characters)"
set xrange [3215:222281]
set xtics

set ylabel "Percentage of requests %"
set yrange [0:100]

set key outside right

set grid front

# plot -------------------------------------------------------------------------
set samples 1000
plot name . ".dat" using 1:($2/$2*100) title "Successful" with filledcurves y1=0, "" u 1:(($3+$4)/$2*100) t "App. Exceptions" w filledc y1=0, "" u 1:($4/$2*100) t "System Errors" w filledc y1=0

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
