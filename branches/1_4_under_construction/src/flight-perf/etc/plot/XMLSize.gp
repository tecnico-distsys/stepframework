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
set xlabel "Average total XML logical length (characters)"
set ylabel "Average request processing time (ms)"
set key outside right

# define function to convert negative numbers to zero (elapsed time cannot be negative)
zeronegative( a ) = ( a < 0 ) ? 0 : a

# plot
plot name . ".dat" using ($7):xtic(1) title "Hibernate Reads", '' using ($8) title "Hibernate Writes", '' using (zeronegative($6-($7+$8))) title "Hibernate Engine", '' using (zeronegative($5-$6)) title "Service", '' u (zeronegative($3-$5)) t "SOAP", '' u (zeronegative($2-$3)) t "Web"

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
