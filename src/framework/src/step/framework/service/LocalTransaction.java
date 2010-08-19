package step.framework.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import step.framework.context.ThreadContext;
import step.framework.exception.TransactionException;
import step.framework.persistence.PersistenceUtil;

/**
 *  A local transaction manages the local persistence's layer
 *  transaction mechanisms.<br />
 *  In the current implementation, it manages an Hibernate transaction.<br />
 *  <br />
 *  Local transaction <b>nest</b>, meaning that when a new transaction is
 *  created when there is already a local transaction underway,
 *  the current transaction is joined instead of creating a new transaction.<br />
 *  <br />
 *  Subclasses of local transaction can also use the nesting
 *  level accessor methods to influence their own extended behaviour.<br />
 *  <br />
 *  In the current implementation, the nesting is kept in a thread local
 *  context property.<br />
 *  <br />
 */
public class LocalTransaction implements Transaction {

    //
    //  Nesting property handling
    //

    /** Nesting property name */
    private static final String NESTING_PROPERTY = "step.framework.service.txNesting";

    /** Access the nesting property's value */
    protected Integer getNesting() {
      return (Integer) ThreadContext.getInstance().get(NESTING_PROPERTY) ;
    }

    /** set the nesting property's value */
    protected void setNesting(int nesting) {
        ThreadContext.getInstance().put(NESTING_PROPERTY, nesting);
    }

    /** remove the nesting property */
    protected void resetNesting() {
        ThreadContext.getInstance().remove(NESTING_PROPERTY);
    }


    //
    // Members
    //
    private org.hibernate.Transaction tx;
    private Session session;

    protected Log log;


    //
    // Constructors
    //

    public LocalTransaction() {
        this.session = PersistenceUtil.getSessionFactory().getCurrentSession();
        this.log = LogFactory.getLog(this.getClass());
    }


    //
    //  Transaction implementation
    //

    /**
     *  If it's the first transaction beginning in the current thread then
     *  start a persistence layer transaction.
     *  Else, just update the nesting level.
     */
    public void begin() throws TransactionException {
        Integer nesting = getNesting();
        try {
            if (nesting == null) {
                try {
                    this.tx = session.beginTransaction();
                } catch (HibernateException ex) {
                    throw new TransactionException(ex);
                }
                nesting = 0;
            }
            setNesting(nesting+1);

        } finally {
            if (log.isTraceEnabled()) {
                log.trace("After begin transaction");
                log.trace(NESTING_PROPERTY + ":=" + getNesting());
            }
        }
    }

    /**
     *  If it's the last transaction commiting in the current thread then
     *  commit the persistence layer transaction.
     *  Else, just update the nesting level.
     */
    public void commit() throws TransactionException {
        Integer nesting = getNesting();
        if (nesting == null) {
            String message = "Can't commit because there is no active transaction.";
            log.warn(message);
            throw new IllegalStateException(message);
        }

        boolean txCommited = false;
        try {
            if (nesting == 1) {
                try {
                    this.tx.commit();
                    txCommited = true;
                } catch (HibernateException ex) {
                    throw new TransactionException(ex);
                } finally {
                    // if the commit fails we should keep the current nesting
                    // to allow the (expected) following rollback operation to succeed.
                    if (txCommited) {
                        resetNesting();
                    }
                }
            } else {
                setNesting(nesting-1);
                txCommited = true;
            }

        } finally {
            if (log.isTraceEnabled()) {
                log.trace("After commit transaction" + (txCommited ? " (ok)" : " (failed)"));
                log.trace(NESTING_PROPERTY + ":=" + getNesting());
            }
        }
    }

    /**
     *  If it's the last transaction rolling back in the current thread then
     *  rollback the persistence layer transaction.
     *  Else, just update the nesting level.
     */
    public void rollback() throws TransactionException {
        Integer nesting = getNesting();
        if (nesting == null) {
            String message = "Can't rollback because there is no active transaction.";
            log.warn(message);
            throw new IllegalStateException(message);
        }

        try {
            if (nesting == 1) {
                try {
                    this.tx.rollback();
                } catch (HibernateException ex) {
                    throw new TransactionException(ex);
                } finally {
                    resetNesting();
                }
            } else {
                setNesting(nesting-1);
            }

        } finally {
            if (log.isTraceEnabled()) {
                log.trace("After rollback transaction");
                log.trace(NESTING_PROPERTY + ":=" + getNesting());
            }
        }
    }

}
