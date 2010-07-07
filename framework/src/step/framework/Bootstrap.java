package step.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.extensions.ExtensionEngine;
import step.framework.extensions.ExtensionEngineException;
import step.framework.persistence.Persistence;
import step.framework.persistence.PersistenceException;

public class Bootstrap {
    /** Logging */
    private static Log log = LogFactory.getLog(Bootstrap.class);

	public static void init() {
		log.info("Initializing STEPframework...");

		log.trace("Initializing persistence support...");
		try {
			Persistence.init();
		} catch (PersistenceException e) {
			log.warn("Persistence support initialization failed", e);
		}

		log.trace("Initializing extension engine");
		ExtensionEngine engine = ExtensionEngine.getInstance();
		try {
			engine.init();
		} catch(ExtensionEngineException e) {
			log.warn("Extensions engine init failed", e);
		}
		log.trace("Extensions engine " + (engine.isEnabled() ? "enabled" : "disabled"));

        log.debug("Disable JAX-WS option to capture stack trace in SOAP fault messages");
        System.getProperties().setProperty("com.sun.xml.ws.fault.SOAPFaultBuilder.disableCaptureStackTrace", "false");
       
		log.info("STEPframework successfully initialized.");
	}

	public static void destroy() {
		log.info("STEPframework terminating...");
		
		log.trace("Destroying extension engine");
		ExtensionEngine engine = ExtensionEngine.getInstance();
		try {
			engine.destroy();
		} catch(ExtensionEngineException e) {
			log.debug("Extensions engine destroy failed", e);
		}
		
		log.info("STEPframework successfully terminated.");
	}
}