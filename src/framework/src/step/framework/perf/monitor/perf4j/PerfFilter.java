package step.framework.perf.monitor.perf4j;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.logging.*;

import org.perf4j.*;



/**
 *  This class implements the Servlet filter interface.
 *  Its purpose is intercept and monitor the Web layer of
 *  a STEP application.<br />
 *  <br />
 *  It must be configured in the web.xml configuration file.<br />
 *  <br />
 */
public class PerfFilter implements Filter {

    /** Logging */
    private static Log log = LogFactory.getLog(PerfFilter.class);


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

        log.trace("doFilter");
        if(log.isTraceEnabled()) {
            if (request instanceof HttpServletRequest) {
                String uri = ((HttpServletRequest)request).getRequestURI();
                log.trace(String.format("Request URI %s", uri));
            }
        }

        log.trace("BEFORE request");
        StopWatchHelper.getThreadStopWatch("filter").start("filter");

        // invoke the next processor in the chain
        // (can be another filter or the web resource)
        chain.doFilter(request, response);

        log.trace("AFTER request");
        StopWatchHelper.getThreadStopWatch("filter").stop("filter");
        StopWatchHelper.deleteAllThreadStopWatches();

    }

}
