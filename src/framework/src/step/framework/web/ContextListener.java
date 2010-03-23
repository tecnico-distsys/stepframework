package step.framework.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.extensions.ExtensionEngine;
import step.framework.extensions.ExtensionEngineException;


/**
 *  When web.xml is properly configured,
 *  an instance of this class is invoked when the web application is
 *  initialized and destroyed.
 *
 *  Performs extension engine initialization and destruction.
 */
public class ContextListener implements ServletContextListener {

    /** Logging */
    private Log log = LogFactory.getLog(ContextListener.class);

    /** Web application initialized */
    public void contextInitialized(ServletContextEvent sce) {
        // write startup message
        log.info("Framework initializing...");

        log.trace("Initializing extension engine");
        ExtensionEngine engine = ExtensionEngine.getInstance();
        boolean extEnabled = false;
        try {
            engine.init();
        } catch(ExtensionEngineException e) {
            log.warn("Extensions engine init failed", e);
        }
        log.trace("Extensions engine " + (engine.isEnabled() ? "enabled" : "disabled"));
    }

    /** Web application destroyed */
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Framework terminating...");

        log.trace("Destroying extension engine");
        ExtensionEngine engine = ExtensionEngine.getInstance();
        try {
            engine.destroy();
        } catch(ExtensionEngineException e) {
            log.debug("Extensions engine destroy failed", e);
        }
    }

}
