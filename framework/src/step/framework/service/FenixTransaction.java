package step.framework.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pt.ist.fenixframework.pstm.Transaction;

import step.framework.context.ThreadContext;
import step.framework.exception.TransactionException;

/**
 *  A FenixTransaction implements the persistence's layer
 *  transaction mechanisms using the Fénix Framework.<br />
 *  
 *  Since there is no nested transaction support, when a new transaction is
 *  created and there is already a local transaction underway,
 *  the current transaction is joined instead of creating a new transaction.<br />
 *  <br />
 *  Subclasses of local transaction can also use the nesting
 *  level accessor methods to influence their own extended behaviour.<br />
 *  <br />
 *  In the current implementation, the nesting is kept in a thread context
 *  property.<br />
 *  <br />
 */

public class FenixTransaction implements step.framework.service.Transaction {
	/** Nesting property name */
	protected static final String NESTING_PROPERTY = "step.framework.service.txNesting";

	//
	// Members
	//
	protected Log log;


	public FenixTransaction() {
		this.log = LogFactory.getLog(this.getClass());
	}

	//
	//  Nesting property handling
	//

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
	//  Transaction implementation
	//

	/**
	 *  If it's the first transaction beginning in the current thread then
	 *  start a persistence layer transaction.
	 *  Else, just update the nesting level.
	 */
	public void begin() throws TransactionException {
		if (log.isTraceEnabled()) {
			log.trace("Begin a new transaction (if necessary).");
		}

		Integer nesting = getNesting();
		if (nesting == null) {
			log.trace("Begin a new fenix-framework transaction.");
			Transaction.begin(); // begin new transaction
			setNesting(1);
		} else {
			setNesting(nesting+1);
		}

		if (log.isTraceEnabled()) {
			log.trace("After transaction begun.");
			log.trace(NESTING_PROPERTY + "=" + getNesting());
		}
	}

	/**
	 *  If it's the last transaction commiting in the current thread then
	 *  commit the persistence layer transaction.
	 *  Else, just update the nesting level.
	 */
	public void commit() throws TransactionException {
		if (log.isTraceEnabled()) {
			log.trace("Commit a transaction (if top-most)");
		}

		Integer nesting = getNesting();
		if (nesting == null) {
			String message = "Can't commit because there is no active transaction.";
			log.warn(message);
			throw new IllegalStateException(message);
		}

		boolean finished = false;
		try {
			if (nesting == 1) {
				try {
					log.trace("Commit the current fenix-framework transaction.");
					Transaction.commit(); // commit current transaction
					resetNesting();
					finished = true;
				} catch (Exception ex) {
					log.warn("Commit failed: " + ex);
					throw new TransactionException(ex);
				}
			} else {
				setNesting(nesting-1);
				finished = true;
			}
		} finally {
			if (log.isTraceEnabled()) {
				log.trace("After transaction commit " + (finished ? "succeeded." : "failed."));
				log.trace(NESTING_PROPERTY + "=" + getNesting());
			}
		}
	}

	/**
	 *  If it's the last transaction rolling back in the current thread then
	 *  rollback the persistence layer transaction.
	 *  Else, just update the nesting level (as the rollback will be propagated
	 *  to upper level transactions).
	 */
	public void rollback() throws TransactionException {
		if (log.isTraceEnabled()) {
			log.trace("Rollback a transaction (if top-most)");
		}
		Integer nesting = getNesting();
		if (nesting == null) {
			String message = "Can't rollback because there is no active transaction.";
			log.warn(message);
			throw new IllegalStateException(message);
		}

		try {
			if (nesting == 1) {
				try {
					log.trace("Abort current fenix-framework transaction.");
					Transaction.abort(); // rollback current transaction
				} catch (Exception ex) {
					log.warn("Rollback failed: " + ex);
					throw new TransactionException(ex);
				} finally {
					resetNesting();
				}
			} else {
				setNesting(nesting-1);
			}
		} finally {
			if (log.isTraceEnabled()) {
				log.trace("After transaction rollback.");
				log.trace(NESTING_PROPERTY + "=" + getNesting());
			}
		}
	}
}
