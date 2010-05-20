package step.framework.extensions.exceptions;

/**
 *  This exception is thrown when an extension has some problem
 *  that it wants to report.<br />
 */
public class ExtensionException extends Exception {

    private static final long serialVersionUID = 1L;

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(Throwable cause) {
        super(cause);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }

}
