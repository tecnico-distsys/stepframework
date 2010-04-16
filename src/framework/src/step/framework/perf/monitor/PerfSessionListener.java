package step.framework.perf.monitor;

import javax.servlet.http.*;

import org.apache.commons.logging.*;


/**
 *  This class implements the Servlet Session Listener interface.
 *  Its purpose is intercept HTTP session creations and destructions of
 *  a STEP application.
 */
public class PerfSessionListener implements HttpSessionListener {

    /** Logging */
    private static Log log = LogFactory.getLog(PerfSessionListener.class);


    //
    //  SessionListener
    //

    public void sessionCreated(HttpSessionEvent hse) {
        log.trace("sessionCreated");
    }

    public void sessionDestroyed(HttpSessionEvent hse) {
        log.trace("sessionDestroyed");
    }

}
