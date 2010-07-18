package step.framework.perf.monitor.event;

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
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.close");
        try {
           wrappedSessionFactory.close();
        } finally {
            monitor.event("exit-hibernate.SessionFactory.close");
        }
    }

    @SuppressWarnings("unchecked") public void evict(Class persistentClass) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.evict");
        try {
            wrappedSessionFactory.evict(persistentClass);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.evict");
        }
    }

    @SuppressWarnings("unchecked") public void evict(Class persistentClass, Serializable id) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.evict");
        try {
            wrappedSessionFactory.evict(persistentClass, id);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.evict");
        }
    }

    public void evictCollection(String roleName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.evictCollection");
        try {
            wrappedSessionFactory.evictCollection(roleName);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.evictCollection");
        }
    }

    public void evictCollection(String roleName, Serializable id) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.evictCollection");
        try {
            wrappedSessionFactory.evictCollection(roleName, id);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.evictCollection");
        }
    }

    public void evictEntity(String entityName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.evictEntity");
        try {
            wrappedSessionFactory.evictEntity(entityName);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.evictEntity");
        }
    }

    public void evictEntity(String entityName, Serializable id) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.evictEntity");
        try {
            wrappedSessionFactory.evictEntity(entityName, id);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.evictEntity");
        }
    }

    public void evictQueries() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.evictQueries");
        try {
            wrappedSessionFactory.evictQueries();
        } finally {
            monitor.event("exit-hibernate.SessionFactory.evictQueries");
        }
    }

    public void evictQueries(String cacheRegion) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.evictQueries");
        try {
            wrappedSessionFactory.evictQueries(cacheRegion);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.evictQueries");
        }
    }

    @SuppressWarnings("unchecked") public Map getAllClassMetadata() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getAllClassMetadata");
        try {
            return wrappedSessionFactory.getAllClassMetadata();
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getAllClassMetadata");
        }
    }

    @SuppressWarnings("unchecked") public Map getAllCollectionMetadata() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getAllCollectionMetadata");
        try {
            return wrappedSessionFactory.getAllCollectionMetadata();
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getAllCollectionMetadata");
        }
    }

    @SuppressWarnings("unchecked") public ClassMetadata getClassMetadata(Class persistentClass) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getClassMetadata");
        try {
            return wrappedSessionFactory.getClassMetadata(persistentClass);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getClassMetadata");
        }
    }

    public ClassMetadata getClassMetadata(String entityName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getClassMetadata");
        try {
            return wrappedSessionFactory.getClassMetadata(entityName);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getClassMetadata");
        }
    }

    public CollectionMetadata getCollectionMetadata(String roleName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getCollectionMetadata");
        try {
            return wrappedSessionFactory.getCollectionMetadata(roleName);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getCollectionMetadata");
        }
    }

    public org.hibernate.classic.Session getCurrentSession() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getCurrentSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.getCurrentSession();
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getCurrentSession");
        }
    }

    @SuppressWarnings("unchecked") public Set getDefinedFilterNames() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getDefinedFilterNames");
        try {
            // TODO wrap filters in set?
            return wrappedSessionFactory.getDefinedFilterNames();
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getDefinedFilterNames");
        }
    }

    public FilterDefinition getFilterDefinition(String filterName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getFilterDefinition");
        try {
            return wrappedSessionFactory.getFilterDefinition(filterName);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getFilterDefinition");
        }
    }

    public Statistics getStatistics() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getStatistics");
        try {
            return wrappedSessionFactory.getStatistics();
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getStatistics");
        }
    }

    public boolean isClosed() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.isClosed");
        try {
            return wrappedSessionFactory.isClosed();
        } finally {
            monitor.event("exit-hibernate.SessionFactory.isClosed");
        }
    }

    public org.hibernate.classic.Session openSession() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.openSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession();
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.event("exit-hibernate.SessionFactory.openSession");
        }
    }

    public org.hibernate.classic.Session openSession(Connection connection) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.openSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession(connection);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.event("exit-hibernate.SessionFactory.openSession");
        }
    }

    public org.hibernate.classic.Session openSession(Connection connection, Interceptor interceptor) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.openSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession(connection, interceptor);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.event("exit-hibernate.SessionFactory.openSession");
        }
    }

    public org.hibernate.classic.Session openSession(Interceptor interceptor) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.openSession");
        try {
            org.hibernate.classic.Session session = wrappedSessionFactory.openSession(interceptor);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.event("exit-hibernate.SessionFactory.openSession");
        }
    }

    public StatelessSession openStatelessSession() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.openStatelessSession");
        try {
            // TODO return wrapped object
            return wrappedSessionFactory.openStatelessSession();
        } finally {
            monitor.event("exit-hibernate.SessionFactory.openStatelessSession");
        }
    }

    public StatelessSession openStatelessSession(Connection connection) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.openStatelessSession");
        try {
            // TODO return wrapped object
            return wrappedSessionFactory.openStatelessSession(connection);
        } finally {
            monitor.event("exit-hibernate.SessionFactory.openStatelessSession");
        }
    }

    public Reference getReference() throws javax.naming.NamingException {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.SessionFactory.getReference");
        try {
            // TODO return wrapped object ?
            return wrappedSessionFactory.getReference();
        } finally {
            monitor.event("exit-hibernate.SessionFactory.getReference");
        }
    }

}
