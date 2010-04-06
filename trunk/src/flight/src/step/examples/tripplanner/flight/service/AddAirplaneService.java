package step.examples.tripplanner.flight.service;

import java.math.BigDecimal;

import step.framework.service.Service;

import step.examples.tripplanner.flight.domain.Airline;
import step.examples.tripplanner.flight.domain.Airplane;
import step.examples.tripplanner.flight.domain.FlightApp;

public class AddAirplaneService extends Service<Void> {
	private String registration;
	private String make;
	private String model;
	private int capacity;
	private BigDecimal cost;
	
	public AddAirplaneService(String registration, String make, String model,
			int capacity, BigDecimal cost) {
		this.registration = registration;
		this.make = make;
		this.model = model;
		this.capacity = capacity;
		this.cost = cost;
	}

	@Override
	protected Void action() {
		FlightApp app = getRoot();
		Airline airline = app.getAirline();
		airline.addAirplane(new Airplane(registration, make, model, capacity, cost));
		
		return null;
	}

}
