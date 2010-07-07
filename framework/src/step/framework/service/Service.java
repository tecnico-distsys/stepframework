package step.framework.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pt.ist.fenixframework.DomainObject;

import step.framework.exception.ServiceException;
import step.framework.extensions.ServiceInterceptorManager;
import step.framework.persistence.Persistence;
import step.framework.service.TransactionManager.TransactionType;

/**
 *  This is the Framework's base service.<br />
 *  All service classes should extend this class or one of its subclasses.<br />
 *  <br />
 */
public abstract class Service<R> {

    //
    // Members
    //
    
	protected Log log;

    /** Return value */
    protected R returnValue;

    /** Transaction manager */
    protected TransactionManager.TransactionType txType;

    /** Extensions manager */
    protected ServiceInterceptorManager extManager;


    //
    // Constructors
    //

    public Service(TransactionManager.TransactionType type) {
        // if persistence support is disabled, all services are transactionless
    	if (Persistence.isEnabled())
    		this.txType = type;
    	else
    		this.txType = TransactionType.DISABLED;
    	
        this.extManager = new ServiceInterceptorManager();
        this.log = LogFactory.getLog(this.getClass());
    }

    public Service() {
    	this(TransactionManagerFactory.defaultTransactionType());
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
     *  The actual transactional behaviour can be redefined passing it different
     *  TransactionManager instances allowing services to be invoked with different
     *  transaction implementations.<br />
     *  <br />
     */
    public final R execute(TransactionManager txManager) throws ServiceException {
    	Transaction tx = null;
        boolean txCommited = false;

        // if persistence support is disabled, all services are transactionless
    	if (!Persistence.isEnabled())
    		txManager = TransactionManagerFactory.defaultTransactionManager(TransactionType.DISABLED);

        if (!txManager.supportsType(txType)) {
        	String message = "Transaction type " + txType + " not supported by " + txManager.getClass().getCanonicalName() + ".";
        	log.warn(message);
        	throw new ServiceException(message);
        }
        
        try {
            tx = txManager.newTransaction(txType);
            tx.begin();
            before();
            returnValue = action();
            after();
            tx.commit();
            txCommited = true;
            return returnValue;
        } catch (RuntimeException ex) {
        	log.debug(ex);
        	throw ex;
        } finally {
            if (!txCommited && tx != null) { tx.rollback(); }
        }

    }

    public R execute() throws ServiceException {
    	return execute(TransactionManagerFactory.defaultTransactionManager(txType));
    }
    
    /** This is the method subclasses should override to implement a service */
    protected abstract R action();


    //
    // Extensions interception
    //

    protected final void before() throws ServiceException {
        extManager.interceptBeforeService(this);
    }

    protected final void after() throws ServiceException {
        extManager.interceptAfterService(this);
    }

    @SuppressWarnings("unchecked")
    protected final <T extends DomainObject> T getRoot() {
        return (T) Persistence.getRoot();
    }
}
