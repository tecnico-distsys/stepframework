package step.framework.service;

/**
 *  Local services execute on a local transaction.<br />
 *  <br />
 *  Its transactional behaviour is specified by
 *  a local transaction manager and transaction.<br />
 *  <br />
 */
public abstract class LocalService<R> extends Service<R> {

    public LocalService() {
        this.txManager = TransactionManagerFactory.getLocalTransactionManager();
    }

}
