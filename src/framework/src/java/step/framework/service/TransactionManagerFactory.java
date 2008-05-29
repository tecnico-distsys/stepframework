package step.framework.service;

/**
 *  The Transaction Manager Factory is the access point for
 *  existing transaction managers.<br />
 *  <br />
 *  This implementation always returns the same
 *  manager instance for each type.<br />
 *  <br />
 */
public class TransactionManagerFactory {

    private static TransactionManager nullTransactionManager = new NullTransactionManager();
    private static TransactionManager localTransactionManager = new LocalTransactionManager();

    public static TransactionManager getDefaultTransactionManager() {
        return getNullTransactionManager();
    }

    public static TransactionManager getNullTransactionManager() {
        return nullTransactionManager;
    }

    public static TransactionManager getLocalTransactionManager() {
        return localTransactionManager;
    }

}
