package step.framework.jarloader;

public class JarException extends Exception {

	private static final long serialVersionUID = -8072667205784318453L;

	public JarException() {
	}

	public JarException(String message) {
		super(message);
	}

	public JarException(Throwable cause) {
		super(cause);
	}

	public JarException(String message, Throwable cause) {
		super(message, cause);
	}

}
