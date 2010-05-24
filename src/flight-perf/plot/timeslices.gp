# clear previous settings
reset

# set style
set style data histogram
set style histogram rowstacked
set style fill pattern 1 border lt -1
set boxwidth 0.75 relative

# decoration
set grid
set title "Timeslices"
set xlabel "Request type"
set ylabel "Request processing time (ms)"
set key outside right

# plot
plot 'timeslices.dat' using ($6):xtic(1) title "Hibernate", '' using ($5-$6) title "Service", '' u ($4-$5) t "WSInterceptor", '' u ($3-$4) t "SOAP", '' u ($2-$3) t "Web"
# plot for[i=1:4] 'timeslices.dat' using i

# configure output
set terminal png size 1024,768
set output "timeslices.png"
replot

set terminal latex
set output "timeslices.tex"
replot

set terminal pdf
set output "timeslices.pdf"
replot
