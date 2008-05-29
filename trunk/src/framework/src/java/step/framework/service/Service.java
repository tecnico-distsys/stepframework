package step.framework.service;

import step.framework.domain.DomainException;
import step.framework.exception.ServiceException;
import step.framework.extensions.ServiceInterceptorManager;

/**
 *  This is the Framework's base service.<br />
 *  All service classes should extend this class or one of its subclasses.<br />
 *  <br />
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
        this.txManager = TransactionManagerFactory.getDefaultTransactionManager();
        this.extManager = new ServiceInterceptorManager();
    }


    //
    // Service execution
    //

    /**
     *  The execute method is a <b>template method</b> for service execution.<br />
     *  It assures that a service's action is executed in a
     *  transaction context.<br />
     *  <br />
     *  It also defines extension points before and after the
     *  service's action.<br />
     *  <br />
     *  If any exception is throw during before, action or after
     *  the transaction is rolled back. If not, it's commited.<br />
     *  <br />
     *  The actual transactional behaviour can be redefined on
     *  different subclasses, as services can use different transaction
     *  managers and different transaction implementations.<br />
     *  <br />
     */
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
