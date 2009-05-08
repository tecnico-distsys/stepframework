package step.example.mediator;

import step.example.mediator.service.CreateReservationService;
import step.example.mediator.view.*;

public class CoreConsole {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello from mediator");

		System.out.println("Executing service...");
		CreateReservationService svc = new CreateReservationService(
				"1200", "Friend", "X2400");
		ReservationView reservationView = svc.execute();

		System.out.println("Result:");
		System.out.println(reservationView.getClientId() + ", "
				+ reservationView.getReservationCode());

	}

}
