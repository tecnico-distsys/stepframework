package hello.exception.ws;


public class MissingNameException extends HelloWSException {

	private static final long serialVersionUID = 1L;

	public MissingNameException() {
	}

	public MissingNameException(String message) {
		super(message);
	}

	public MissingNameException(Throwable cause) {
		super(cause);
	}

	public MissingNameException(String message, Throwable cause) {
		super(message, cause);
	}

}
