package step.examples.tripplanner.mediator;

import step.framework.Bootstrap;

import step.examples.tripplanner.mediator.service.*;
import step.examples.tripplanner.mediator.view.ReservationView;


public class Main {
	public static void main(String args[]) throws Exception {
		// initialize STEPframework
		Bootstrap.init();
		createFlightReservation();
	}

	private static void createFlightReservation() {
		ReservationView view = new BookTripService("LIS", "TLS", "12345678", "John Doe").execute();
		
		if (view != null) {
			System.out.println("Reservation number: " + view.getCode() + " Client ID: "
					+ view.getClient().getIdentification() + " Client name: "
					+ view.getClient().getName());
		} else {
			System.out.println("There are no available flights that match your request");
		}
	}
}
