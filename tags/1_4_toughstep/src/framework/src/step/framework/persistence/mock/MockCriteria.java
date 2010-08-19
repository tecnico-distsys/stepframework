package step.framework.persistence.mock;

import java.util.List;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.ResultTransformer;


/**
 *  This is a mock implementation of the Hibernate criteria.<br />
 *  <br />
 *  Read more about <a href="http://en.wikipedia.org/wiki/Mock_object">Mock objects</a>.
 */
public class MockCriteria implements Criteria {
    private static final long serialVersionUID = 1L;

    private MockSession session;
    private Class clazz;

    public MockCriteria(MockSession session, Class clazz) {
        this.session = session;
	this.clazz = clazz;
    }

    /* org.hibernate.Criteria methods */
    public Criteria add(Criterion criterion) { throw new UnsupportedOperationException(); }
    public Criteria addOrder(Order order) { throw new UnsupportedOperationException(); }
    public Criteria createAlias(String associationPath, String alias) { throw new UnsupportedOperationException(); }
    public Criteria createAlias(String associationPath, String alias, int joinType) { throw new UnsupportedOperationException(); }
    public Criteria createCriteria(String associationPath) { throw new UnsupportedOperationException(); }
    public Criteria createCriteria(String associationPath, int joinType) { throw new UnsupportedOperationException(); }
    public Criteria createCriteria(String associationPath, String alias) { throw new UnsupportedOperationException(); }
    public Criteria createCriteria(String associationPath, String alias, int joinType) { throw new UnsupportedOperationException(); }
    public String getAlias() { throw new UnsupportedOperationException(); }
    @SuppressWarnings("unchecked") public List list() { throw new UnsupportedOperationException(); }
    public ScrollableResults scroll() { throw new UnsupportedOperationException(); }
    public ScrollableResults scroll(ScrollMode scrollMode) { throw new UnsupportedOperationException(); }
    public Criteria setCacheable(boolean cacheable) { throw new UnsupportedOperationException(); }
    public Criteria setCacheMode(CacheMode cacheMode) { throw new UnsupportedOperationException(); }
    public Criteria setCacheRegion(String cacheRegion) { throw new UnsupportedOperationException(); }
    public Criteria setComment(String comment) { throw new UnsupportedOperationException(); }
    public Criteria setFetchMode(String associationPath, FetchMode mode) { throw new UnsupportedOperationException(); }
    public Criteria setFetchSize(int fetchSize) { throw new UnsupportedOperationException(); }
    public Criteria setFirstResult(int firstResult) { throw new UnsupportedOperationException(); }
    public Criteria setFlushMode(FlushMode flushMode) { throw new UnsupportedOperationException(); }
    public Criteria setLockMode(LockMode lockMode) { throw new UnsupportedOperationException(); }
    public Criteria setLockMode(String alias, LockMode lockMode) { throw new UnsupportedOperationException(); }
    public Criteria setProjection(Projection projection) { throw new UnsupportedOperationException(); }
    public Criteria setMaxResults(int maxResults) { throw new UnsupportedOperationException(); }
    public Criteria setResultTransformer(ResultTransformer resultTransformer) { throw new UnsupportedOperationException(); }
    public Criteria setTimeout(int timeout) { throw new UnsupportedOperationException(); }

    @SuppressWarnings("unchecked")
    public Object uniqueResult() {
        return session.getObject(clazz);
    }
}