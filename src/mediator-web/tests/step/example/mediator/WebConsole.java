package step.example.mediator;

import step.example.mediator.service.BookFlightService;
import step.example.mediator.view.*;

public class WebConsole {

	public static void main(String[] args) {

		System.out.println("Hello from mediator");

		try {
			ReservationView reservation = new BookFlightService("New York",
					"Rochester", "id213", "Cardoso").execute();
			System.out.println("Reservation "
					+ reservation.getReservationCode() + " for client "
					+ reservation.getClientId() + " completed.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("end mediator invocation");
		}
	}
}
