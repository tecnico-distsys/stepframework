package step.framework.persistence.mock;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.Reference;

import org.hibernate.*;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.*;
import org.hibernate.stat.Statistics;


/**
 *  This is a mock implementation of the Hibernate session factory.<br />
 *  <br />
 *  Read more about <a href="http://en.wikipedia.org/wiki/Mock_object">Mock objects</a>.
 */
public class MockSessionFactory implements SessionFactory {

    private static final long serialVersionUID = 1L;

    private static MockSessionFactory instance;

    private MockSessionFactory() {}

    public static synchronized MockSessionFactory getInstance() {
        if (instance == null) {
            return new MockSessionFactory();
        }
        return instance;
    }

    /* org.hibernate.SessionFactory methods */
    public void close() { throw new UnsupportedOperationException(); }
    @SuppressWarnings("unchecked") public void evict(Class persistentClass) { throw new UnsupportedOperationException(); }
    @SuppressWarnings("unchecked") public void evict(Class persistentClass, Serializable id) { throw new UnsupportedOperationException(); }
    public void evictCollection(String roleName) { throw new UnsupportedOperationException(); }
    public void evictCollection(String roleName, Serializable id) { throw new UnsupportedOperationException(); }
    public void evictEntity(String entityName) { throw new UnsupportedOperationException(); }
    public void evictEntity(String entityName, Serializable id) { throw new UnsupportedOperationException(); }
    public void evictQueries() { throw new UnsupportedOperationException(); }
    public void evictQueries(String cacheRegion) { throw new UnsupportedOperationException(); }
    @SuppressWarnings("unchecked") public Map getAllClassMetadata() { throw new UnsupportedOperationException(); }
    @SuppressWarnings("unchecked") public Map getAllCollectionMetadata() { throw new UnsupportedOperationException(); }
    @SuppressWarnings("unchecked") public ClassMetadata getClassMetadata(Class persistentClass) { throw new UnsupportedOperationException(); }
    public ClassMetadata getClassMetadata(String entityName) { throw new UnsupportedOperationException(); }
    public CollectionMetadata getCollectionMetadata(String roleName) { throw new UnsupportedOperationException(); }
    public Session getCurrentSession() { return MockSession.getInstance(); }
    @SuppressWarnings("unchecked") public Set getDefinedFilterNames() { throw new UnsupportedOperationException(); }
    public FilterDefinition getFilterDefinition(String filterName) { throw new UnsupportedOperationException(); }
    public Statistics getStatistics() { throw new UnsupportedOperationException(); }
    public boolean isClosed() { throw new UnsupportedOperationException(); }
    public Session openSession() { throw new UnsupportedOperationException(); }
    public Session openSession(Connection connection) { throw new UnsupportedOperationException(); }
    public Session openSession(Connection connection, Interceptor interceptor) { throw new UnsupportedOperationException(); }
    public Session openSession(Interceptor interceptor) { throw new UnsupportedOperationException(); }
    public StatelessSession openStatelessSession() { throw new UnsupportedOperationException(); }
    public StatelessSession openStatelessSession(Connection connection) { throw new UnsupportedOperationException(); }
    public Reference getReference() { throw new UnsupportedOperationException(); }

}
