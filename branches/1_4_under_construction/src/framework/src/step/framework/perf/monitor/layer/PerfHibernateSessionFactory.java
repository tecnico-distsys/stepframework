package step.framework.perf.monitor.layer;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.Reference;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.*;
import org.hibernate.stat.Statistics;

import step.framework.perf.monitor.event.*;


/**
 *  This is a performance monitoring wrapping of the Hibernate session factory.
 */
public class PerfHibernateSessionFactory implements SessionFactory {

    private static final long serialVersionUID = 1L;

    private static SessionFactory instance;

    public static synchronized SessionFactory getInstance() {
        if (instance == null) {
            SessionFactory sessionFactoryToWrap = new AnnotationConfiguration().configure().buildSessionFactory();
            instance = new PerfHibernateSessionFactory(sessionFactoryToWrap);
        }
        return instance;
    }

    private SessionFactory wrappedSessionFactory;

    PerfHibernateSessionFactory(SessionFactory sessionFactoryToWrap) {
        wrappedSessionFactory = sessionFactoryToWrap;
    }


    /* org.hibernate.SessionFactory methods */
    public void close() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.close");
        try {
           wrappedSessionFactory.close();
        } finally {
            monitor.exit("hibernate.SessionFactory.close");
        }
    }

    @SuppressWarnings("unchecked") public void evict(Class persistentClass) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.evict");
        try {
            wrappedSessionFactory.evict(persistentClass);
        } finally {
            monitor.exit("hibernate.SessionFactory.evict");
        }
    }

    @SuppressWarnings("unchecked") public void evict(Class persistentClass, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.evict");
        try {
            wrappedSessionFactory.evict(persistentClass, id);
        } finally {
            monitor.exit("hibernate.SessionFactory.evict");
        }
    }

    public void evictCollection(String roleName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.evictCollection");
        try {
            wrappedSessionFactory.evictCollection(roleName);
        } finally {
            monitor.exit("hibernate.SessionFactory.evictCollection");
        }
    }

    public void evictCollection(String roleName, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.evictCollection");
        try {
            wrappedSessionFactory.evictCollection(roleName, id);
        } finally {
            monitor.exit("hibernate.SessionFactory.evictCollection");
        }
    }

    public void evictEntity(String entityName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.evictEntity");
        try {
            wrappedSessionFactory.evictEntity(entityName);
        } finally {
            monitor.exit("hibernate.SessionFactory.evictEntity");
        }
    }

    public void evictEntity(String entityName, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.evictEntity");
        try {
            wrappedSessionFactory.evictEntity(entityName, id);
        } finally {
            monitor.exit("hibernate.SessionFactory.evictEntity");
        }
    }

    public void evictQueries() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.evictQueries");
        try {
            wrappedSessionFactory.evictQueries();
        } finally {
            monitor.exit("hibernate.SessionFactory.evictQueries");
        }
    }

    public void evictQueries(String cacheRegion) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.evictQueries");
        try {
            wrappedSessionFactory.evictQueries(cacheRegion);
        } finally {
            monitor.exit("hibernate.SessionFactory.evictQueries");
        }
    }

    @SuppressWarnings("unchecked") public Map getAllClassMetadata() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getAllClassMetadata");
        try {
            return wrappedSessionFactory.getAllClassMetadata();
        } finally {
            monitor.exit("hibernate.SessionFactory.getAllClassMetadata");
        }
    }

    @SuppressWarnings("unchecked") public Map getAllCollectionMetadata() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getAllCollectionMetadata");
        try {
            return wrappedSessionFactory.getAllCollectionMetadata();
        } finally {
            monitor.exit("hibernate.SessionFactory.getAllCollectionMetadata");
        }
    }

    @SuppressWarnings("unchecked") public ClassMetadata getClassMetadata(Class persistentClass) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getClassMetadata");
        try {
            return wrappedSessionFactory.getClassMetadata(persistentClass);
        } finally {
            monitor.exit("hibernate.SessionFactory.getClassMetadata");
        }
    }

    public ClassMetadata getClassMetadata(String entityName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getClassMetadata");
        try {
            return wrappedSessionFactory.getClassMetadata(entityName);
        } finally {
            monitor.exit("hibernate.SessionFactory.getClassMetadata");
        }
    }

    public CollectionMetadata getCollectionMetadata(String roleName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getCollectionMetadata");
        try {
            return wrappedSessionFactory.getCollectionMetadata(roleName);
        } finally {
            monitor.exit("hibernate.SessionFactory.getCollectionMetadata");
        }
    }

    public org.hibernate.classic.Session getCurrentSession() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getCurrentSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.getCurrentSession();
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate.SessionFactory.getCurrentSession");
        }
    }

    @SuppressWarnings("unchecked") public Set getDefinedFilterNames() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getDefinedFilterNames");
        try {
            // TODO wrap filters in set?
            return wrappedSessionFactory.getDefinedFilterNames();
        } finally {
            monitor.exit("hibernate.SessionFactory.getDefinedFilterNames");
        }
    }

    public FilterDefinition getFilterDefinition(String filterName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getFilterDefinition");
        try {
            // TODO wrap filter definition?
            return wrappedSessionFactory.getFilterDefinition(filterName);
        } finally {
            monitor.exit("hibernate.SessionFactory.getFilterDefinition");
        }
    }

    public Statistics getStatistics() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getStatistics");
        try {
            return wrappedSessionFactory.getStatistics();
        } finally {
            monitor.exit("hibernate.SessionFactory.getStatistics");
        }
    }

    public boolean isClosed() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.isClosed");
        try {
            return wrappedSessionFactory.isClosed();
        } finally {
            monitor.exit("hibernate.SessionFactory.isClosed");
        }
    }

    public org.hibernate.classic.Session openSession() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.openSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession();
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate.SessionFactory.openSession");
        }
    }

    public org.hibernate.classic.Session openSession(Connection connection) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.openSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession(connection);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate.SessionFactory.openSession");
        }
    }

    public org.hibernate.classic.Session openSession(Connection connection, Interceptor interceptor) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.openSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession(connection, interceptor);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate.SessionFactory.openSession");
        }
    }

    public org.hibernate.classic.Session openSession(Interceptor interceptor) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.openSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession(interceptor);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate.SessionFactory.openSession");
        }
    }

    public StatelessSession openStatelessSession() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.openStatelessSession");
        try {
            // TODO return wrapped object
            return wrappedSessionFactory.openStatelessSession();
        } finally {
            monitor.exit("hibernate.SessionFactory.openStatelessSession");
        }
    }

    public StatelessSession openStatelessSession(Connection connection) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.openStatelessSession");
        try {
            // TODO return wrapped object
            return wrappedSessionFactory.openStatelessSession(connection);
        } finally {
            monitor.exit("hibernate.SessionFactory.openStatelessSession");
        }
    }

    public Reference getReference() throws javax.naming.NamingException {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.SessionFactory.getReference");
        try {
            return wrappedSessionFactory.getReference();
        } finally {
            monitor.exit("hibernate.SessionFactory.getReference");
        }
    }

}
