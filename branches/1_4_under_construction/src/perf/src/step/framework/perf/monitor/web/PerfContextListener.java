package step.framework.perf.monitor.web;

import java.util.*;

import javax.servlet.*;

import org.apache.commons.logging.*;


/**
 *  This class implements the Servlet Context Listener interface.
 *  Its purpose is intercept STEP application context initialization and destruction.
 */
public class PerfContextListener implements ServletContextListener {

    /** Logging */
    private static Log log = LogFactory.getLog(PerfContextListener.class);

    //
    //  ContextListener
    //

    public void contextInitialized(ServletContextEvent sce) {
        log.trace("contextInitialized");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        log.trace("contextDestroyed");
    }

}
