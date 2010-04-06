package step.examples.tripplanner.mediator.presentation.client;

import java.util.List;

import step.examples.tripplanner.mediator.view.AirportInformation;
import step.examples.tripplanner.mediator.view.ReservationView;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of <code>AirlineManager</code>.
 */
public interface MediationServiceAsync {
	void bookFlight(String origin, String destination, String passport, String name, AsyncCallback<ReservationView> callback);
	void requestAirports(AsyncCallback<List<AirportInformation>> callback);
}
