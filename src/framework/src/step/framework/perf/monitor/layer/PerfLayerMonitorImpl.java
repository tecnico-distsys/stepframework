package step.framework.perf.monitor.layer;

import java.io.*;
import java.util.*;


/**
 *  Performance layer monitor implementation.
 */
public class PerfLayerMonitorImpl implements PerfLayerMonitor {

    /** Performance layer record */
    private static class Record {
        Object tag;
        int nesting = 0;
        long startTime = -1L;
        long accTime = 0L;
        Map<String,String> context;

        Record(Object tag) {
            this.tag = tag;
            context = new TreeMap<String,String>();
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
            return String.format("tag[%s] accTime[%d%s] context[%s]",
                tag.toString(), accTime, TIME_UNIT, sb.toString());
        }
    }

    /** Record map */
    private Map<Object,Record> recordMap;

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
        recordMap = new TreeMap<Object,Record>();
    }

    public void dump(File f) throws IOException {
        Writer w = new FileWriter(f, /*append*/ true);
        dump(w);
        w.close();
    }

    public void dump(Writer w) throws IOException {
        for (Map.Entry<Object,Record> pair : recordMap.entrySet()) {
            Record record = pair.getValue();
            if (record.nesting != 0)
                throw new IllegalStateException(
                    String.format("Performance record for layer with tag %s is inconsistent. Probably because of missing exits (nesting is %d and should be %d).",
                        record.tag, record.nesting, 0));
            w.write(record.toString());
            w.write(EOL);
        }
        // empty line ends record
        w.write(EOL);
    }

    public void enter(Object tag) {
        if (tag == null)
            throw new IllegalArgumentException("Layer tag cannot be null");

        Record record = recordMap.get(tag);
        if (record == null) {
            record = new Record(tag);
            recordMap.put(tag, record);
        }

        if (record.nesting == 0) {
            record.startTime = getCurrentTime();
        }
        record.nesting++;
    }

    public void exit(Object tag) {
        if (tag == null)
            throw new IllegalArgumentException("Layer tag cannot be null");

        Record record = recordMap.get(tag);
        if (record == null)
            throw new IllegalStateException(
                String.format("No entry performance record for layer with tag %s", tag));
        if (record.nesting <= 0)
            throw new IllegalStateException(
                String.format("Entry performance record for layer with tag %s is inconsistent. Nesting value is %d and should be greater than %d.",
                    tag, record.nesting, 0));

        record.nesting--;

        if (record.nesting == 0) {
            long stopTime = getCurrentTime();

            if (record.startTime == -1L)
                throw new IllegalStateException(
                    String.format("Entry performance record for layer with tag %s has uninitialized entry time", tag));

            record.accTime += stopTime - record.startTime;
            record.startTime = -1L;
        }
    }

    public void context(Object tag, String key, String value) {
        if (tag == null)
            throw new IllegalArgumentException("Layer tag cannot be null");

        Record record = recordMap.get(tag);
        if (record == null) {
            record = new Record(tag);
            recordMap.put(tag, record);
        }
        record.context.put(key, value);
    }

}
