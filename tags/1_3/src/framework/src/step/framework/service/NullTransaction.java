package step.framework.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.exception.TransactionException;

/**
 *  A null transaction provides an empty implementation of the
 *  transaction interface.<br />
 *  <br />
 */
public class NullTransaction implements Transaction {

	protected Log log;

	public NullTransaction() {
		this.log = LogFactory.getLog(this.getClass());
	}

	/** empty */
	public void begin() throws TransactionException {
		if (log.isTraceEnabled()) {
			log.trace("Commit a transaction");
		}
	}

	/** empty */
	public void commit() throws TransactionException {
		if (log.isTraceEnabled()) {
			log.trace("Commit a transaction");
		}
	}

	/** empty */
	public void rollback() throws TransactionException {
		if (log.isTraceEnabled()) {
			log.trace("Rollback a transaction");
		}
	}

}
