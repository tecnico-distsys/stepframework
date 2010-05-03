/**
 *  Groovy command to calculate request totals in perf4j performance log file
 */

import org.apache.commons.cli.*;

public class RequestTotals extends IOCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        RequestTotals instance = new RequestTotals();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    @Override protected void cmdRun() {

        err.println("Running " + this.class.simpleName);

        // ~ creates a Pattern
        // =~ creates a Matcher
        // ==~ tests if String matches the pattern

        final def master = "filter";
        def totalMap = new HashMap();

        i.eachLine { line ->
            def lineMatcher = ( line =~ "start\\[(.*)\\] time\\[(.*)\\] tag\\[(.*)\\]" );
            if(!lineMatcher.matches()) {
                // no match, ignore line
                err.println("Ignoring unrecognized line: " + line);
            } else {
                // process recognized line

                final def lineFormat = "tag[%s] time[%d] percent[%s]%n";

                def tag = lineMatcher[0][3];
                def time = Long.parseLong(lineMatcher[0][2]);

                if(lineMatcher[0][3] ==~ master) {
                    //
                    // process start of new request (implicit end of previous request)
                    //

                    // dump previous request data
                    def requestTotal = time;
                    o.printf(lineFormat, tag, requestTotal, "1");

                    // Sort the keys by descending value
                    def keys = totalMap.keySet().sort{totalMap[it]}.reverse();
                    // print slice
                    keys.each {
                        def percent = totalMap[it] / requestTotal;
                        o.printf(lineFormat, it, totalMap[it], percent);
                    }

                    // reset map
                    totalMap = new HashMap();

                    o.println();

                } else {
                    // process request slice
                    def total = totalMap[tag];
                    if(total == null) total = 0;
                    total += time;
                    totalMap[tag] = total;
                }
            }
        }

    }

}
