package step.framework.perf.monitor.layer;

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
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            Transaction transaction = wrappedSession.beginTransaction();
            if (!(transaction instanceof PerfHibernateTransaction))
                transaction = new PerfHibernateTransaction(transaction);
            return transaction;
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void cancelQuery() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.cancelQuery();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void clear() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.clear();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Connection close() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.close();
        } finally {
            monitor.exit("hibernate");
        }
    }

    @Deprecated public Connection connection() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.connection();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public boolean contains(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.contains(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Criteria createCriteria(Class persistentClass) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap criteria?
            return wrappedSession.createCriteria(persistentClass);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Criteria createCriteria(Class persistentClass, String alias) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap criteria?
            return wrappedSession.createCriteria(persistentClass, alias);
        } finally {
            monitor.exit("hibernate");
        }
    }
    public Criteria createCriteria(String entityName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap criteria?
            return wrappedSession.createCriteria(entityName);
        } finally {
            monitor.exit("hibernate");
        }
    }
    public Criteria createCriteria(String entityName, String alias) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap criteria?
            return wrappedSession.createCriteria(entityName, alias);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Query createFilter(Object collection, String queryString) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap query?
            return wrappedSession.createFilter(collection, queryString);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Query createQuery(String queryString) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap query?
            return wrappedSession.createQuery(queryString);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public SQLQuery createSQLQuery(String queryString) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap query?
            return wrappedSession.createSQLQuery(queryString);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void delete(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.delete(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void delete(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.delete(entityName, object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void disableFilter(String filterName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.disableFilter(filterName);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Connection disconnect() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.disconnect();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void doWork(Work work) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.doWork(work);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Filter enableFilter(String filterName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap filter?
            return wrappedSession.enableFilter(filterName);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void evict(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.evict(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void flush() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.flush();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object get(Class clazz, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.get(clazz, id);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object get(Class clazz, Serializable id, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.get(clazz, id, lockMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object get(String entityName, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.get(entityName, id);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object get(String entityName, Serializable id, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.get(entityName, id, lockMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public CacheMode getCacheMode() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.getCacheMode();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public LockMode getCurrentLockMode(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.getCurrentLockMode(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Filter getEnabledFilter(String filterName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            // TODO wrap filter?
            return wrappedSession.getEnabledFilter(filterName);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public EntityMode getEntityMode() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.getEntityMode();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public String getEntityName(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.getEntityName(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public FlushMode getFlushMode() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.getFlushMode();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Serializable getIdentifier(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.getIdentifier(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Query getNamedQuery(String queryName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.getNamedQuery(queryName);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Session getSession(EntityMode entityMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            Session session = wrappedSession.getSession(entityMode);
            if (!(session instanceof PerfHibernateSession))
                session = new PerfHibernateSession(session);
            return session;
        } finally {
            monitor.exit("hibernate");
        }
    }

    public SessionFactory getSessionFactory() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            SessionFactory sessionFactory = wrappedSession.getSessionFactory();
            if (!(sessionFactory instanceof PerfHibernateSessionFactory))
                sessionFactory = new PerfHibernateSessionFactory(sessionFactory);
            return sessionFactory;
        } finally {
            monitor.exit("hibernate");
        }
    }

    public SessionStatistics getStatistics() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.getStatistics();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Transaction getTransaction() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            Transaction transaction = wrappedSession.getTransaction();
            if (!(transaction instanceof PerfHibernateTransaction))
                transaction = new PerfHibernateTransaction(transaction);
            return transaction;
        } finally {
            monitor.exit("hibernate");
        }
    }

    public boolean isConnected() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.isConnected();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public boolean isDirty() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.isDirty();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public boolean isOpen() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.isOpen();
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object load(Class theClass, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.load(theClass, id);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object load(Class theClass, Serializable id, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.load(theClass, id, lockMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void load(Object object, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.load(object, id);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object load(String entityName, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.load(entityName, id);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object load(String entityName, Serializable id, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.load(entityName, id, lockMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void lock(Object object, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.lock(object, lockMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void lock(String entityName, Object object, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.lock(entityName, object, lockMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object merge(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.merge(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Object merge(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.merge(entityName, object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void persist(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.persist(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void persist(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.persist(entityName, object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    @Deprecated public void reconnect() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.reconnect();
        } finally {
            monitor.exit("hibernate");
        }
 }
    public void reconnect(Connection connection) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.reconnect(connection);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void refresh(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.refresh(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void refresh(Object object, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.refresh(object, lockMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void replicate(Object object, ReplicationMode replicationMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.replicate(object, replicationMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void replicate(String entityName, Object object, ReplicationMode replicationMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.replicate(entityName, object, replicationMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Serializable save(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.save(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public Serializable save(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            return wrappedSession.save(entityName, object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void saveOrUpdate(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.saveOrUpdate(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void saveOrUpdate(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.saveOrUpdate(entityName, object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void setCacheMode(CacheMode cacheMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.setCacheMode(cacheMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void setFlushMode(FlushMode flushMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.setFlushMode(flushMode);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void setReadOnly(Object entity, boolean readOnly) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.setReadOnly(entity, readOnly);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void update(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.update(object);
        } finally {
            monitor.exit("hibernate");
        }
    }

    public void update(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        try {
            wrappedSession.update(entityName, object);
        } finally {
            monitor.exit("hibernate");
        }
    }

}
