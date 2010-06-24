package org.tripplanner.flight.perf.analyzer;

import step.groovy.command.*;
import org.tripplanner.flight.perf.*;


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
        def previousMessage = "";

        def sequenceStart = 0L;
        def sequenceTimeSum = 0L;

        def lineNr = 0;
        i.eachLine { line ->
            lineNr++;

            //
            //  Extract current line data
            //
            def lineMatcher = ( line =~ Perf4JHelper.PERF_LOG_LINE_REGEX );
            def lineMatchResult = lineMatcher.matches();
            assert(lineMatchResult);

            def start = Long.parseLong(lineMatcher.group(1));
            def time = Long.parseLong(lineMatcher.group(2));
            def tag = lineMatcher.group(3);
            def message = lineMatcher.group(4);

            if (!previousTag.equals(tag)) {
                // change of tag

                if (previousTag.length() != 0) {
                    // end of previous sequence
                    def newTime = previousStart - sequenceStart + sequenceTimeSum;
                    // write line for previous sequence
                    o.printf(Perf4JHelper.sprintPerfLogLine(sequenceStart, newTime, previousTag, previousMessage));
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
            previousMessage = message;
        }

        if (previousTag.length() != 0) {
            // end of last sequence
            def newTime = previousStart - sequenceStart + sequenceTimeSum;
            // write line for last sequence
            o.printf(Perf4JHelper.sprintPerfLogLine(sequenceStart, newTime, previousTag, previousMessage));
        }

    }

}
