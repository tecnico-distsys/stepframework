package step.examples.tripplanner.flight;

import step.framework.Bootstrap;

import step.examples.tripplanner.flight.service.*;
import step.examples.tripplanner.flight.view.ReservationVoucher;


public class Main {
	public static void main(String args[]) throws Exception {
		// initialize STEPframework
		Bootstrap.init();
		createFlightReservation();
	}

	private static void createFlightReservation() {
		ReservationVoucher voucher = new BookFlightService("LIS", "TLS", "12345678", "John Doe").execute();
		
		if (voucher != null) {
			System.out.println("Flight: " + voucher.getFlightNumber() + " Departure time: "
					+ voucher.getFlightDate() + " Reservation number: "
					+ voucher.getReservationCode());
		} else {
			System.out.println("There are no available flights that match your request");
		}
	}
}
