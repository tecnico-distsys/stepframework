package step.examples.tripplanner.flight.service;

import java.math.BigDecimal;

import step.examples.tripplanner.flight.domain.Airport;
import step.examples.tripplanner.flight.domain.FlightApp;

import step.framework.service.Service;

public class AddAirportService extends Service<Void> {
	private String iata;
	private String icao;
	private String city;
	private BigDecimal cost;
	
	public AddAirportService(String iata, String icao, String city, BigDecimal cost) {
		this.iata = iata;
		this.icao = icao;
		this.city = city;
		this.cost = cost;		
	}

	@Override
	protected Void action() {
		FlightApp app = getRoot();
		
		app.addAirport(new Airport(iata, icao, city, cost));
		return null;
	}

}
