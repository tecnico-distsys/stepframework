# clear previous settings
reset

# name
name = "XMLSize"

# set style
set style data histogram
set style histogram rowstacked
set style fill pattern 1 border lt -1
set boxwidth 0.75 relative

# decoration
set grid
set title "Timeslices"
set xlabel "Request + response XML Logical Length (characters)"
set ylabel "Request processing time (ms)"
set key outside right

# plot
plot name . ".dat" using ($6):xtic(1) title "Hibernate Reads", '' using ($7) title "Hibernate Writes", '' using ($5-$6) title "Service", '' u ($3-$5) t "SOAP", '' u ($2-$3) t "Web"

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
