package step.framework.perf.monitor.layer;

import java.util.List;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.ResultTransformer;


/**
 *  This is a performance monitoring wrapping of the Hibernate session.
 */
public class PerfHibernateCriteria implements Criteria {

    private Criteria wrappedCriteria;

    PerfHibernateCriteria(Criteria criteriaToWrap) {
        wrappedCriteria = criteriaToWrap;
    }

    /* org.hibernate.Criteria methods */
    public Criteria add(Criterion criterion) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.add");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.add(criterion));
        } finally {
            //monitor.exit("hibernate.Criteria.add");
            monitor.exit("hibernate");
        }
    }

    public Criteria addOrder(Order order) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.addOrder");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.addOrder(order));
        } finally {
            //monitor.exit("hibernate.Criteria.addOrder");
            monitor.exit("hibernate");
        }
    }

    public Criteria createAlias(String associationPath, String alias) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.createAlias");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.createAlias(associationPath, alias));
        } finally {
            //monitor.exit("hibernate.Criteria.createAlias");
            monitor.exit("hibernate");
        }
    }

    public Criteria createAlias(String associationPath, String alias, int joinType) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.createAlias");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.createAlias(associationPath, alias, joinType));
        } finally {
            //monitor.exit("hibernate.Criteria.createAlias");
            monitor.exit("hibernate");
        }
    }

    public Criteria createCriteria(String associationPath) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.createCriteria");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.createCriteria(associationPath));
        } finally {
            //monitor.exit("hibernate.Criteria.createCriteria");
            monitor.exit("hibernate");
        }
    }

    public Criteria createCriteria(String associationPath, int joinType) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.createCriteria");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.createCriteria(associationPath, joinType));
        } finally {
            //monitor.exit("hibernate.Criteria.createCriteria");
            monitor.exit("hibernate");
        }
    }

    public Criteria createCriteria(String associationPath, String alias) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.createCriteria");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.createCriteria(associationPath, alias));
        } finally {
            //monitor.exit("hibernate.Criteria.createCriteria");
            monitor.exit("hibernate");
        }
    }

    public Criteria createCriteria(String associationPath, String alias, int joinType) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.createCriteria");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.createCriteria(associationPath, alias, joinType));
        } finally {
            //monitor.exit("hibernate.Criteria.createCriteria");
            monitor.exit("hibernate");
        }
    }

    public String getAlias() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.getAlias");
        try {
            return wrappedCriteria.getAlias();
        } finally {
            //monitor.exit("hibernate.Criteria.getAlias");
            monitor.exit("hibernate");
        }
    }

    @SuppressWarnings("unchecked")
    public List list() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.list");
        try {
            return wrappedCriteria.list();
        } finally {
            //monitor.exit("hibernate.Criteria.list");
            monitor.exit("hibernate");
        }
    }

    public ScrollableResults scroll() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.scroll");
        try {
            return PerfHibernateHelper.wrapScrollableResults(wrappedCriteria.scroll());
        } finally {
            //monitor.exit("hibernate.Criteria.scroll");
            monitor.exit("hibernate");
        }
    }

    public ScrollableResults scroll(ScrollMode scrollMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.scroll");
        try {
            return PerfHibernateHelper.wrapScrollableResults(wrappedCriteria.scroll(scrollMode));
        } finally {
            //monitor.exit("hibernate.Criteria.scroll");
            monitor.exit("hibernate");
        }
    }

    public Criteria setCacheable(boolean cacheable) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setCacheable");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setCacheable(cacheable));
        } finally {
            //monitor.exit("hibernate.Criteria.setCacheable");
            monitor.exit("hibernate");
        }
    }

    public Criteria setCacheMode(CacheMode cacheMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setCacheMode");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setCacheMode(cacheMode));
        } finally {
            //monitor.exit("hibernate.Criteria.setCacheMode");
            monitor.exit("hibernate");
        }
    }

    public Criteria setCacheRegion(String cacheRegion) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setCacheRegion");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setCacheRegion(cacheRegion));
        } finally {
            //monitor.exit("hibernate.Criteria.setCacheRegion");
            monitor.exit("hibernate");
        }
    }

    public Criteria setComment(String comment) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setComment");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setComment(comment));
        } finally {
            //monitor.exit("hibernate.Criteria.setComment");
            monitor.exit("hibernate");
        }
    }

    public Criteria setFetchMode(String associationPath, FetchMode mode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setFetchMode");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setFetchMode(associationPath, mode));
        } finally {
            //monitor.exit("hibernate.Criteria.setFetchMode");
            monitor.exit("hibernate");
        }
    }

    public Criteria setFetchSize(int fetchSize) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setFetchSize");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setFetchSize(fetchSize));
        } finally {
            //monitor.exit("hibernate.Criteria.setFetchSize");
            monitor.exit("hibernate");
        }
    }

    public Criteria setFirstResult(int firstResult) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setFirstResult");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setFirstResult(firstResult));
        } finally {
            //monitor.exit("hibernate.Criteria.setFirstResult");
            monitor.exit("hibernate");
        }
    }

    public Criteria setFlushMode(FlushMode flushMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setFlushMode");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setFlushMode(flushMode));
        } finally {
            //monitor.exit("hibernate.Criteria.setFlushMode");
            monitor.exit("hibernate");
        }
    }

    public Criteria setLockMode(LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setLockMode");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setLockMode(lockMode));
        } finally {
            //monitor.exit("hibernate.Criteria.setLockMode");
            monitor.exit("hibernate");
        }
    }

    public Criteria setLockMode(String alias, LockMode lockMode) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setLockMode");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setLockMode(alias, lockMode));
        } finally {
            //monitor.exit("hibernate.Criteria.setLockMode");
            monitor.exit("hibernate");
        }
    }

    public Criteria setProjection(Projection projection) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setProjection");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setProjection(projection));
        } finally {
            //monitor.exit("hibernate.Criteria.setProjection");
            monitor.exit("hibernate");
        }
    }

    public Criteria setMaxResults(int maxResults) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setMaxResults");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setMaxResults(maxResults));
        } finally {
            //monitor.exit("hibernate.Criteria.setMaxResults");
            monitor.exit("hibernate");
        }
    }

    public Criteria setResultTransformer(ResultTransformer resultTransformer) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setResultTransformer");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setResultTransformer(resultTransformer));
        } finally {
            //monitor.exit("hibernate.Criteria.setResultTransformer");
            monitor.exit("hibernate");
        }
    }

    public Criteria setTimeout(int timeout) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.setTimeout");
        try {
            return PerfHibernateHelper.wrapCriteria(wrappedCriteria.setTimeout(timeout));
        } finally {
            //monitor.exit("hibernate.Criteria.setTimeout");
            monitor.exit("hibernate");
        }
    }

    @SuppressWarnings("unchecked")
    public Object uniqueResult() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Criteria.uniqueResult");
        try {
            return wrappedCriteria.uniqueResult();
        } finally {
            //monitor.exit("hibernate.Criteria.uniqueResult");
            monitor.exit("hibernate");
        }
    }

}
