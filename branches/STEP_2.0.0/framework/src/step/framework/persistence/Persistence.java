package step.framework.persistence;

import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import step.framework.config.ConfigUtil;
import step.framework.exception.InitializationError;

/**
 *  This class is the entry point for Fénix Framework persistence support.
 *  It instantiates the Fénix Framework with the adequate data for persistence (default) or
 *  for mock persistence (used for domain logic testing).<br />
 *  The implementation is specified using a System property.
 *  If the property is undefined, the default implementation is used.<br />
 *  <br />
 */
public class Persistence { 
	/** Default persistence properties file resource path */
	private static final String DEFAULT_PERSISTENCE_RESOURCE_PATH = "/persistence.properties";

	/** All persistence properties start with this value */
	private static final String PERSISTENCE_PROPERTY_NAME_PREFIX = "persistence";

	/** This property enables (true) or disables persistence support */
	private static final String PERSISTENCE_ENABLED_PROPERTY_NAME = PERSISTENCE_PROPERTY_NAME_PREFIX + ".enabled";

	/** Logging */
	private static Log log = LogFactory.getLog(Persistence.class);
	
	/** Persistence support status **/
	private static boolean enabled = false;
	
	/**
	 *  Initialize the Fénix Framework persistence support assuming the default persistence
	 *  configuration resource path.<br />
	 *  <br />
	 *  @throws PersistenceException when initialization failed
	 */
	public static void init() throws PersistenceException {
		// begin Fenix Framework initialisation
		try {
			Properties properties = ConfigUtil.getResourceAsProperties(DEFAULT_PERSISTENCE_RESOURCE_PATH);
			if(properties == null) {
				log.trace("Persistence configuration file not found");
				throw new PersistenceException("Persistence configuration file (" + DEFAULT_PERSISTENCE_RESOURCE_PATH + ")");
			}
			
			// if configuration file states nothing about persistence requirements, assume enabled.
			String enabledValue = properties.getProperty(PERSISTENCE_ENABLED_PROPERTY_NAME);
			enabled = (enabledValue == null) ? true : Boolean.parseBoolean(enabledValue);
			if (!enabled) {
				log.info("Persistence support not required (" + PERSISTENCE_ENABLED_PROPERTY_NAME + "=false)");
				return;
			}
			
			Config config = new Config() {
				private static final String PERSISTENCE_DOMAINMODEL_PROPERTY_NAME = PERSISTENCE_PROPERTY_NAME_PREFIX + ".domainModelPath";
				private static final String PERSISTENCE_DBALIAS_PROPERTY_NAME = PERSISTENCE_PROPERTY_NAME_PREFIX + ".dbAlias";
				private static final String PERSISTENCE_USERNAME_PROPERTY_NAME = PERSISTENCE_PROPERTY_NAME_PREFIX + ".dbUsername";
				private static final String PERSISTENCE_PASSWORD_PROPERTY_NAME = PERSISTENCE_PROPERTY_NAME_PREFIX + ".dbPassword";
				private static final String PERSISTENCE_ROOTCLASS_PROPERTY_NAME = PERSISTENCE_PROPERTY_NAME_PREFIX + ".rootClass";
				{
					Properties properties = ConfigUtil.getResourceAsProperties(DEFAULT_PERSISTENCE_RESOURCE_PATH);                	

// JORGE: irrelevant, as it has already been tested above and will never reach this point if the file does not exist    				
//					if(properties == null) {
//					log.trace("Persistence configuration file not found");
//					throw new PersistenceException("Persistence configuration file (" + DEFAULT_PERSISTENCE_RESOURCE_PATH + ")");
//				}
					if(log.isTraceEnabled()) {
						log.trace("Persistence configuration properties:");
						Set<Object> keySet = properties.keySet();
						for(Object key : keySet)
							log.trace(key + "=" + properties.get(key));
					}
					
					domainModelPath = properties.getProperty(PERSISTENCE_DOMAINMODEL_PROPERTY_NAME);		// "/fears.dml";
					dbAlias = properties.getProperty(PERSISTENCE_DBALIAS_PROPERTY_NAME);					// "//localhost:3306/fears"; 
					dbUsername= properties.getProperty(PERSISTENCE_USERNAME_PROPERTY_NAME);					// "fears";
					dbPassword = properties.getProperty(PERSISTENCE_PASSWORD_PROPERTY_NAME); 				// "";
					rootClass = Class.forName(properties.getProperty(PERSISTENCE_ROOTCLASS_PROPERTY_NAME)); // FearsApp.class;
					updateRepositoryStructureIfNeeded = true;
				}
			};

			log.debug("Initializing Fenix Framework persistence support");
			FenixFramework.initialize(config);
			log.debug("Fenix Framework persistence support successfully initialized");
		} catch(Throwable t) {
			enabled = false;
			log.debug("Caught Throwable when trying to initialize Fenix Framework");
			log.debug(t);
			log.trace("Throwable details", t);
			log.trace("Throw exception with nested cause");
			throw new PersistenceException("Failed to initialize persistence", t);
		} finally {
			log.info("Persistence support " + (enabled ? "enabled" : "disabled"));
// JORGE: since the Fenix Framework cannot be disabled, anything that tries to disable the
//		  persistence support will have to abort the STEPframework initialization process
			if (!enabled)
				throw new InitializationError("Persistence support initialization failed");
		}
		// end persistence initialisation
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T extends DomainObject> T getRoot() {
		return (T) FenixFramework.getRoot();
	}
}