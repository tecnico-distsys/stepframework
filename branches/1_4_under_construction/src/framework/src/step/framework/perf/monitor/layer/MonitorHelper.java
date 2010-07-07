package step.framework.perf.monitor.layer;

import java.io.*;
import java.util.*;
import step.framework.context.*;


/**
 *  Monitor helper methods.
 */
class MonitorHelper {

    static final String THREAD_MONITOR_PROPERTY = "step.framework.perf.monitor.layer.PerfMonitor";

    /**
     *  Access or create thread monitor.
     */
    public static PerfLayerMonitor get() {
        ThreadContext thrCtx = ThreadContext.getInstance();

        Object monitor = thrCtx.get(THREAD_MONITOR_PROPERTY);
        if (monitor == null) {
            monitor = new PerfLayerMonitorImpl();
            thrCtx.put(THREAD_MONITOR_PROPERTY, monitor);
        }
        return (PerfLayerMonitor) monitor;
    }


    /**
     *  Provide output file
     */
    public static File getDumpFile() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        String logFileName = String.format("PerfLayer-thr%d.log", Thread.currentThread().getId());
        return new File(tempDir, logFileName);
    }

}
