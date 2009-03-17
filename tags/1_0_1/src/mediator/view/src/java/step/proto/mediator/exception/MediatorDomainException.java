package step.proto.mediator.exception;

import step.framework.domain.DomainException;

public class MediatorDomainException extends DomainException {
    private static final long serialVersionUID = 1L;

    public MediatorDomainException() { }

    public MediatorDomainException(String message) {
        super(message);
    }

    public MediatorDomainException(Throwable cause) {
        super(cause);
    }

    public MediatorDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
