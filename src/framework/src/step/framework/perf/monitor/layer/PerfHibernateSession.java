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
        //monitor.enter("hibernate.Session.beginTransaction");
        try {
            return PerfHibernateHelper.wrapTransaction(wrappedSession.beginTransaction());
        } finally {
            //monitor.exit("hibernate.Session.beginTransaction");
            monitor.exit("hibernate");
        }
    }

    public void cancelQuery() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.cancelQuery");
        try {
            wrappedSession.cancelQuery();
        } finally {
            //monitor.exit("hibernate.Session.cancelQuery");
            monitor.exit("hibernate");
        }
    }

    public void clear() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.clear");
        try {
            wrappedSession.clear();
        } finally {
            //monitor.exit("hibernate.Session.clear");
            monitor.exit("hibernate");
        }
    }

    public Connection close() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.close");
        try {
            return wrappedSession.close();
        } finally {
            //monitor.exit("hibernate.Session.close");
            monitor.exit("hibernate");
        }
    }

    @Deprecated public Connection connection() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.connection");
        try {
            return wrappedSession.connection();
        } finally {
            //monitor.exit("hibernate.Session.connection");
            monitor.exit("hibernate");
        }
    }

    public boolean contains(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.contains");
        try {
            return wrappedSession.contains(object);
        } finally {
            //monitor.exit("hibernate.Session.contains");
            monitor.exit("hibernate");
        }
    }

    public Criteria createCriteria(Class persistentClass) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.createCriteria");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedSession.createCriteria(persistentClass));
        } finally {
            //monitor.exit("hibernate.Session.createCriteria");
            monitor.exit("hibernate");
        }
    }

    public Criteria createCriteria(Class persistentClass, String alias) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.createCriteria");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedSession.createCriteria(persistentClass, alias));
        } finally {
            //monitor.exit("hibernate.Session.createCriteria");
            monitor.exit("hibernate");
        }
    }
    public Criteria createCriteria(String entityName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.createCriteria");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedSession.createCriteria(entityName));
        } finally {
            //monitor.exit("hibernate.Session.createCriteria");
            monitor.exit("hibernate");
        }
    }
    public Criteria createCriteria(String entityName, String alias) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.createCriteria");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedSession.createCriteria(entityName, alias));
        } finally {
            //monitor.exit("hibernate.Session.createCriteria");
            monitor.exit("hibernate");
        }
    }

    public Query createFilter(Object collection, String queryString) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.createFilter");
        try {
            return PerfHibernateHelper.wrapQuery(wrappedSession.createFilter(collection, queryString));
        } finally {
            //monitor.exit("hibernate.Session.createFilter");
            monitor.exit("hibernate");
        }
    }

    public Query createQuery(String queryString) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.createQuery");
        try {
            return PerfHibernateHelper.wrapQuery(wrappedSession.createQuery(queryString));
        } finally {
            //monitor.exit("hibernate.Session.createQuery");
            monitor.exit("hibernate");
        }
    }

    public SQLQuery createSQLQuery(String queryString) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.createSQLQuery");
        try {
            return PerfHibernateHelper.wrapSQLQuery(wrappedSession.createSQLQuery(queryString));
        } finally {
            //monitor.exit("hibernate.Session.createSQLQuery");
            monitor.exit("hibernate");
        }
    }

    public void delete(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.delete");
        try {
            wrappedSession.delete(object);
        } finally {
            //monitor.exit("hibernate.Session.delete");
            monitor.exit("hibernate");
        }
    }

    public void delete(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.delete");
        try {
            wrappedSession.delete(entityName, object);
        } finally {
            //monitor.exit("hibernate.Session.delete");
            monitor.exit("hibernate");
        }
    }

    public void disableFilter(String filterName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.disableFilter");
        try {
            wrappedSession.disableFilter(filterName);
        } finally {
            //monitor.exit("hibernate.Session.disableFilter");
            monitor.exit("hibernate");
        }
    }

    public Connection disconnect() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.disconnect");
        try {
            return PerfHibernateHelper.wrapConnection(wrappedSession.disconnect());
        } finally {
            //monitor.exit("hibernate.Session.disconnect");
            monitor.exit("hibernate");
        }
    }

    public void doWork(Work work) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.doWork");
        try {
            wrappedSession.doWork(work);
        } finally {
            //monitor.exit("hibernate.Session.doWork");
            monitor.exit("hibernate");
        }
    }

    public Filter enableFilter(String filterName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.enableFilter");
        try {
            return PerfHibernateHelper.wrapFilter(wrappedSession.enableFilter(filterName));
        } finally {
            //monitor.exit("hibernate.Session.enableFilter");
            monitor.exit("hibernate");
        }
    }

    public void evict(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.evict");
        try {
            wrappedSession.evict(object);
        } finally {
            //monitor.exit("hibernate.Session.evict");
            monitor.exit("hibernate");
        }
    }

    public void flush() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.flush");
        try {
            wrappedSession.flush();
        } finally {
            //monitor.exit("hibernate.Session.flush");
            monitor.exit("hibernate");
        }
    }

    public Object get(Class clazz, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.get");
        try {
            return wrappedSession.get(clazz, id);
        } finally {
            //monitor.exit("hibernate.Session.get");
            monitor.exit("hibernate");
        }
    }

    public Object get(Class clazz, Serializable id, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.get");
        try {
            return wrappedSession.get(clazz, id, lockMode);
        } finally {
            //monitor.exit("hibernate.Session.get");
            monitor.exit("hibernate");
        }
    }

    public Object get(String entityName, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.get");
        try {
            return wrappedSession.get(entityName, id);
        } finally {
            //monitor.exit("hibernate.Session.get");
            monitor.exit("hibernate");
        }
    }

    public Object get(String entityName, Serializable id, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.get");
        try {
            return wrappedSession.get(entityName, id, lockMode);
        } finally {
            //monitor.exit("hibernate.Session.get");
            monitor.exit("hibernate");
        }
    }

    public CacheMode getCacheMode() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getCacheMode");
        try {
            return wrappedSession.getCacheMode();
        } finally {
            //monitor.exit("hibernate.Session.getCacheMode");
            monitor.exit("hibernate");
        }
    }

    public LockMode getCurrentLockMode(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getCurrentLockMode");
        try {
            return wrappedSession.getCurrentLockMode(object);
        } finally {
            //monitor.exit("hibernate.Session.getCurrentLockMode");
            monitor.exit("hibernate");
        }
    }

    public Filter getEnabledFilter(String filterName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getEnabledFilter");
        try {
            return PerfHibernateHelper.wrapFilter(wrappedSession.getEnabledFilter(filterName));
        } finally {
            //monitor.exit("hibernate.Session.getEnabledFilter");
            monitor.exit("hibernate");
        }
    }

    public EntityMode getEntityMode() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getEntityMode");
        try {
            return wrappedSession.getEntityMode();
        } finally {
            //monitor.exit("hibernate.Session.getEntityMode");
            monitor.exit("hibernate");
        }
    }

    public String getEntityName(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getEntityName");
        try {
            return wrappedSession.getEntityName(object);
        } finally {
            //monitor.exit("hibernate.Session.getEntityName");
            monitor.exit("hibernate");
        }
    }

    public FlushMode getFlushMode() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getFlushMode");
        try {
            return wrappedSession.getFlushMode();
        } finally {
            //monitor.exit("hibernate.Session.getFlushMode");
            monitor.exit("hibernate");
        }
    }

    public Serializable getIdentifier(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getIdentifier");
        try {
            return wrappedSession.getIdentifier(object);
        } finally {
            //monitor.exit("hibernate.Session.getIdentifier");
            monitor.exit("hibernate");
        }
    }

    public Query getNamedQuery(String queryName) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getNamedQuery");
        try {
            return wrappedSession.getNamedQuery(queryName);
        } finally {
            //monitor.exit("hibernate.Session.getNamedQuery");
            monitor.exit("hibernate");
        }
    }

    public Session getSession(EntityMode entityMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getSession");
        try {
            return PerfHibernateHelper.wrapSession(wrappedSession.getSession(entityMode));
        } finally {
            //monitor.exit("hibernate.Session.getSession");
            monitor.exit("hibernate");
        }
    }

    public SessionFactory getSessionFactory() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getSessionFactory");
        try {
            return PerfHibernateHelper.wrapSessionFactory(wrappedSession.getSessionFactory());
        } finally {
            //monitor.exit("hibernate.Session.getSessionFactory");
            monitor.exit("hibernate");
        }
    }

    public SessionStatistics getStatistics() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getStatistics");
        try {
            return wrappedSession.getStatistics();
        } finally {
            //monitor.exit("hibernate.Session.getStatistics");
            monitor.exit("hibernate");
        }
    }

    public Transaction getTransaction() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.getTransaction");
        try {
            return PerfHibernateHelper.wrapTransaction(wrappedSession.getTransaction());
        } finally {
            //monitor.exit("hibernate.Session.getTransaction");
            monitor.exit("hibernate");
        }
    }

    public boolean isConnected() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.isConnected");
        try {
            return wrappedSession.isConnected();
        } finally {
            //monitor.exit("hibernate.Session.isConnected");
            monitor.exit("hibernate");
        }
    }

    public boolean isDirty() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.isDirty");
        try {
            return wrappedSession.isDirty();
        } finally {
            //monitor.exit("hibernate.Session.isDirty");
            monitor.exit("hibernate");
        }
    }

    public boolean isOpen() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.isOpen");
        try {
            return wrappedSession.isOpen();
        } finally {
            //monitor.exit("hibernate.Session.isOpen");
            monitor.exit("hibernate");
        }
    }

    public Object load(Class theClass, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.load");
        try {
            return wrappedSession.load(theClass, id);
        } finally {
            //monitor.exit("hibernate.Session.load");
            monitor.exit("hibernate");
        }
    }

    public Object load(Class theClass, Serializable id, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.load");
        try {
            return wrappedSession.load(theClass, id, lockMode);
        } finally {
            //monitor.exit("hibernate.Session.load");
            monitor.exit("hibernate");
        }
    }

    public void load(Object object, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.load");
        try {
            wrappedSession.load(object, id);
        } finally {
            //monitor.exit("hibernate.Session.load");
            monitor.exit("hibernate");
        }
    }

    public Object load(String entityName, Serializable id) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.load");
        try {
            return wrappedSession.load(entityName, id);
        } finally {
            //monitor.exit("hibernate.Session.load");
            monitor.exit("hibernate");
        }
    }

    public Object load(String entityName, Serializable id, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.load");
        try {
            return wrappedSession.load(entityName, id, lockMode);
        } finally {
            //monitor.exit("hibernate.Session.load");
            monitor.exit("hibernate");
        }
    }

    public void lock(Object object, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.lock");
        try {
            wrappedSession.lock(object, lockMode);
        } finally {
            //monitor.exit("hibernate.Session.lock");
            monitor.exit("hibernate");
        }
    }

    public void lock(String entityName, Object object, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.lock");
        try {
            wrappedSession.lock(entityName, object, lockMode);
        } finally {
            //monitor.exit("hibernate.Session.lock");
            monitor.exit("hibernate");
        }
    }

    public Object merge(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.merge");
        try {
            return wrappedSession.merge(object);
        } finally {
            //monitor.exit("hibernate.Session.merge");
            monitor.exit("hibernate");
        }
    }

    public Object merge(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.merge");
        try {
            return wrappedSession.merge(entityName, object);
        } finally {
            //monitor.exit("hibernate.Session.merge");
            monitor.exit("hibernate");
        }
    }

    public void persist(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.persist");
        try {
            wrappedSession.persist(object);
        } finally {
            //monitor.exit("hibernate.Session.persist");
            monitor.exit("hibernate");
        }
    }

    public void persist(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.persist");
        try {
            wrappedSession.persist(entityName, object);
        } finally {
            //monitor.exit("hibernate.Session.persist");
            monitor.exit("hibernate");
        }
    }

    @Deprecated public void reconnect() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.reconnect");
        try {
            wrappedSession.reconnect();
        } finally {
            //monitor.exit("hibernate.Session.reconnect");
            monitor.exit("hibernate");
        }
 }
    public void reconnect(Connection connection) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.reconnect");
        try {
            wrappedSession.reconnect(connection);
        } finally {
            //monitor.exit("hibernate.Session.reconnect");
            monitor.exit("hibernate");
        }
    }

    public void refresh(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.refresh");
        try {
            wrappedSession.refresh(object);
        } finally {
            //monitor.exit("hibernate.Session.refresh");
            monitor.exit("hibernate");
        }
    }

    public void refresh(Object object, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.refresh");
        try {
            wrappedSession.refresh(object, lockMode);
        } finally {
            //monitor.exit("hibernate.Session.refresh");
            monitor.exit("hibernate");
        }
    }

    public void replicate(Object object, ReplicationMode replicationMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.replicate");
        try {
            wrappedSession.replicate(object, replicationMode);
        } finally {
            //monitor.exit("hibernate.Session.replicate");
            monitor.exit("hibernate");
        }
    }

    public void replicate(String entityName, Object object, ReplicationMode replicationMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.replicate");
        try {
            wrappedSession.replicate(entityName, object, replicationMode);
        } finally {
            //monitor.exit("hibernate.Session.replicate");
            monitor.exit("hibernate");
        }
    }

    public Serializable save(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.save");
        try {
            return wrappedSession.save(object);
        } finally {
            //monitor.exit("hibernate.Session.save");
            monitor.exit("hibernate");
        }
    }

    public Serializable save(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.save");
        try {
            return wrappedSession.save(entityName, object);
        } finally {
            //monitor.exit("hibernate.Session.save");
            monitor.exit("hibernate");
        }
    }

    public void saveOrUpdate(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.saveOrUpdate");
        try {
            wrappedSession.saveOrUpdate(object);
        } finally {
            //monitor.exit("hibernate.Session.saveOrUpdate");
            monitor.exit("hibernate");
        }
    }

    public void saveOrUpdate(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.saveOrUpdate");
        try {
            wrappedSession.saveOrUpdate(entityName, object);
        } finally {
            //monitor.exit("hibernate.Session.saveOrUpdate");
            monitor.exit("hibernate");
        }
    }

    public void setCacheMode(CacheMode cacheMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.setCacheMode");
        try {
            wrappedSession.setCacheMode(cacheMode);
        } finally {
            //monitor.exit("hibernate.Session.setCacheMode");
            monitor.exit("hibernate");
        }
    }

    public void setFlushMode(FlushMode flushMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.setFlushMode");
        try {
            wrappedSession.setFlushMode(flushMode);
        } finally {
            //monitor.exit("hibernate.Session.setFlushMode");
            monitor.exit("hibernate");
        }
    }

    public void setReadOnly(Object entity, boolean readOnly) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.setReadOnly");
        try {
            wrappedSession.setReadOnly(entity, readOnly);
        } finally {
            //monitor.exit("hibernate.Session.setReadOnly");
            monitor.exit("hibernate");
        }
    }

    public void update(Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.update");
        try {
            wrappedSession.update(object);
        } finally {
            //monitor.exit("hibernate.Session.update");
            monitor.exit("hibernate");
        }
    }

    public void update(String entityName, Object object) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Session.update");
        try {
            wrappedSession.update(entityName, object);
        } finally {
            //monitor.exit("hibernate.Session.update");
            monitor.exit("hibernate");
        }
    }

}
