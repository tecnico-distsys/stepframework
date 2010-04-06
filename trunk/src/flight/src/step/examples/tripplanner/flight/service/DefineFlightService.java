package step.examples.tripplanner.flight.service;

import org.joda.time.LocalTime;

import step.examples.tripplanner.flight.domain.Airline;
import step.examples.tripplanner.flight.domain.Airport;
import step.examples.tripplanner.flight.domain.Flight;
import step.examples.tripplanner.flight.domain.FlightApp;

import step.framework.service.Service;

public class DefineFlightService extends Service<Void> {
	private int number;
	private String origin;
	private String destination;
	private LocalTime departure;
	
	public DefineFlightService(int number, String origin, String destination, LocalTime departure) {
		this.number = number;
		this.origin = origin;
		this.destination = destination;
		this.departure = departure;
	}

	@Override
	protected Void action() {
		FlightApp app = getRoot();

		Airport departFrom = app.getAirport(origin);
		Airport arriveAt = app.getAirport(destination);
		Airline airline = app.getAirline();

		airline.addFlight(new Flight(number, departure, departFrom, arriveAt));

		return null;
	}

}
