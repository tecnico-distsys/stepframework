/**
 *  Groovy command to aggregate time values of contiguous entries with same tag,
 *  using the start field to compute elapsed time,
 *  in perf4j performance log file
 */

public class Perf4JAggregateContiguousEntries extends IOCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        Perf4JAggregateContiguousEntries instance = new Perf4JAggregateContiguousEntries();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    @Override protected void cmdRun() {

        err.println("Running " + this.class.simpleName);

        def previousStart = 0L;
        def previousTime = 0L;
        def previousTag = "";

        def sequenceStart;
        def sequenceTimeSum;

        def lineNr = 0;
        i.eachLine { line ->
            lineNr++;

            //
            //  Extract current line data
            //
            def lineMatcher = ( line =~ "start\\[(.*)\\] time\\[(.*)\\] tag\\[(.*)\\]" );
            def lineMatchResult = lineMatcher.matches();
            assert(lineMatchResult);

            def start = Long.parseLong(lineMatcher[0][1]);
            def time = Long.parseLong(lineMatcher[0][2]);
            def tag = lineMatcher[0][3];

            if (!previousTag.equals(tag)) {
                // change of tag

                if (previousTag.length() != 0) {
                    // end of previous sequence
                    def newTime = previousStart - sequenceStart + sequenceTimeSum;
                    // write line for previous sequence
                    o.printf("start[%d] time[%d] tag[%s]%n", sequenceStart, newTime, previousTag);
                }

                // beginning of new sequence
                sequenceStart = start;
                sequenceTimeSum = time;

            } else {
                // continuation of sequence
                sequenceTimeSum += time;

            }

            previousStart = start;
            previousTime = time;
            previousTag = tag;
        }

        if (previousTag.length() != 0) {
            // end of last sequence
            def newTime = previousStart - sequenceStart + sequenceTimeSum;
            // write line for last sequence
            o.printf("start[%d] time[%d] tag[%s]%n", sequenceStart, newTime, previousTag);
        }

    }

}
