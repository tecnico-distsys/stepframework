package step.examples.tripplanner.mediator.presentation.client;

import java.util.List;

import step.examples.tripplanner.mediator.exception.NoFlightAvailableException;
import step.examples.tripplanner.mediator.view.AirportInformation;
import step.examples.tripplanner.mediator.view.ReservationView;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("mediator")
public interface MediationService extends RemoteService {
	ReservationView bookFlight(String origin, String destination, String passport, String name) throws NoFlightAvailableException;
	List<AirportInformation> requestAirports();
}
