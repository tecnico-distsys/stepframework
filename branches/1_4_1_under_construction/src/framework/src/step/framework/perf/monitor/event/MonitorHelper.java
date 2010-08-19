package step.framework.perf.monitor.event;

import java.io.*;
import java.util.*;
import step.framework.context.*;


/**
 *  Monitor helper methods.
 */
class MonitorHelper {

    static final String THREAD_MONITOR_PROPERTY = "step.framework.perf.monitor.event.PerfMonitor";

    /**
     *  Access or create thread monitor.
     */
    public static PerfEventMonitor get() {
        ThreadContext thrCtx = ThreadContext.getInstance();

        Object monitor = thrCtx.get(THREAD_MONITOR_PROPERTY);
        if (monitor == null) {
            monitor = new PerfEventMonitorImpl();
            thrCtx.put(THREAD_MONITOR_PROPERTY, monitor);
        }
        return (PerfEventMonitor) monitor;
    }


    /**
     *  Provide output file
     */
    public static File getDumpFile() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        String logFileName = String.format("PerfEvent-thr%d.log", Thread.currentThread().getId());
        return new File(tempDir, logFileName);
    }

}
