package step.examples.tripplanner.mediator.presentation.server;

import java.util.List;

import step.examples.tripplanner.mediator.exception.NoFlightAvailableException;
import step.examples.tripplanner.mediator.presentation.client.MediationService;
import step.examples.tripplanner.mediator.service.BookTripService;
import step.examples.tripplanner.mediator.service.GetAirportsService;
import step.examples.tripplanner.mediator.view.AirportInformation;
import step.examples.tripplanner.mediator.view.ReservationView;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MediationServiceImpl extends RemoteServiceServlet implements
		MediationService {

	@Override
	public ReservationView bookFlight(String origin, String destination, 
			String passport, String name) throws NoFlightAvailableException{
		return new BookTripService(origin, destination, passport, name).execute();
	}
	
	@Override
	public List<AirportInformation> requestAirports() {
		return new GetAirportsService().execute();
	}
}
