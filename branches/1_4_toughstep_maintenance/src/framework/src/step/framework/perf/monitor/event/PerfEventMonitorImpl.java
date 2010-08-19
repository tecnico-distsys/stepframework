package step.framework.perf.monitor.event;

import java.io.*;
import java.util.*;


/**
 *  Performance event monitor implementation.
 */
public class PerfEventMonitorImpl implements PerfEventMonitor {

    /** Performance event record */
    private static class Record {
        Object tag;
        long time;
        Map<String,String> context;

        Record(Object tag) {
            this.tag = tag;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (context != null && context.size() > 0) {
                for (Map.Entry<String,String> pair : context.entrySet()) {
                    sb.append(pair.getKey());
                    sb.append(":");
                    sb.append(pair.getValue());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length()-1);
            }
            return String.format("tag[%s] time[%d%s] context[%s]",
                tag.toString(), time, TIME_UNIT, sb.toString());
        }
    }

    /** record list */
    private List<Record> recordList;
    /** current context map */
    private Map<String,String> currentContext;

    /** platform end of line */
    private static final String EOL = System.getProperty("line.separator");

    /** timestamp unit - ns nanoseconds, us microseconds, ms milliseconds */
    private static final String TIME_UNIT = "ns";

    /** return current time */
    private long getCurrentTime() {
        return System.nanoTime();
    }


    // Interface implementation ------------------------------------------------

    public void init() {
        recordList = new ArrayList<Record>();
    }

    public void dump(File f) throws IOException {
        Writer w = new FileWriter(f, /*append*/ true);
        dump(w);
        w.close();
    }

    public void dump(Writer w) throws IOException {
        for (Record record : recordList) {
            w.write(record.toString());
            w.write(EOL);
        }
        // empty line separates records
        w.write(EOL);
    }

    public void event(Object tag) {
        if (tag == null)
            throw new IllegalArgumentException("Event tag cannot be null");
        Record record = new Record(tag);
        record.time = getCurrentTime();
        record.context = currentContext;
        currentContext = null;
        recordList.add(record);
    }

    public void context(String key, String value) {
        if (currentContext == null) {
            currentContext = new HashMap<String,String>();
        }
        currentContext.put(key, value);
    }

}
