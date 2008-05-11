package step.framework.service;

public class LocalTransactionManager implements TransactionManager {
    
    private ThreadLocal<Transaction> currentTx = new ThreadLocal<Transaction>() {
        protected synchronized Transaction initialValue() {
            return null;
        }
    };
    
    public Transaction newTransaction() {
	if (currentTx.get() == null) {
	    currentTx.set(new LocalTransaction(this));
	}
        return currentTx.get();
    }

    protected void txDone(LocalTransaction tx) {
	if (tx == currentTx.get()) {
	    currentTx.remove();
	}
    }
}