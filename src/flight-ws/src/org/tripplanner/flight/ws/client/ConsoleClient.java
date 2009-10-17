package org.tripplanner.flight.ws.client;

import java.util.Random;

import org.tripplanner.flight.view.ReservationVoucher;
import org.tripplanner.flight.ws.client.service.CreateFlightReservationService;

public class ConsoleClient {

	public static void main(String[] args) {

		try {
			System.out.println("begin test invocation");

			CreateFlightReservationService service = new CreateFlightReservationService(
					"New York", "Rochester", "id213", "Cardoso");

			ReservationVoucher voucher = service.execute();

			System.out.println("-Reservation code: "
					+ voucher.getReservationCode());
			System.out.println("-Reservation flight number: "
					+ voucher.getFlightNumber());

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
