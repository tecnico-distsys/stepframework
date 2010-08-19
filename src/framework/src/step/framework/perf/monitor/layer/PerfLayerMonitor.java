package step.framework.perf.monitor.layer;

import java.io.*;
import java.util.*;


/**
 *  Performance layer monitor interface.<br />
 *  <br />
 *  Captures performance data totals for each framework layer.<br />
 *  Capture session is initiated by init() and terminated by dump().<br />
 *  Layer boundaries are crossed with enter() and exit().
 */
public interface PerfLayerMonitor {

    /** Initiate capture session */
    public void init();

    /** Terminate capture session and append performance data to file
        using the platform's default character encoding */
    public void dump(File f) throws IOException;
    /** Terminate capture session and dump performance data to writer */
    public void dump(Writer w) throws IOException;

    /** Enter layer and start timer. */
    public void enter(Object tag);

    /** Exit layer and stop timer. */
    public void exit(Object tag);

    /** Add context attribute (key-value pair) to be recorded with next layer exit */
    public void context(Object tag, String key, String value);

}
