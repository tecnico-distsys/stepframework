package step.framework.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.extensions.ExtensionRepository;
import step.framework.jarloader.JarException;
import step.framework.jarloader.JarLoader;


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
        
        log.debug("Disable JAX-WS option to capture stack trace in SOAP fault messages");
        System.getProperties().setProperty("com.sun.xml.ws.fault.SOAPFaultBuilder.disableCaptureStackTrace", "false");
        
        try
        {
        	JarLoader.load(ExtensionRepository.DEFAULT_PATH);
            log.info("Extensions JarLoader initialized on path \"" + ExtensionRepository.DEFAULT_PATH + "\"");
        }
        catch(JarException e)
        {
            log.info("Extensions JarLoader failed to initialize on path \"" + ExtensionRepository.DEFAULT_PATH + "\"", e);
        }
    }

    /** Web application destroyed */
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Framework terminating...");
    }

}
