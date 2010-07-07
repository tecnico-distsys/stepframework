package step.framework.service;

import step.framework.service.TransactionManager.TransactionType;

/**
 *  The Transaction Manager Factory is the access point for existing transaction managers.<br />
 *  <br />
 *  This implementation always returns the same
 *  manager instance for each type.<br />
 *  <br />
 *  By default it returns a TransactionManager that supports the REQUIRED transaction type
 */
public class TransactionManagerFactory {
	private static TransactionManager NULL_TRANSACTION = new NullTransactionManager();
	private static TransactionManager FENIX_TRANSACTION = new FenixTransactionManager();
	
	public static TransactionManager defaultTransactionManager(TransactionType type) {
		switch (type) { 
		default:
		case REQUIRED:
			return FENIX_TRANSACTION;
		case DISABLED:
			return NULL_TRANSACTION;
		}
	}

	public static TransactionType defaultTransactionType() {
		return TransactionType.REQUIRED;
	}
}
