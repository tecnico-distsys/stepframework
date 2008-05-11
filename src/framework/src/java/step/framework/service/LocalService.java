package step.framework.service;


public abstract class LocalService<R> extends Service<R> {

    public LocalService() {
        this.txManager = TransactionManagerFactory.getLocalTransactionManager();
    }
}
