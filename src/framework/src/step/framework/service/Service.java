package step.framework.service;

import step.framework.domain.DomainException;
import step.framework.exception.ServiceException;
import step.framework.extensions.ExtensionException;
import step.framework.extensions.pipe.PipeFactory;
import step.framework.extensions.pipe.ServiceInterceptorPipe;

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

    //
    // Constructors
    //

    public Service() {
        this.txManager = TransactionManagerFactory.getDefaultTransactionManager();
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
            ServiceInterceptorPipe pipe = PipeFactory.getServiceInterceptorPipe(this);
            tx = txManager.newTransaction();
            tx.begin();
            before(pipe);
            returnValue = action();
            after(pipe);
            tx.commit();
            txCommited = true;
            return returnValue;
        } catch (ExtensionException e) {
			throw new RuntimeException(e);
		} finally {
            if (!txCommited && tx != null) { tx.rollback(); }
        }

    }

    /** This is the method subclasses should override to implement a service */
    protected abstract R action() throws DomainException;


    //
    // Extensions interception
    //

    protected final void before(ServiceInterceptorPipe pipe) throws DomainException, ServiceException {
        pipe.executeBefore(this);
    }

    protected final void after(ServiceInterceptorPipe pipe) throws DomainException, ServiceException {
        pipe.executeAfter(this);
    }

}
