package step.framework.perf.monitor;

import org.perf4j.*;
import org.perf4j.log4j.*;

import step.framework.context.*;


/**
 *  Performance stop-watch helper methods.
 */
public class StopWatchHelper {

    private static final String STOP_WATCH_PROPERTY = "step.framework.perf.StopWatch";

    /**
     *  Access current thread stop watch.
     */
    public static StopWatch getThreadStopWatch() {
        return getThreadStopWatch(false);
    }

    /**
     *  Access current thread stop watch.
     *  A new stop watch is created if one does not exist and create is true.
     */
    public static StopWatch getThreadStopWatch(boolean create) {
        ThreadContext thrCtx = ThreadContext.getInstance();
        StopWatch stopWatch = (StopWatch) thrCtx.get(STOP_WATCH_PROPERTY);
        if(stopWatch == null && create) {
            stopWatch = new Log4JStopWatch();
            thrCtx.put(STOP_WATCH_PROPERTY, stopWatch);
        }
        return stopWatch;
    }

    /**
     *  Delete current thread stop watch.
     */
    public static void deleteThreadStopWatch() {
        ThreadContext thrCtx = ThreadContext.getInstance();
        thrCtx.remove(STOP_WATCH_PROPERTY);
    }

}
