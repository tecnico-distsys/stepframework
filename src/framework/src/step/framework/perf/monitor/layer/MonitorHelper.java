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


    static final String CATALINA_HOME = System.getenv("CATALINA_HOME");
    static final File CATALINA_HOME_DIR;
    static final File CATALINA_LOGS_DIR;

    static {
        if (CATALINA_HOME == null)
            throw new IllegalStateException("CATALINA_HOME environment variable is required!");

        CATALINA_HOME_DIR = new File(CATALINA_HOME);
        if (!CATALINA_HOME_DIR.exists() || !CATALINA_HOME_DIR.isDirectory())
            throw new IllegalStateException(
                String.format("CATALINA_HOME directory %s does not exist!", CATALINA_HOME));

        CATALINA_LOGS_DIR = new File(CATALINA_HOME_DIR, "logs");
        if (!CATALINA_LOGS_DIR.exists() || !CATALINA_LOGS_DIR.isDirectory())
            throw new IllegalStateException(
                "CATALINA_HOME logs directory does not exist!");
    }

    /**
     *  Provide output file
     */
    public static File getDumpFile() {
        String logFileName = String.format("PerfLayerLog-thr%d.txt", Thread.currentThread().getId());
        return new File(CATALINA_LOGS_DIR, logFileName);
    }

}
