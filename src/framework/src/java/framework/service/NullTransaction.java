package step.framework.service;

import step.framework.exception.TransactionException;

/**
 *  A null transaction provides an empty implementation of the
 *  transaction interface.<br />
 *  <br />
 */
public class NullTransaction implements Transaction {

    public NullTransaction() {
    }

    /** empty */
    public void begin() throws TransactionException {
    }

    /** empty */
    public void commit() throws TransactionException {
    }

    /** empty */
    public void rollback() throws TransactionException {
    }

}
