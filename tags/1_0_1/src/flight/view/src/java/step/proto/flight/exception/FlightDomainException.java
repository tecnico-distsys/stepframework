package step.proto.flight.exception;

import step.framework.domain.DomainException;

public abstract class FlightDomainException extends DomainException {

    private static final long serialVersionUID = 1L;

    public FlightDomainException() {
    }

    public FlightDomainException(String message) {
	super(message);
    }

    public FlightDomainException(Throwable cause) {
	super(cause);
    }

    public FlightDomainException(String message, Throwable cause) {
	super(message, cause);
    }

}
