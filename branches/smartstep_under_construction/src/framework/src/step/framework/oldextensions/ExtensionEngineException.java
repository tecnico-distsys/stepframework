package step.framework.oldextensions;

/**
 *  This exception is thrown when the extension engine has some problem
 *  that it wants to report.<br />
 */
public class ExtensionEngineException extends Exception {

    private static final long serialVersionUID = 1L;

    public ExtensionEngineException(String message) {
        super(message);
    }

    public ExtensionEngineException(Throwable cause) {
        super(cause);
    }

    public ExtensionEngineException(String message, Throwable cause) {
        super(message, cause);
    }

}
