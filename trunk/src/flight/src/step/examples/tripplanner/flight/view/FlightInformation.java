package step.examples.tripplanner.flight.view;

import java.io.Serializable;
import java.util.Date;

public class FlightInformation implements Serializable {
	private static final long serialVersionUID = 1L;

	private String flightNumber;
	private Date departure;
	private AirportInformation origin;
	private AirportInformation destination;
	private boolean available;

	FlightInformation() { }

	public FlightInformation(String flightNumber, Date departure,
			AirportInformation origin, AirportInformation destination,
			boolean available) {
		this.flightNumber = flightNumber;
		this.departure = departure;
		this.origin = origin;
		this.destination = destination;
		this.available = available;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public Date getDeparture() {
		return departure;
	}

	public AirportInformation getOrigin() {
		return origin;
	}

	public AirportInformation getDestination() {
		return destination;
	}

	public boolean isAvailable() {
		return available;
	}
}
