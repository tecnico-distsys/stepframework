package step.framework.exception;

public class RemoteServiceException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public RemoteServiceException() {
    }

    public RemoteServiceException(String message) {
	super(message);
    }

    public RemoteServiceException(Throwable cause) {
	super(cause);
    }

    public RemoteServiceException(String message, Throwable cause) {
	super(message, cause);
    }

}
