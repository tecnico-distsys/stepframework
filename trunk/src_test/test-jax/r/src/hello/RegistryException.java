package hello;

/**
 * Exception type for all problems related with Registry classes
 * Most times this exception will wrap a cause exception.
 */
public class RegistryException extends Exception {

	private static final long serialVersionUID = 1L;

	public RegistryException(String message) {
		super(message);
	}

	public RegistryException(Throwable cause) {
		super(cause);
	}

	public RegistryException(String message, Throwable cause) {
		super(message, cause);
	}

}
