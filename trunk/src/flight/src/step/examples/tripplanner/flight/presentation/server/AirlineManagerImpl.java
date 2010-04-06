package step.examples.tripplanner.flight.presentation.server;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;

import step.examples.tripplanner.flight.exception.NoAvailableFlightsException;
import step.examples.tripplanner.flight.presentation.client.AirlineManager;
import step.examples.tripplanner.flight.service.BookFlightService;
import step.examples.tripplanner.flight.service.GetAirportsService;
import step.examples.tripplanner.flight.service.GetAvailableFlightsService;
import step.examples.tripplanner.flight.service.GetFlightInformationService;
import step.examples.tripplanner.flight.view.AirportInformation;
import step.examples.tripplanner.flight.view.FlightInformation;
import step.examples.tripplanner.flight.view.ReservationVoucher;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AirlineManagerImpl extends RemoteServiceServlet implements
		AirlineManager {

	@Override
	public ReservationVoucher bookFlight(String origin, String destination, 
			String passport, String name) throws NoAvailableFlightsException {
		return new BookFlightService(origin, destination, passport, name).execute();
	}

	@Override
	public List<FlightInformation> requestAvailableFlights() {
		return new GetAvailableFlightsService().execute();
	}

	@Override
	public List<AirportInformation> requestAirports() {
		return new GetAirportsService().execute();
	}

	@Override
	public FlightInformation requestFlightInformation(String flightNumber,
			Date departure) {
		return new GetFlightInformationService(flightNumber, new LocalDate(departure)).execute();
	}
}
