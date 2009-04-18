package step.example.mediator.ws.client;

import java.util.Random;

import step.example.mediator.view.*;
import step.example.mediator.ws.client.service.CreateReservationService;

public class ConsoleClient {

	public static void main(String[] args) {

		try {
			System.out.println("begin test invocation");

			CreateReservationService service = new CreateReservationService(
					"1300", "Remote Friend", "X2600");
			ReservationView view = service.execute();

			System.out.println("-Reservation code: "
					+ view.getReservationCode());
			System.out.println("-Reservation client id: " + view.getClientId());

		} catch (Exception e) {
			System.out.println("Exception...");
			e.printStackTrace();

		} finally {
			System.out.println("end test invocation");
		}
	}

	protected static String generateRandomPassengerId() {
		Random generator = new Random(System.currentTimeMillis());
		int randomNumber = generator.nextInt();
		randomNumber = (randomNumber < 0 ? -randomNumber : randomNumber);
		return "PT" + Integer.toString(randomNumber);
	}

}
