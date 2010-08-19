package org.tripplanner.flight.perf.helper;

/**
 *  Event Monitor helper.
 */
class EventMonHelper {

    /** Regular expression to match performance log line. */
    final static def PERF_LOG_LINE_REGEX = "thread\\[(.*?)\\] tag\\[(.*?)\\] time\\[(.*?)\\] context\\[(.*?)\\]";

}
