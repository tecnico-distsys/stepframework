package step.framework.service;

import step.framework.exception.TransactionException;

/**
 *  This is the Framework's transaction interface.<br />
 *  It specifies the entry points for transactional processing used in the
 *  service's execute <b>template method</b>: begin, commit and rollback.<br />
 *  <br />
 */
public interface Transaction {

    /** Called at the beginning of a transaction */
    public void begin() throws TransactionException;

    /** Called to commit a transaction */
    public void commit() throws TransactionException;

    /** Called to rollback a transaction */
    public void rollback() throws TransactionException;

}
