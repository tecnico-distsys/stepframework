package step.examples.tripplanner.flight.service;

import java.util.ArrayList;
import java.util.List;

import step.examples.tripplanner.flight.domain.Airport;
import step.examples.tripplanner.flight.domain.FlightApp;
import step.examples.tripplanner.flight.domain.FlightInstance;
import step.examples.tripplanner.flight.view.AirportInformation;
import step.examples.tripplanner.flight.view.FlightInformation;

import step.framework.service.Service;

public class GetAvailableFlightsService extends Service<List<FlightInformation>> {

	
	@Override
	protected List<FlightInformation> action() {
		List<FlightInformation> flights= new ArrayList<FlightInformation>();
		
		Airport from, to;
		
		FlightApp app = getRoot();

		for (FlightInstance flight: app.getAirline().findAvailableFlights()) {
			// create view
			from = flight.getFlight().getOrigin();
			to = flight.getFlight().getDestination();
			flights.add(new FlightInformation(flight.getFlight().getCodeAsString(), 
					flight.getDeparture().toDate(),
					new AirportInformation(from.getIATACode(), from.getICAOCode(), from.getCity()),
					new AirportInformation(to.getIATACode(), to.getICAOCode(), to.getCity()),
					flight.getAvailableSeats() > 0));
		}
		
		return flights;
	}

}
