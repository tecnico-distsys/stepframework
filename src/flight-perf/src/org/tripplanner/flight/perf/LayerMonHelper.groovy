package org.tripplanner.flight.perf;

/**
 *  Layer Monitor helper.
 */
class LayerMonHelper {

    /** Regular expression to match performance log line. */
    final static def PERF_LOG_LINE_REGEX = "thread\\[(.*?)\\] tag\\[(.*?)\\] accTime\\[(.*?)\\] context\\[(.*?)\\]";

}
