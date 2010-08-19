package step.framework.perf.monitor.perf4j;

import java.util.*;

import org.perf4j.*;
import org.perf4j.log4j.*;


/**
 *  Performance stop-watch helper methods.
 */
class StopWatchHelper {

    /** ThreadLocal stopwatch collection - each thread has one of its own */
    private static final ThreadLocal<Map<String,StopWatch>> stopWatches =
        new ThreadLocal<Map<String,StopWatch>>() {

        @Override protected Map<String,StopWatch> initialValue() {
            return newMap();
        }

    };

    /** Decide new map implementation */
    private static Map<String,StopWatch> newMap() {
        return new HashMap<String,StopWatch>();
    }


    /**
     *  Access one of the current thread's stop watches.
     *  A new stop watch is created if it does not exist.
     */
    public static StopWatch getThreadStopWatch(String stopWatchId) {
        StopWatch stopWatch = stopWatches.get().get(stopWatchId);
        if(stopWatch == null) {
            stopWatch = new Log4JStopWatch();
            stopWatches.get().put(stopWatchId, stopWatch);
        }
        return stopWatch;
    }

    /**
     *  Delete one of the current thread's stop watches.
     */
    public static StopWatch deleteThreadStopWatch(String stopWatchId) {
        return stopWatches.get().remove(stopWatchId);
    }

    /**
     *  Delete all of the current thread's stop watches.
     */
    public static void deleteAllThreadStopWatches() {
        stopWatches.get().clear();
    }

}
