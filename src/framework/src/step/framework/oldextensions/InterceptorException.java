package step.framework.oldextensions;

/**
 *  This exception is the base type for interceptor exceptions.
 */
public class InterceptorException extends ExtensionException {

    private static final long serialVersionUID = 1L;

    public InterceptorException(String message) {
        super(message);
    }

    public InterceptorException(Throwable cause) {
        super(cause);
    }

    public InterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

}
