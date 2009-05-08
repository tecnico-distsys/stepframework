package step.framework.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.Criteria;
import org.hibernate.Session;

import step.framework.persistence.PersistenceUtil;

/** Superclasse of all domain classes. */
@MappedSuperclass
public abstract class DomainObject implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 
     * Unique identifier.
     */
    @SuppressWarnings("unused")
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    /**
     * Object version number.  
     * Necessary for the optimistic policy for concurrency control
     */
    @SuppressWarnings("unused")
    @Version
    private Long objVersion;
    
    public void save() {
        Session session = PersistenceUtil.getSessionFactory().getCurrentSession();
        session.save(this);
    }

    public void delete() {
        Session session = PersistenceUtil.getSessionFactory().getCurrentSession();
        session.delete(this);
    }

    protected static <T> T loadSingleton(Class<T> singletonClass) {
        T result = null;

        Session session = PersistenceUtil.getSessionFactory().getCurrentSession();
        Criteria criteria = session.createCriteria(singletonClass);
        result = singletonClass.cast(criteria.uniqueResult());
        return result;
    }

}
