package step.framework.persistence;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class PersistenceUtil {
    /** Location of persistence properties file. */
    public static final String PERSISTENCE_PROPERTIES = "/persistence.properties";
    
    /** defines the SessionFactory to use */
    public static final String PERSISTENCE_FACTORY = "persistence.factory";
    public static final String PERSISTENCE_FACTORY_HIBERNATE = "hibernate";
    public static final String PERSISTENCE_FACTORY_MOCK = "mock";

    private static final SessionFactory sessionFactory;

    static {
        Log log = LogFactory.getLog(PersistenceUtil.class);
        
        try {
            Properties props = new Properties();
            props.load(PersistenceUtil.class.getResourceAsStream(PERSISTENCE_PROPERTIES));
            String factory = props.getProperty(PERSISTENCE_FACTORY);

            if (factory != null && factory.trim().equals(PERSISTENCE_FACTORY_MOCK)) {
                sessionFactory = MockSessionFactory.getInstance();
            } else {
                // By default (and when the property doesn't have the "mock" value) we use the Hibernate SessionFactory
                
                // Create the SessionFactory from hibernate.cfg.xml
                sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
            }
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
	    StringBuilder logMessage = new StringBuilder("\n==================================================\n");
	    logMessage.append("Initial SessionFactory creation failed.\n");
	    logMessage.append("Please check that:\n");
	    logMessage.append("- hibernate.cfg.xml exists and is syntactically correct\n");
	    logMessage.append("- the database is well configured and the tables have been correctly created\n");
	    logMessage.append("==================================================");

            log.fatal(logMessage, ex);
            throw new ExceptionInInitializerError(logMessage.toString());
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
