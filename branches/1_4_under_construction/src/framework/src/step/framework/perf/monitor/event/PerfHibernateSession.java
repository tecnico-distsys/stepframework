package step.framework.perf.monitor.event;

import java.io.Serializable;
import java.sql.Connection;
import java.util.*;

import org.hibernate.*;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;


/**
 *  This is a performance monitoring wrapping of the Hibernate session.
 */
public class PerfHibernateSession implements org.hibernate.classic.Session {

    private Session wrappedSession;

    PerfHibernateSession(Session sessionToWrap) {
        wrappedSession = sessionToWrap;
    }

    /* org.hibernate.classic.Session methods */
    @Deprecated public Query createSQLQuery(String sql, String[] returnAliases, Class[] returnClasses) { throw new UnsupportedOperationException(); }
    @Deprecated public Query createSQLQuery(String sql, String returnAlias, Class returnClass) { throw new UnsupportedOperationException(); }
    @Deprecated public int delete(String query) { throw new UnsupportedOperationException(); }
    @Deprecated public int delete(String query, Object[] values, Type[] types) { throw new UnsupportedOperationException(); }
    @Deprecated public int delete(String query, Object value, Type type) { throw new UnsupportedOperationException(); }
    @Deprecated public Collection filter(Object collection, String filter) { throw new UnsupportedOperationException(); }
    @Deprecated public Collection filter(Object collection, String filter, Object[] values, Type[] types) { throw new UnsupportedOperationException(); }
    @Deprecated public Collection filter(Object collection, String filter, Object value, Type type) { throw new UnsupportedOperationException(); }
    @Deprecated public List find(String query) { throw new UnsupportedOperationException(); }
    @Deprecated public List find(String query, Object[] values, Type[] types) { throw new UnsupportedOperationException(); }
    @Deprecated public List find(String query, Object value, Type type) { throw new UnsupportedOperationException(); }
    @Deprecated public Iterator iterate(String query) { throw new UnsupportedOperationException(); }
    @Deprecated public Iterator iterate(String query, Object[] values, Type[] types) { throw new UnsupportedOperationException(); }
    @Deprecated public Iterator iterate(String query, Object value, Type type) { throw new UnsupportedOperationException(); }
    @Deprecated public void save(Object object, Serializable id) { throw new UnsupportedOperationException(); }
    @Deprecated public void save(String entityName, Object object, Serializable id) { throw new UnsupportedOperationException(); }
    @Deprecated public Object saveOrUpdateCopy(Object object) { throw new UnsupportedOperationException(); }
    @Deprecated public Object saveOrUpdateCopy(Object object, Serializable id) { throw new UnsupportedOperationException(); }
    @Deprecated public Object saveOrUpdateCopy(String entityName, Object object) { throw new UnsupportedOperationException(); }
    @Deprecated public Object saveOrUpdateCopy(String entityName, Object object, Serializable id) { throw new UnsupportedOperationException(); }
    @Deprecated public void update(Object object, Serializable id) { throw new UnsupportedOperationException(); }
    @Deprecated public void update(String entityName, Object object, Serializable id) { throw new UnsupportedOperationException(); }


