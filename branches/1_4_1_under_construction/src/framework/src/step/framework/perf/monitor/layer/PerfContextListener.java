package step.framework.perf.monitor.layer;

import java.util.*;
import javax.servlet.*;
import org.apache.commons.logging.*;

import step.framework.persistence.PersistenceUtil;


/**
 *  This class implements the Servlet Context Listener interface.
 */
public class PerfContextListener implements ServletContextListener {

    //
    //  ContextListener
    //

    public void contextInitialized(ServletContextEvent sce) {
        // modify implementation of persistence session factory
        System.setProperty(PersistenceUtil.PERSISTENCE_FACTORY_PROPERTY_NAME, PersistenceUtil.PERSISTENCE_FACTORY_PERF_LAYER);
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }

}
