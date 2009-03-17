package step.framework.persistence;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.LinkedHashSet;


import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;

public class MockSession implements Session {
    private static final long serialVersionUID = 1L;
    
    private static Session instance;

    /**
     * Stores all objects touched in this session
     **/ 
    private Map<Class, LinkedHashSet<Object>> persistentObjects;

    private MockSession() {
        persistentObjects = new HashMap<Class, LinkedHashSet<Object>>();
    }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new MockSession();
        }
        return instance;
    }

    /**
     * Returns the first object of the parameter class (or sub-class) touched in this session
     **/
    public <T> T getObject(Class<T> clazz) {
        T result = null;
	Iterator instanceIterator;

	if (persistentObjects.containsKey(clazz)) {
	    instanceIterator = persistentObjects.get(clazz).iterator();

	    if (instanceIterator.hasNext()) {
	        result = clazz.cast(instanceIterator.next());
	    }
        }
        return result;
    }

    private void addObject(Object object) {
        Class currentClass = null;
	LinkedHashSet<Object> objectSet;

	if (object != null) currentClass = object.getClass();

	while (currentClass != null) {
	    if (persistentObjects.containsKey(currentClass)) {
		objectSet = persistentObjects.get(currentClass);
	    } else {
		objectSet = new LinkedHashSet<Object>();
		persistentObjects.put(currentClass, objectSet);
	    }	  
	    objectSet.add(object);
	    currentClass = currentClass.getSuperclass();
	}
    }

    private void removeObject(Object object) {
        Class currentClass = null;
	LinkedHashSet<Object> objectSet;

	if (object != null && persistentObjects.containsKey(object.getClass())) currentClass = object.getClass();

	while (currentClass != null) {
	    objectSet = persistentObjects.get(currentClass);
	    objectSet.remove(object);
	    currentClass = currentClass.getSuperclass();
	}
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
        return MockTransaction.getInstance();
    }
    public void cancelQuery() { throw new UnsupportedOperationException(); }
    public void clear() { throw new UnsupportedOperationException(); }
    public Connection close() { throw new UnsupportedOperationException(); }
    @Deprecated public Connection connection() { throw new UnsupportedOperationException(); }
    public boolean contains(Object object) { throw new UnsupportedOperationException(); }
    public Criteria createCriteria(Class persistentClass) {
        return new MockCriteria(this, persistentClass);
    }
    public Criteria createCriteria(Class persistentClass, String alias) { throw new UnsupportedOperationException(); }
    public Criteria createCriteria(String entityName) { throw new UnsupportedOperationException(); }
    public Criteria createCriteria(String entityName, String alias) { throw new UnsupportedOperationException(); }
    public Query createFilter(Object collection, String queryString) { throw new UnsupportedOperationException(); }
    public Query createQuery(String queryString) { throw new UnsupportedOperationException(); }
    public SQLQuery createSQLQuery(String queryString) { throw new UnsupportedOperationException(); }
    public void delete(Object object) {
        removeObject(object);
    }
    public void delete(String entityName, Object object) { throw new UnsupportedOperationException(); }
    public void disableFilter(String filterName) { throw new UnsupportedOperationException(); }
    public Connection disconnect() { throw new UnsupportedOperationException(); }
    public Filter enableFilter(String filterName) { throw new UnsupportedOperationException(); }
    public void evict(Object object) { throw new UnsupportedOperationException(); }
    public void flush() { throw new UnsupportedOperationException(); }
    public Object get(Class clazz, Serializable id) { throw new UnsupportedOperationException(); }
    public Object get(Class clazz, Serializable id, LockMode lockMode) { throw new UnsupportedOperationException(); }
    public Object get(String entityName, Serializable id) { throw new UnsupportedOperationException(); }
    public Object get(String entityName, Serializable id, LockMode lockMode) { throw new UnsupportedOperationException(); }
    public CacheMode getCacheMode() { throw new UnsupportedOperationException(); }
    public LockMode getCurrentLockMode(Object object) { throw new UnsupportedOperationException(); }
    public Filter getEnabledFilter(String filterName) { throw new UnsupportedOperationException(); }
    public EntityMode getEntityMode() { throw new UnsupportedOperationException(); }
    public String getEntityName(Object object) { throw new UnsupportedOperationException(); }
    public FlushMode getFlushMode() { throw new UnsupportedOperationException(); }
    public Serializable getIdentifier(Object object) { throw new UnsupportedOperationException(); }
    public Query getNamedQuery(String queryName) { throw new UnsupportedOperationException(); }
    public Session getSession(EntityMode entityMode) { throw new UnsupportedOperationException(); }
    public SessionFactory getSessionFactory() { throw new UnsupportedOperationException(); }
    public SessionStatistics getStatistics() { throw new UnsupportedOperationException(); }
    public Transaction getTransaction() { throw new UnsupportedOperationException(); }
    public boolean isConnected() { throw new UnsupportedOperationException(); }
    public boolean isDirty() { throw new UnsupportedOperationException(); }
    public boolean isOpen() { throw new UnsupportedOperationException(); }
    public Object load(Class theClass, Serializable id) { throw new UnsupportedOperationException(); }
    public Object load(Class theClass, Serializable id, LockMode lockMode) { throw new UnsupportedOperationException(); }
    public void load(Object object, Serializable id) { throw new UnsupportedOperationException(); }
    public Object load(String entityName, Serializable id) { throw new UnsupportedOperationException(); }
    public Object load(String entityName, Serializable id, LockMode lockMode) { throw new UnsupportedOperationException(); }
    public void lock(Object object, LockMode lockMode) { throw new UnsupportedOperationException(); }
    public void lock(String entityName, Object object, LockMode lockMode) { throw new UnsupportedOperationException(); }
    public Object merge(Object object) { throw new UnsupportedOperationException(); }
    public Object merge(String entityName, Object object) { throw new UnsupportedOperationException(); }
    public void persist(Object object) { throw new UnsupportedOperationException(); }
    public void persist(String entityName, Object object) { throw new UnsupportedOperationException(); }
    @Deprecated public void reconnect() { throw new UnsupportedOperationException(); }
    public void reconnect(Connection connection) { throw new UnsupportedOperationException(); }
    public void refresh(Object object) { throw new UnsupportedOperationException(); }
    public void refresh(Object object, LockMode lockMode) { throw new UnsupportedOperationException(); }
    public void replicate(Object object, ReplicationMode replicationMode) { throw new UnsupportedOperationException(); }
    public void replicate(String entityName, Object object, ReplicationMode replicationMode) { throw new UnsupportedOperationException(); }
    public Serializable save(Object object) {
        addObject(object);
	return null;
    }
    public Serializable save(String entityName, Object object) { throw new UnsupportedOperationException(); }
    public void saveOrUpdate(Object object) { throw new UnsupportedOperationException(); }
    public void saveOrUpdate(String entityName, Object object) { throw new UnsupportedOperationException(); }
    public void setCacheMode(CacheMode cacheMode) { throw new UnsupportedOperationException(); }
    public void setFlushMode(FlushMode flushMode) { throw new UnsupportedOperationException(); }
    public void setReadOnly(Object entity, boolean readOnly) { throw new UnsupportedOperationException(); }
    public void update(Object object) { throw new UnsupportedOperationException(); }
    public void update(String entityName, Object object) { throw new UnsupportedOperationException(); }


}
