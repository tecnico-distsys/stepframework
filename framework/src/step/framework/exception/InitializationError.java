package step.framework.exception;

/**
 * Thrown when the STEPframework initialization process fails and cannot be recovered
 */
public class InitializationError extends Error {

	private static final long serialVersionUID = 1L;

	public InitializationError() {}

	public InitializationError(String message) {
		super(message);
	}

	public InitializationError(Throwable cause) {
		super(cause);
	}

	public InitializationError(String message, Throwable cause) {
		super(message, cause);
	}

}
