package step.examples.tripplanner.flight.presentation.client;

import java.util.Date;
import java.util.List;

import step.examples.tripplanner.flight.exception.NoAvailableFlightsException;
import step.examples.tripplanner.flight.view.AirportInformation;
import step.examples.tripplanner.flight.view.FlightInformation;
import step.examples.tripplanner.flight.view.ReservationVoucher;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of <code>AirlineManager</code>.
 */
public interface AirlineManagerAsync {
	void requestFlightInformation(String flightNumber, Date departure, AsyncCallback<FlightInformation> callback);
	void bookFlight(String origin, String destination, String passport, String name, AsyncCallback<ReservationVoucher> callback) throws NoAvailableFlightsException;
	void requestAvailableFlights(AsyncCallback<List<FlightInformation>> callback);
	void requestAirports(AsyncCallback<List<AirportInformation>> callback);
}
