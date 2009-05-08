package step.framework.domain;

public abstract class DomainException extends Exception {
    private static final long serialVersionUID = 1L;

    public DomainException() { }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(Throwable cause) {
        super(cause);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
