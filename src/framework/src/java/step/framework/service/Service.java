package step.framework.service;

import step.framework.domain.DomainException;
import step.framework.exception.ServiceException;
import step.framework.extensions.ServiceInterceptorManager;

/**
 *  Base service.
 *
 */
public abstract class Service<R> {

    //
    // Members
    //

    /** Return value */
    protected R returnValue;

    /** Transaction manager */
    protected TransactionManager txManager;

    /** Extensions manager */
    protected ServiceInterceptorManager extManager;


    //
    // Constructors
    //

    public Service() {
        this.txManager = TransactionManagerFactory.getLocalTransactionManager();
        this.extManager = new ServiceInterceptorManager();
    }


    //
    // Service execution
    //

    public final R execute() throws DomainException, ServiceException {
    Transaction tx = null;
        boolean txCommited = false;

        try {
            tx = txManager.newTransaction();
            tx.begin();
            before();
            returnValue = action();
            after();
            tx.commit();
            txCommited = true;
            return returnValue;
        } finally {
            if (!txCommited && tx != null) { tx.rollback(); }
        }

    }

    /** This is the method subclasses should override to implement a service */
    protected abstract R action() throws DomainException;


    //
    // Extensions interception
    //

    protected final void before() throws DomainException, ServiceException {
        extManager.interceptBeforeService(this);
    }

    protected final void after() throws DomainException, ServiceException {
        extManager.interceptAfterService(this);
    }

}
