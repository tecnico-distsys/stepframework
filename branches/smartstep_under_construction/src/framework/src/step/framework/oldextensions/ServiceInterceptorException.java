package step.framework.oldextensions;

/**
 *  This exception is thrown when a service interceptor has some problem
 *  that it wants to report.<br />
 */
public class ServiceInterceptorException extends InterceptorException {

    private static final long serialVersionUID = 1L;

    public ServiceInterceptorException(String message) {
        super(message);
    }

    public ServiceInterceptorException(Throwable cause) {
        super(cause);
    }

    public ServiceInterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

}
