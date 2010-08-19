package hello.exception.ws;

import step.framework.domain.DomainException;

public abstract class HelloWSException extends DomainException {

	private static final long serialVersionUID = 1L;

	public HelloWSException() {
	}

	public HelloWSException(String message) {
		super(message);
	}

	public HelloWSException(Throwable cause) {
		super(cause);
	}

	public HelloWSException(String message, Throwable cause) {
		super(message, cause);
	}

}
