package step.examples.tripplanner.flight.service;

import step.examples.tripplanner.flight.domain.Airline;
import step.examples.tripplanner.flight.domain.FlightApp;

import step.framework.service.Service;

public class CreateAirlineService extends Service<Void> {
	private String name;
	private String code;
	
	public CreateAirlineService(String name, String code) {
		this.name = name;
		this.code = code;
	}

	@Override
	protected Void action() {
		FlightApp app = getRoot();
		
		Airline airline = new Airline(name, code);
		app.setAirline(airline);
		
		return null;
	}
}
