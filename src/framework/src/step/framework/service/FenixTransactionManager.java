package step.framework.service;

/**
 *  The FenixTransactionManager is a factory for new transactions.<br />
 *  A NullTransaction is of the REQUIRED transaction type.<br />
 *  <br />
 */
public class FenixTransactionManager implements TransactionManager {

    public Transaction newTransaction(TransactionType type) {
    	if (!supportsType(type)) {
    		throw new IllegalArgumentException(this.getClass().getName() + " does not support " + type + " transactions.");
    	}
    	return new FenixTransaction();
    }
    
    public boolean supportsType(TransactionType type) {
    	return TransactionType.REQUIRED.equals(type);
    }

}
