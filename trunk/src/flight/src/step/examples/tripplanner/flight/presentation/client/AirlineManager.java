package step.examples.tripplanner.flight.presentation.client;

import java.util.Date;
import java.util.List;

import step.examples.tripplanner.flight.exception.NoAvailableFlightsException;
import step.examples.tripplanner.flight.view.AirportInformation;
import step.examples.tripplanner.flight.view.FlightInformation;
import step.examples.tripplanner.flight.view.ReservationVoucher;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("manager")
public interface AirlineManager extends RemoteService {
	FlightInformation requestFlightInformation(String flightNumber, Date departure);
	ReservationVoucher bookFlight(String origin, String destination, String passport, String name) throws NoAvailableFlightsException;
	List<FlightInformation> requestAvailableFlights();
	List<AirportInformation> requestAirports();
}
