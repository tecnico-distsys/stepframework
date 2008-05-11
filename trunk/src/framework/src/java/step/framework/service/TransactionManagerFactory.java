package step.framework.service;

public class TransactionManagerFactory {
    private static TransactionManager localTransactionManager = new LocalTransactionManager();
//    private static TransactionManager remoteInvocationTransactionManager = new NullTransactionManager();
//    private static TransactionManager distributedTransactionManager = new DistributedTransactionManager();

    public static TransactionManager getLocalTransactionManager() {
        return localTransactionManager;
    }

//    public static TransactionManager getRemoteInvocationTransactionManager() {
//        return remoteInvocationTransactionManager;
//    }
//
//    public static TransactionManager getDistributedTransactionManager() {
//        return distributedTransactionManager;
//    }
}