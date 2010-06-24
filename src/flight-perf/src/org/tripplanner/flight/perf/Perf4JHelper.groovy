package org.tripplanner.flight.perf;

/**
 *  Perf4J helper.
 */
class Perf4JHelper {

    private final static def BR = System.getProperty("line.separator");

    /** Regular expression to match performance log line. */
    final static def PERF_LOG_LINE_REGEX = "start\\[(.*?)\\] time\\[(.*?)\\] ?(?:tag\\[(.*?)\\])?? ?(?:message\\[(.*?)\\])?? *";

    /** Produce a string for the performance log line using the provided data. Tag and message can be null. */
    static def sprintPerfLogLine(start, time, tag, message) {
        def result = sprintf("start[%d] time[%d]", start, time);
        if (tag != null && tag.length() > 0)
            result += sprintf(" tag[%s]", tag);
        if (message != null && message.length() > 0)
            result += sprintf(" message[%s]", message);
        result += BR;
        return result;
    }

}
