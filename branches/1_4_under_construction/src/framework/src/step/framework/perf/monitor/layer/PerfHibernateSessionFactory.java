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
        monitor.enter("hibernate");
        try {
           wrappedSessionFactory.close();
        } finally {
            monitor.exit("hibernate");
        }
    }

    @SuppressWarnings("unchecked") public void evict(Class persistentClass) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSessionFactory.evict(persistentClass);
        } finally {
            monitor.exit("hibernate");
        }
    }

    @SuppressWarnings("unchecked") public void evict(Class persistentClass, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSessionFactory.evict(persistentClass, id);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void evictCollection(String roleName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSessionFactory.evictCollection(roleName);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void evictCollection(String roleName, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSessionFactory.evictCollection(roleName, id);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void evictEntity(String entityName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSessionFactory.evictEntity(entityName);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void evictEntity(String entityName, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSessionFactory.evictEntity(entityName, id);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void evictQueries() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSessionFactory.evictQueries();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void evictQueries(String cacheRegion) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSessionFactory.evictQueries(cacheRegion);
        } finally {
            monitor.exit("hibernate");
        }
    }

    @SuppressWarnings("unchecked") public Map getAllClassMetadata() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSessionFactory.getAllClassMetadata();
        } finally {
            monitor.exit("hibernate");
        }
    }

    @SuppressWarnings("unchecked") public Map getAllCollectionMetadata() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSessionFactory.getAllCollectionMetadata();
        } finally {
            monitor.exit("hibernate");
        }
    }

    @SuppressWarnings("unchecked") public ClassMetadata getClassMetadata(Class persistentClass) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSessionFactory.getClassMetadata(persistentClass);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public ClassMetadata getClassMetadata(String entityName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSessionFactory.getClassMetadata(entityName);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public CollectionMetadata getCollectionMetadata(String roleName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSessionFactory.getCollectionMetadata(roleName);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public org.hibernate.classic.Session getCurrentSession() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.getCurrentSession();
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate");
        }
    }

    @SuppressWarnings("unchecked") public Set getDefinedFilterNames() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap filters in set?
            return wrappedSessionFactory.getDefinedFilterNames();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public FilterDefinition getFilterDefinition(String filterName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSessionFactory.getFilterDefinition(filterName);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Statistics getStatistics() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSessionFactory.getStatistics();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public boolean isClosed() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSessionFactory.isClosed();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public org.hibernate.classic.Session openSession() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession();
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate");
        }
    }

    public org.hibernate.classic.Session openSession(Connection connection) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession(connection);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate");
        }
    }

    public org.hibernate.classic.Session openSession(Connection connection, Interceptor interceptor) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession(connection, interceptor);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate");
        }
    }

    public org.hibernate.classic.Session openSession(Interceptor interceptor) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession(interceptor);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate");
        }
    }

    public StatelessSession openStatelessSession() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO return wrapped object
            return wrappedSessionFactory.openStatelessSession();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public StatelessSession openStatelessSession(Connection connection) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO return wrapped object
            return wrappedSessionFactory.openStatelessSession(connection);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Reference getReference() throws javax.naming.NamingException {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO return wrapped object ?
            return wrappedSessionFactory.getReference();
        } finally {
            monitor.exit("hibernate");
        }
    }

}
