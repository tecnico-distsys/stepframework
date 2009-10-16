package step.framework.service;

/**
 *  The Null Transaction manager is a factory for null transactions.<br />
 *  <br />
 */
public class NullTransactionManager implements TransactionManager {

    public Transaction newTransaction() {
        return new NullTransaction();
    }

}
