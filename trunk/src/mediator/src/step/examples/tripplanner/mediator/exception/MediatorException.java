package step.examples.tripplanner.mediator.exception;

public class MediatorException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MediatorException() { }

	public MediatorException(String message) {
		super(message);
	}

	public MediatorException(Throwable cause) {
		super(cause);
	}

	public MediatorException(String message, Throwable cause) {
		super(message, cause);
	}
}
