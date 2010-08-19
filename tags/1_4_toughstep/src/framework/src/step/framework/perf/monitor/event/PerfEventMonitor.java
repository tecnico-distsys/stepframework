package step.framework.perf.monitor.event;

import java.io.*;
import java.util.*;

/**
 *  Performance event monitor interface.<br />
 *  <br />
 *  Captures performance related events.<br />
 *  Capture session is initiated by init() and terminated by dump()
 */
public interface PerfEventMonitor {

    /** Initiate capture session */
    public void init();

    /** Terminate capture session and append performance data to file
        using the platform's default character encoding */
    public void dump(File f) throws IOException;
    /** Terminate capture session and dump performance data to writer */
    public void dump(Writer w) throws IOException;

    /** Record event */
    public void event(Object tag);

    /** Add context attribute (key, value pair) to be recorded with next event */
    public void context(String key, String value);

}
