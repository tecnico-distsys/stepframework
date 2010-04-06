package step.examples.tripplanner.mediator.exception;

public class NoFlightAvailableException extends MediatorException {

	private static final long serialVersionUID = 1L;

	public NoFlightAvailableException() { }

	public NoFlightAvailableException(Throwable cause) {
		super(cause);
	}

}
