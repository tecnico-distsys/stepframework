package step.framework.service;

/**
 *  The Local Transaction manager is a factory for new local transactions.<br />
 *  <br />
 */
public class LocalTransactionManager implements TransactionManager {

    public Transaction newTransaction() {
        return new LocalTransaction();
    }

}