    /* org.hibernate.Session methods */
    public Transaction beginTransaction() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.beginTransaction");
        try {
            Transaction transaction = wrappedSession.beginTransaction();
            if (!(transaction instanceof PerfHibernateTransaction))
                transaction = new PerfHibernateTransaction(transaction);
            return transaction;
        } finally {
            monitor.event("exit-hibernate.Session.beginTransaction");
        }
    }

    public void cancelQuery() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.cancelQuery");
        try {
            wrappedSession.cancelQuery();
        } finally {
            monitor.event("exit-hibernate.Session.cancelQuery");
        }
    }

    public void clear() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.clear");
        try {
            wrappedSession.clear();
        } finally {
            monitor.event("exit-hibernate.Session.clear");
        }
    }

    public Connection close() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.close");
        try {
            return wrappedSession.close();
        } finally {
            monitor.event("exit-hibernate.Session.close");
        }
    }

    @Deprecated public Connection connection() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.connection");
        try {
            return wrappedSession.connection();
        } finally {
            monitor.event("exit-hibernate.Session.connection");
        }
    }

    public boolean contains(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.contains");
        try {
            return wrappedSession.contains(object);
        } finally {
            monitor.event("exit-hibernate.Session.contains");
        }
    }

    public Criteria createCriteria(Class persistentClass) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.createCriteria");
        try {
            // TODO wrap criteria?
            return wrappedSession.createCriteria(persistentClass);
        } finally {
            monitor.event("exit-hibernate.Session.createCriteria");
        }
    }

    public Criteria createCriteria(Class persistentClass, String alias) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.createCriteria");
        try {
            // TODO wrap criteria?
            return wrappedSession.createCriteria(persistentClass, alias);
        } finally {
            monitor.event("exit-hibernate.Session.createCriteria");
        }
    }
    public Criteria createCriteria(String entityName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.createCriteria");
        try {
            // TODO wrap criteria?
            return wrappedSession.createCriteria(entityName);
        } finally {
            monitor.event("exit-hibernate.Session.createCriteria");
        }
    }
    public Criteria createCriteria(String entityName, String alias) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.createCriteria");
        try {
            // TODO wrap criteria?
            return wrappedSession.createCriteria(entityName, alias);
        } finally {
            monitor.event("exit-hibernate.Session.createCriteria");
        }
    }

    public Query createFilter(Object collection, String queryString) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.createFilter");
        try {
            // TODO wrap query?
            return wrappedSession.createFilter(collection, queryString);
        } finally {
            monitor.event("exit-hibernate.Session.createFilter");
        }
    }

    public Query createQuery(String queryString) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.createQuery");
        try {
            // TODO wrap query?
            return wrappedSession.createQuery(queryString);
        } finally {
            monitor.event("exit-hibernate.Session.createQuery");
        }
    }

    public SQLQuery createSQLQuery(String queryString) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.createSQLQuery");
        try {
            // TODO wrap query?
            return wrappedSession.createSQLQuery(queryString);
        } finally {
            monitor.event("exit-hibernate.Session.createSQLQuery");
        }
    }

    public void delete(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.delete");
        try {
            wrappedSession.delete(object);
        } finally {
            monitor.event("exit-hibernate.Session.delete");
        }
    }

    public void delete(String entityName, Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.delete");
        try {
            wrappedSession.delete(entityName, object);
        } finally {
            monitor.event("exit-hibernate.Session.delete");
        }
    }

    public void disableFilter(String filterName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.disableFilter");
        try {
            wrappedSession.disableFilter(filterName);
        } finally {
            monitor.event("exit-hibernate.Session.disableFilter");
        }
    }

    public Connection disconnect() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.disconnect");
        try {
            return wrappedSession.disconnect();
        } finally {
            monitor.event("exit-hibernate.Session.disconnect");
        }
    }

    public void doWork(Work work) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.doWork");
        try {
            wrappedSession.doWork(work);
        } finally {
            monitor.event("exit-hibernate.Session.doWork");
        }
    }

    public Filter enableFilter(String filterName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.enableFilter");
        try {
            // TODO wrap filter?
            return wrappedSession.enableFilter(filterName);
        } finally {
            monitor.event("exit-hibernate.Session.enableFilter");
        }
    }

    public void evict(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.evict");
        try {
            wrappedSession.evict(object);
        } finally {
            monitor.event("exit-hibernate.Session.evict");
        }
    }

    public void flush() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.flush");
        try {
            wrappedSession.flush();
        } finally {
            monitor.event("exit-hibernate.Session.flush");
        }
    }

    public Object get(Class clazz, Serializable id) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.get");
        try {
            return wrappedSession.get(clazz, id);
        } finally {
            monitor.event("exit-hibernate.Session.get");
        }
    }

    public Object get(Class clazz, Serializable id, LockMode lockMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.get");
        try {
            return wrappedSession.get(clazz, id, lockMode);
        } finally {
            monitor.event("exit-hibernate.Session.get");
        }
    }

    public Object get(String entityName, Serializable id) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.get");
        try {
            return wrappedSession.get(entityName, id);
        } finally {
            monitor.event("exit-hibernate.Session.get");
        }
    }

    public Object get(String entityName, Serializable id, LockMode lockMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.get");
        try {
            return wrappedSession.get(entityName, id, lockMode);
        } finally {
            monitor.event("exit-hibernate.Session.get");
        }
    }

    public CacheMode getCacheMode() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getCacheMode");
        try {
            return wrappedSession.getCacheMode();
        } finally {
            monitor.event("exit-hibernate.Session.getCacheMode");
        }
    }

    public LockMode getCurrentLockMode(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getCurrentLockMode");
        try {
            return wrappedSession.getCurrentLockMode(object);
        } finally {
            monitor.event("exit-hibernate.Session.getCurrentLockMode");
        }
    }

    public Filter getEnabledFilter(String filterName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getEnabledFilter");
        try {
            // TODO wrap filter?
            return wrappedSession.getEnabledFilter(filterName);
        } finally {
            monitor.event("exit-hibernate.Session.getEnabledFilter");
        }
    }

    public EntityMode getEntityMode() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getEntityMode");
        try {
            return wrappedSession.getEntityMode();
        } finally {
            monitor.event("exit-hibernate.Session.getEntityMode");
        }
    }

    public String getEntityName(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getEntityName");
        try {
            return wrappedSession.getEntityName(object);
        } finally {
            monitor.event("exit-hibernate.Session.getEntityName");
        }
    }

    public FlushMode getFlushMode() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getFlushMode");
        try {
            return wrappedSession.getFlushMode();
        } finally {
            monitor.event("exit-hibernate.Session.getFlushMode");
        }
    }

    public Serializable getIdentifier(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getIdentifier");
        try {
            return wrappedSession.getIdentifier(object);
        } finally {
            monitor.event("exit-hibernate.Session.getIdentifier");
        }
    }

    public Query getNamedQuery(String queryName) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getNamedQuery");
        try {
            return wrappedSession.getNamedQuery(queryName);
        } finally {
            monitor.event("exit-hibernate.Session.getNamedQuery");
        }
    }

    public Session getSession(EntityMode entityMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getSession");
        try {
            Session session = wrappedSession.getSession(entityMode);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.event("exit-hibernate.Session.getSession");
        }
    }

    public SessionFactory getSessionFactory() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getSessionFactory");
        try {
            SessionFactory sessionFactory = wrappedSession.getSessionFactory();
            if (!(sessionFactory instanceof PerfHibernateSessionFactory))
                sessionFactory = new PerfHibernateSessionFactory(sessionFactory);
            return sessionFactory;
        } finally {
            monitor.event("exit-hibernate.Session.getSessionFactory");
        }
    }

    public SessionStatistics getStatistics() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getStatistics");
        try {
            return wrappedSession.getStatistics();
        } finally {
            monitor.event("exit-hibernate.Session.getStatistics");
        }
    }

    public Transaction getTransaction() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.getTransaction");
        try {
            Transaction transaction = wrappedSession.getTransaction();
            if (!(transaction instanceof PerfHibernateTransaction))
                transaction = new PerfHibernateTransaction(transaction);
            return transaction;
        } finally {
            monitor.event("exit-hibernate.Session.getTransaction");
        }
    }

    public boolean isConnected() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.isConnected");
        try {
            return wrappedSession.isConnected();
        } finally {
            monitor.event("exit-hibernate.Session.isConnected");
        }
    }

    public boolean isDirty() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.isDirty");
        try {
            return wrappedSession.isDirty();
        } finally {
            monitor.event("exit-hibernate.Session.isDirty");
        }
    }

    public boolean isOpen() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.isOpen");
        try {
            return wrappedSession.isOpen();
        } finally {
            monitor.event("exit-hibernate.Session.isOpen");
        }
    }

    public Object load(Class theClass, Serializable id) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.load");
        try {
            return wrappedSession.load(theClass, id);
        } finally {
            monitor.event("exit-hibernate.Session.load");
        }
    }

    public Object load(Class theClass, Serializable id, LockMode lockMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.load");
        try {
            return wrappedSession.load(theClass, id, lockMode);
        } finally {
            monitor.event("exit-hibernate.Session.load");
        }
    }

    public void load(Object object, Serializable id) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.load");
        try {
            wrappedSession.load(object, id);
        } finally {
            monitor.event("exit-hibernate.Session.load");
        }
    }

    public Object load(String entityName, Serializable id) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.load");
        try {
            return wrappedSession.load(entityName, id);
        } finally {
            monitor.event("exit-hibernate.Session.load");
        }
    }

    public Object load(String entityName, Serializable id, LockMode lockMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.load");
        try {
            return wrappedSession.load(entityName, id, lockMode);
        } finally {
            monitor.event("exit-hibernate.Session.load");
        }
    }

    public void lock(Object object, LockMode lockMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.lock");
        try {
            wrappedSession.lock(object, lockMode);
        } finally {
            monitor.event("exit-hibernate.Session.lock");
        }
    }

    public void lock(String entityName, Object object, LockMode lockMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.lock");
        try {
            wrappedSession.lock(entityName, object, lockMode);
        } finally {
            monitor.event("exit-hibernate.Session.lock");
        }
    }

    public Object merge(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.merge");
        try {
            return wrappedSession.merge(object);
        } finally {
            monitor.event("exit-hibernate.Session.merge");
        }
    }

    public Object merge(String entityName, Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.merge");
        try {
            return wrappedSession.merge(entityName, object);
        } finally {
            monitor.event("exit-hibernate.Session.merge");
        }
    }

    public void persist(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.persist");
        try {
            wrappedSession.persist(object);
        } finally {
            monitor.event("exit-hibernate.Session.persist");
        }
    }

    public void persist(String entityName, Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.persist");
        try {
            wrappedSession.persist(entityName, object);
        } finally {
            monitor.event("exit-hibernate.Session.persist");
        }
    }

    @Deprecated public void reconnect() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.reconnect");
        try {
            wrappedSession.reconnect();
        } finally {
            monitor.event("exit-hibernate.Session.reconnect");
        }
 }
    public void reconnect(Connection connection) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.reconnect");
        try {
            wrappedSession.reconnect(connection);
        } finally {
            monitor.event("exit-hibernate.Session.reconnect");
        }
    }

    public void refresh(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.refresh");
        try {
            wrappedSession.refresh(object);
        } finally {
            monitor.event("exit-hibernate.Session.refresh");
        }
    }

    public void refresh(Object object, LockMode lockMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.refresh");
        try {
            wrappedSession.refresh(object, lockMode);
        } finally {
            monitor.event("exit-hibernate.Session.refresh");
        }
    }

    public void replicate(Object object, ReplicationMode replicationMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.replicate");
        try {
            wrappedSession.replicate(object, replicationMode);
        } finally {
            monitor.event("exit-hibernate.Session.replicate");
        }
    }

    public void replicate(String entityName, Object object, ReplicationMode replicationMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.replicate");
        try {
            wrappedSession.replicate(entityName, object, replicationMode);
        } finally {
            monitor.event("exit-hibernate.Session.replicate");
        }
    }

    public Serializable save(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.save");
        try {
            return wrappedSession.save(object);
        } finally {
            monitor.event("exit-hibernate.Session.save");
        }
    }

    public Serializable save(String entityName, Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.save");
        try {
            return wrappedSession.save(entityName, object);
        } finally {
            monitor.event("exit-hibernate.Session.save");
        }
    }

    public void saveOrUpdate(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.saveOrUpdate");
        try {
            wrappedSession.saveOrUpdate(object);
        } finally {
            monitor.event("exit-hibernate.Session.saveOrUpdate");
        }
    }

    public void saveOrUpdate(String entityName, Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.saveOrUpdate");
        try {
            wrappedSession.saveOrUpdate(entityName, object);
        } finally {
            monitor.event("exit-hibernate.Session.saveOrUpdate");
        }
    }

    public void setCacheMode(CacheMode cacheMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.setCacheMode");
        try {
            wrappedSession.setCacheMode(cacheMode);
        } finally {
            monitor.event("exit-hibernate.Session.setCacheMode");
        }
    }

    public void setFlushMode(FlushMode flushMode) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.setFlushMode");
        try {
            wrappedSession.setFlushMode(flushMode);
        } finally {
            monitor.event("exit-hibernate.Session.setFlushMode");
        }
    }

    public void setReadOnly(Object entity, boolean readOnly) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.setReadOnly");
        try {
            wrappedSession.setReadOnly(entity, readOnly);
        } finally {
            monitor.event("exit-hibernate.Session.setReadOnly");
        }
    }

    public void update(Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.update");
        try {
            wrappedSession.update(object);
        } finally {
            monitor.event("exit-hibernate.Session.update");
        }
    }

    public void update(String entityName, Object object) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Session.update");
        try {
            wrappedSession.update(entityName, object);
        } finally {
            monitor.event("exit-hibernate.Session.update");
        }
    }

}
