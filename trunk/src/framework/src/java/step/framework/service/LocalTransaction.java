package step.framework.service;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import step.framework.exception.TransactionException;
import step.framework.persistence.PersistenceUtil;

public class LocalTransaction implements Transaction {
    private LocalTransactionManager ltxm;
    private org.hibernate.Transaction tx;
    private int nesting = 0;
    private Session session;

    public LocalTransaction(LocalTransactionManager ltxm) {
	this.ltxm = ltxm;
        this.session = PersistenceUtil.getSessionFactory().getCurrentSession();
    }

    public void begin() throws TransactionException {
        if (nesting == 0) {
            try {
                this.tx = session.beginTransaction();
            } catch (HibernateException ex) {
                throw new TransactionException(ex);
            }
        }
        nesting++;
    }

    public void commit() throws TransactionException {
	if (nesting == 1) {
            try {
                this.tx.commit();
            } catch (HibernateException ex) {
                throw new TransactionException(ex);
            } finally {
		ltxm.txDone(this);
	    }
	}
	nesting--;
    }

    public void rollback() throws TransactionException {
	if (nesting == 1) {
            try {
                this.tx.rollback();
            } catch (HibernateException ex) {
                throw new TransactionException(ex);
            } finally {
		ltxm.txDone(this);
	    }
	}
    	nesting--;
    }
}