package step.examples.tripplanner.flight.service;


import org.joda.time.LocalDate;

import step.examples.tripplanner.flight.domain.Airport;
import step.examples.tripplanner.flight.domain.FlightApp;
import step.examples.tripplanner.flight.domain.FlightInstance;
import step.examples.tripplanner.flight.view.AirportInformation;
import step.examples.tripplanner.flight.view.FlightInformation;

import step.framework.service.Service;

public class GetFlightInformationService extends Service<FlightInformation> {
	private String code;
	private LocalDate departure;
	
	public GetFlightInformationService(String code, LocalDate departure) {
		this.code = code;
		this.departure = departure;
	}
	
	@Override
	protected FlightInformation action() {
		FlightApp app = getRoot();
		Airport from, to;
		FlightInstance flight;
		try {
			flight = app.getAirline().getFlight(Integer.parseInt(code.substring(2))).getInstance(departure);
		} catch (NullPointerException ex) {
			flight = null;
		}
		
		if (flight == null) {
			return null;
		} else {
			// create view
			from = flight.getFlight().getOrigin();
			to = flight.getFlight().getDestination();
			return new FlightInformation(flight.getFlight().getCodeAsString(), 
					flight.getDeparture().toDate(),
					new AirportInformation(from.getIATACode(), from.getICAOCode(), from.getCity()),
					new AirportInformation(to.getIATACode(), to.getICAOCode(), to.getCity()),
					flight.getAvailableSeats() > 0);
		}
	}

}
