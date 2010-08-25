package step.framework.wsdl;

public class WSDLException extends Exception {

	private static final long serialVersionUID = -863747777741356287L;

	public WSDLException() {
	}

	public WSDLException(String message) {
		super(message);
	}

	public WSDLException(Throwable cause) {
		super(cause);
	}

	public WSDLException(String message, Throwable cause) {
		super(message, cause);
	}

}
