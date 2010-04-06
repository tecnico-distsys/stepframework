package step.examples.tripplanner.flight.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import step.examples.tripplanner.flight.domain.Airport;
import step.examples.tripplanner.flight.domain.FlightApp;
import step.examples.tripplanner.flight.view.AirportInformation;

import step.framework.service.Service;

public class GetAirportsService extends Service<List<AirportInformation>> {

	@Override
	protected List<AirportInformation> action() {
		List<AirportInformation> airports = new ArrayList<AirportInformation>();
		
		FlightApp app = getRoot();

		for (Airport airport: app.getAirportSet()) {
			// create view
			airports.add(new AirportInformation(airport.getIATACode(), airport.getICAOCode(),
					airport.getCity()));
		}

		Collections.sort(airports, new Comparator<AirportInformation>() {
			public int compare(AirportInformation a1, AirportInformation a2) {
				int result = a1.getCity().compareTo(a2.getCity());

				return (result == 0) ? result = a1.getIATACode().compareTo(a2.getIATACode()) : result;
			}	
		});

		return airports;
	}

}
