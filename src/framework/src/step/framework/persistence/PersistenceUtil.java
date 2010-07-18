package step.framework.persistence;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import step.framework.persistence.mock.MockSessionFactory;


/**
 *  This class is the entry point for persistence support.
 *  It provides access to a Hibernate Session Factory (default) or
 *  to a Mock Session Factory.<br />
 *  The implementation is specified using a System property.
 *  If the property is undefined, the default implementation is used.<br />
 *  <br />
 */
public class PersistenceUtil {
    /** Persistence factory system property name */
    public static final String PERSISTENCE_FACTORY_PROPERTY_NAME = "step.framework.persistence.factory";

    /** Persistence factory system property value for Hibernate persistence */
    public static final String PERSISTENCE_FACTORY_HIBERNATE = "hibernate";

    /** Persistence factory system property value for Mock persistence */
    public static final String PERSISTENCE_FACTORY_MOCK = "mock";

    /** Persistence factory system property value for Perf monitoring of persistence */
    public static final String PERSISTENCE_FACTORY_PERF_EVENT = "PerfMonEvent";

    /** Persistence factory system property value for Perf monitoring of persistence */
    public static final String PERSISTENCE_FACTORY_PERF_LAYER = "PerfMonLayer";

    /** Session factory instance holder */
    private static final SessionFactory sessionFactory;

    static {

        Log log = LogFactory.getLog(PersistenceUtil.class);

        try {
            // access System property
            Properties props = System.getProperties();
            String factory = props.getProperty(PERSISTENCE_FACTORY_PROPERTY_NAME);

            if (factory != null && factory.trim().equalsIgnoreCase(PERSISTENCE_FACTORY_MOCK)) {
                log.info("Setting up mock persistence...");
                sessionFactory = MockSessionFactory.getInstance();
            } else if (factory != null && factory.trim().equalsIgnoreCase(PERSISTENCE_FACTORY_PERF_EVENT)) {
                log.info("Setting up persistence with performance monitoring for each event...");
                sessionFactory = step.framework.perf.monitor.event.PerfHibernateSessionFactory.getInstance();
            } else if (factory != null && factory.trim().equalsIgnoreCase(PERSISTENCE_FACTORY_PERF_LAYER)) {
                log.info("Setting up persistence with performance monitoring for each layer...");
                sessionFactory = step.framework.perf.monitor.layer.PerfHibernateSessionFactory.getInstance();
            } else {
                // By default (and when the property does not have another value) we use the Hibernate SessionFactory

                // Create the SessionFactory from hibernate.cfg.xml
                log.info("Setting up hibernate persistence...");
                sessionFactory = new AnnotationConfiguration()
                    .configure()
                    .buildSessionFactory();
            }

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            StringBuilder logMessage = new StringBuilder();
            logMessage.append("\n==================================================\n");
            logMessage.append("Initial SessionFactory creation failed.\n");

            logMessage.append("Error messages: ");
            Throwable t = ex;
            while(t != null) {
                logMessage.append(t.getMessage());
                logMessage.append("\n");
                t = t.getCause();
            }

            logMessage.append("Please check that:\n");
            logMessage.append("- hibernate.cfg.xml exists and is syntactically correct\n");
            logMessage.append("- the database is well configured and the tables have been correctly created\n");
            logMessage.append("==================================================");

            log.fatal(logMessage, ex);
            throw new ExceptionInInitializerError(logMessage.toString());
        }
    }

    /** Obtain a reference to a session factory */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
