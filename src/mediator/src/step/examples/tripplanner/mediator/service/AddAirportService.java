package step.examples.tripplanner.mediator.service;

import step.examples.tripplanner.mediator.domain.Airport;
import step.examples.tripplanner.mediator.domain.MediatorApp;

import step.framework.service.Service;

public class AddAirportService extends Service<Void> {
	private String code;
	private String city;
	private String name;

	public AddAirportService(String code, String city) {
		this(code, city, "");
	}

	public AddAirportService(String code, String city, String name) {
		this.code = code;
		this.city = city;
		this.name = name;
	}

	@Override
	protected Void action() {
		MediatorApp app = getRoot();
		
		app.addAirport(new Airport(code, city, name));
		return null;
	}

}
