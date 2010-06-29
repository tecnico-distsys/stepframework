# clear previous settings
reset

# set style
set style data histogram
set style histogram rowstacked
set style fill pattern 1 border lt -1
set boxwidth 0.75 relative

# decoration
set grid
set xlabel "Average total XML logical length (characters)"
set ylabel "Average request processing time (ms)"
set key outside right

# plot
plot "XMLSize.dat" using ($3-$5):xtic(1) title "SOAP", '' u ($2-$3) t "Web"

# configure output
set terminal png size 1024,768
set output "XMLSizeZoom.png"
replot

set terminal latex
set output "XMLSizeZoom.tex"
replot

set terminal pdf
set output "XMLSizeZoom.pdf"
replot
