package step.framework.service;

/**
 *  This is the Framework's transaction manager interface.<br />
 *  It specifies the transaction creation entry point used in the
 *  service's execute <b>template method</b>.<br />
 *  <br />
 */
public interface TransactionManager {

    public Transaction newTransaction();

}
