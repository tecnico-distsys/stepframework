package step.examples.tripplanner.flight.service;

import org.joda.time.LocalDate;

import step.examples.tripplanner.flight.domain.Airline;
import step.examples.tripplanner.flight.domain.Airplane;
import step.examples.tripplanner.flight.domain.FlightApp;
import step.examples.tripplanner.flight.domain.FlightInstance;

import step.framework.service.Service;

public class DefineFlightInstanceService extends Service<Void> {
	private int number;
	private LocalDate date;
	private String airplaneRegistration;
	
	public DefineFlightInstanceService(int number, LocalDate date, String airplaneRegistration) {
		this.number = number;
		this.date = date;
		this.airplaneRegistration = airplaneRegistration;		
	}

	@Override
	protected Void action() {
		FlightApp app = getRoot();
		
		Airline airline = app.getAirline();
		Airplane airplane = airline.getAirplane(airplaneRegistration);
		new FlightInstance(airline.getFlight(number), date, airplane);

		return null;
	}

}
