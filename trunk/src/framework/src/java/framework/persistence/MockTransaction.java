package step.framework.persistence;

import javax.transaction.Synchronization;

import org.hibernate.Transaction;

public class MockTransaction implements Transaction {
    
    private static Transaction instance;

    private MockTransaction() {}

    public static synchronized Transaction getInstance() {
        if (instance == null) {
            instance = new MockTransaction();
        }
        return instance;
    }

    /* org.hibernate.Transaction methods */
    public void begin() {}
    public void commit() {}
    public boolean isActive() { throw new UnsupportedOperationException(); }
    public void registerSynchronization(Synchronization synchronization) { throw new UnsupportedOperationException(); }
    public void rollback() {}
    public void setTimeout(int seconds) { throw new UnsupportedOperationException(); }
    public boolean wasCommitted() { throw new UnsupportedOperationException(); }
    public boolean wasRolledBack() { throw new UnsupportedOperationException(); }
}
