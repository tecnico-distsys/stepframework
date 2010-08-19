package step.framework.perf.monitor.layer;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.logging.*;

import org.perf4j.*;

import step.framework.perf.monitor.layer.*;


/**
 *  This class implements the Servlet filter interface.
 *  Its purpose is intercept and monitor the Web layer of
 *  a STEP application.<br />
 *  <br />
 *  It must be configured in the web.xml configuration file.<br />
 *  <br />
 */
public class PerfFilter implements Filter {

    //
    //  Filter
    //

    /** Filter configuration */
    private FilterConfig config = null;

    public void init(FilterConfig config) throws ServletException {
        // save a reference for filter config object
        this.config = config;
    }

    public void destroy() {
        config = null;
    }

    public void doFilter(ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        // BEFORE request
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.init();
        monitor.enter("filter");

        // invoke the next processor in the chain
        // (can be another filter or the web resource)
        chain.doFilter(request, response);

        // AFTER request
        monitor.exit("filter");
        monitor.dump(MonitorHelper.getDumpFile());
    }

}
