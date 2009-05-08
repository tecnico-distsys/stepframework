package step.example.flight;

import step.example.flight.service.CreateFlightReservationService;
import step.example.flight.view.ReservationVoucher;

public class Console {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello from flight");

		System.out.println("Executing service...");
		CreateFlightReservationService svc = new CreateFlightReservationService(
				"new york", "rochester", "id11", "Smith");
		ReservationVoucher reservationVoucher = svc.execute();

		System.out.println("Result:");
		System.out.println(reservationVoucher.getReservationCode() + ", "
				+ reservationVoucher.getFlightNumber());

	}

}
