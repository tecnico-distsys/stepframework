package step.examples.tripplanner.mediator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import step.examples.tripplanner.mediator.domain.Airport;
import step.examples.tripplanner.mediator.domain.MediatorApp;
import step.examples.tripplanner.mediator.view.AirportInformation;

import step.framework.service.Service;

public class GetAirportsService extends Service<List<AirportInformation>> {

	@Override
	protected List<AirportInformation> action() {
		List<AirportInformation> airports = new ArrayList<AirportInformation>();
		
		MediatorApp app = getRoot();

		for (Airport airport: app.getAirportSet()) {
			// create view
			airports.add(new AirportInformation(airport.getCode(), airport.getCity(), airport.getName()));
		}

		Collections.sort(airports, new Comparator<AirportInformation>() {
			public int compare(AirportInformation a1, AirportInformation a2) {
				int result = a1.getCity().compareTo(a2.getCity());

				return (result == 0) ? result = a1.getCode().compareTo(a2.getCode()) : result;
			}	
		});

		return airports;
	}

}
