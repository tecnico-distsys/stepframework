package step.test;

public class AssertException extends Exception {

    private static final long serialVersionUID = 1L;

    public AssertException() { }

    public AssertException(String arg0) {
        super(arg0); }

    public AssertException(Throwable cause) {
        super(cause); }

    public AssertException(String message, Throwable cause) {
        super(message, cause);
    }

}
