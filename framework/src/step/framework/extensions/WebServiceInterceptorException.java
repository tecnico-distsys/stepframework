package step.framework.extensions;

/**
 *  This exception is thrown when a web service interceptor has some problem
 *  that it wants to report.<br />
 */
public class WebServiceInterceptorException extends InterceptorException {

    private static final long serialVersionUID = 1L;

    public WebServiceInterceptorException(String message) {
        super(message);
    }

    public WebServiceInterceptorException(Throwable cause) {
        super(cause);
    }

    public WebServiceInterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

}
