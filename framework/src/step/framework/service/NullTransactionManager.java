package step.framework.service;

/**
 *  The Null Transaction manager is a factory for null transactions.<br />
 *  A NullTransaction is of the DISABLED transaction type.<br />
 *  <br />
 */
public class NullTransactionManager implements TransactionManager {

    public Transaction newTransaction(TransactionType type) {
    	if (!supportsType(type)) {
    		throw new IllegalArgumentException(this.getClass().getName() + " does not support " + type + " transactions.");
    	}

        return new NullTransaction();
    }

    public boolean supportsType(TransactionType type) {
    	return TransactionType.DISABLED.equals(type);
    }

}
