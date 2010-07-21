package step.framework.perf.monitor.layer;

import javax.transaction.Synchronization;

import org.hibernate.Transaction;

/**
 *  This is a performance monitoring wrapping of the Hibernate transaction.
 */
public class PerfHibernateTransaction implements Transaction {

    private Transaction wrappedTransaction;

    PerfHibernateTransaction(Transaction transactionToWrap) {
        wrappedTransaction = transactionToWrap;
    }

    /* org.hibernate.Transaction methods */
    public void begin() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Transaction.begin");
        try {
            wrappedTransaction.begin();
        } finally {
            //monitor.exit("hibernate.Transaction.begin");
            monitor.exit("hibernate");
        }
    }

    public void commit() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Transaction.commit");
        try {
            wrappedTransaction.commit();
        } finally {
            //monitor.exit("hibernate.Transaction.commit");
            monitor.exit("hibernate");
        }
    }

    public boolean isActive() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Transaction.isActive");
        try {
            return wrappedTransaction.isActive();
        } finally {
            //monitor.exit("hibernate.Transaction.isActive");
            monitor.exit("hibernate");
        }
    }

    public void rollback() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Transaction.rollback");
        try {
            wrappedTransaction.rollback();
        } finally {
            //monitor.exit("hibernate.Transaction.rollback");
            monitor.exit("hibernate");
        }
    }

    public void registerSynchronization(Synchronization synchronization) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Transaction.registerSynchronization");
        try {
            wrappedTransaction.registerSynchronization(synchronization);
        } finally {
            //monitor.exit("hibernate.Transaction.registerSynchronization");
            monitor.exit("hibernate");
        }
    }

    public void setTimeout(int seconds) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Transaction.setTimeout");
        try {
            wrappedTransaction.setTimeout(seconds);
        } finally {
            //monitor.exit("hibernate.Transaction.setTimeout");
            monitor.exit("hibernate");
        }
    }

    public boolean wasCommitted() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Transaction.wasCommitted");
        try {
            return wrappedTransaction.wasCommitted();
        } finally {
            //monitor.exit("hibernate.Transaction.wasCommitted");
            monitor.exit("hibernate");
        }
    }

    public boolean wasRolledBack() {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        //monitor.enter("hibernate.Transaction.wasRolledBack");
        try {
            return wrappedTransaction.wasRolledBack();
        } finally {
            //monitor.exit("hibernate.Transaction.wasRolledBack");
            monitor.exit("hibernate");
        }
    }

}
