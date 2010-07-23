# clear previous settings
reset

# name
name = "Users"

# set style
set style line 1 lc rgb 'grey20'
set style line 2 lc rgb 'grey30'
set style line 3 lc rgb 'grey40'
set style line 4 lc rgb 'grey50'
set style line 5 lc rgb 'grey60'
set style line 6 lc rgb 'grey70'
set style increment user

set style fill pattern 1 border -1
set style histogram rowstacked
set style data histogram
set boxwidth 0.75 relative

# decoration
set grid
unset title
# set xtic rotate by -30 scale 0
set xlabel "Number of concurrent users"
set ylabel "Average request processing time (ms)"
set key outside right

# define function to convert negative numbers to zero (elapsed time cannot be negative)
zeronegative( a ) = ( a < 0 ) ? 0 : a


# plot
plot name . ".dat" using ($7):xtic(1) title "Hibernate Reads", '' using ($8) title "Hibernate Writes", '' using (zeronegative($6-($7+$8))) title "Hibernate Engine", '' using ($5-$6) title "Service", '' u ($3-$5) t "Web Service", '' u ($2-$3) t "Web"

# text output
set table name . ".table"
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
