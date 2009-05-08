package step.framework.exception;

public class TransactionException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public TransactionException() { }

    public TransactionException(String arg0) {
        super(arg0);
    }

    public TransactionException(Throwable arg0) {
        super(arg0);
    }

    public TransactionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
