package org.tripplanner.mediator.exception;

public class UnknownClientException extends MediatorException {
	private static final long serialVersionUID = 1L;

	public UnknownClientException() {
	}

	public UnknownClientException(String message) {
		super(message);
	}

	public UnknownClientException(Throwable cause) {
		super(cause);
	}

	public UnknownClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
