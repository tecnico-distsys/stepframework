package step.framework.perf.monitor.event;

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
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Transaction.begin");
        try {
            wrappedTransaction.begin();
        } finally {
            monitor.event("exit-hibernate.Transaction.begin");
        }
    }

    public void commit() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Transaction.commit");
        try {
            wrappedTransaction.commit();
        } finally {
            monitor.event("exit-hibernate.Transaction.commit");
        }
    }

    public boolean isActive() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Transaction.isActive");
        try {
            return wrappedTransaction.isActive();
        } finally {
            monitor.event("exit-hibernate.Transaction.isActive");
        }
    }

    public void rollback() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Transaction.rollback");
        try {
            wrappedTransaction.rollback();
        } finally {
            monitor.event("exit-hibernate.Transaction.rollback");
        }
    }

    public void registerSynchronization(Synchronization synchronization) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Transaction.registerSynchronization");
        try {
            wrappedTransaction.registerSynchronization(synchronization);
        } finally {
            monitor.event("exit-hibernate.Transaction.registerSynchronization");
        }
    }

    public void setTimeout(int seconds) {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Transaction.setTimeout");
        try {
            wrappedTransaction.setTimeout(seconds);
        } finally {
            monitor.event("exit-hibernate.Transaction.setTimeout");
        }
    }

    public boolean wasCommitted() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Transaction.wasCommitted");
        try {
            return wrappedTransaction.wasCommitted();
        } finally {
            monitor.event("exit-hibernate.Transaction.wasCommitted");
        }
    }

    public boolean wasRolledBack() {
        PerfEventMonitor monitor = MonitorHelper.get();
        monitor.event("enter-hibernate.Transaction.wasRolledBack");
        try {
            return wrappedTransaction.wasRolledBack();
        } finally {
            monitor.event("exit-hibernate.Transaction.wasRolledBack");
        }
    }

}
