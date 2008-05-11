package step.framework.service;

import step.framework.exception.TransactionException;

public interface Transaction {
    public void begin() throws TransactionException;
    public void commit() throws TransactionException;
    public void rollback() throws TransactionException;
}
