package org.tripplanner.flight.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.tripplanner.flight.exception.NoFlightAvailableForReservationException;
import org.tripplanner.flight.view.ReservationView;
import org.tripplanner.flight.view.PassengerView;
import org.tripplanner.flight.view.CreateLowPriceReservationInput;
import org.tripplanner.flight.view.CreateLowPriceReservationOutput;
import org.tripplanner.flight.service.CreateLowPriceReservationService;

public class CreateLowPriceReservationTest extends DefaultServiceTest {

	@Test
	public void testReservationOkExistingClient() throws Exception {
	    CreateLowPriceReservationInput input = new CreateLowPriceReservationInput();
	    input.setDeparture("Lisbon");
	    input.setDestination("New York");
	    PassengerView passenger = new PassengerView();
	    passenger.setId("L4532602");
	    passenger.setId("John Williams");
	    input.setPassenger(passenger);

		CreateLowPriceReservationService service = new CreateLowPriceReservationService(input);

        CreateLowPriceReservationOutput output = service.execute();
		ReservationView voucher = output.getReservation();

		assertNotNull("Voucher cannot be null", voucher);
		assertNotNull("Voucher's reservation code cannot be null", voucher
				.getCode());
		assertEquals("Voucher's flight number is incorrect", "LIS-NY-1",
				voucher.getFlightNumber());
	}

	@Test
	public void testReservationOkNewClient() throws Exception {
	    CreateLowPriceReservationInput input = new CreateLowPriceReservationInput();
	    input.setDeparture("New York");
	    input.setDestination("Rochester");
	    PassengerView passenger = new PassengerView();
	    passenger.setId("L123123123");
	    passenger.setId("John Doe");
	    input.setPassenger(passenger);

		CreateLowPriceReservationService service = new CreateLowPriceReservationService(input);
        CreateLowPriceReservationOutput output = service.execute();

        ReservationView voucher = output.getReservation();
		assertNotNull("Voucher cannot be null", voucher);
		assertNotNull("Voucher's reservation code cannot be null", voucher
				.getCode());
		assertEquals("Voucher's flight number is incorrect", "NY-ROC-1",
				voucher.getFlightNumber());
	}

	@Test(expected = NoFlightAvailableForReservationException.class)
	public void testReservationFailNoOrigin() throws Exception {
	    CreateLowPriceReservationInput input = new CreateLowPriceReservationInput();
	    input.setDeparture("nowhere");
	    input.setDestination("New York");
	    PassengerView passenger = new PassengerView();
	    passenger.setId("L453260");
	    passenger.setId("John Williams");
	    input.setPassenger(passenger);

		CreateLowPriceReservationService service = new CreateLowPriceReservationService(input);
		service.execute();
	}

	@Test(expected = NoFlightAvailableForReservationException.class)
	public void testReservationFailNoDestination() throws Exception {
	    CreateLowPriceReservationInput input = new CreateLowPriceReservationInput();
	    input.setDeparture("Lisbon");
	    input.setDestination("nowhere");
	    PassengerView passenger = new PassengerView();
	    passenger.setId("L453260");
	    passenger.setId("John Williams");
	    input.setPassenger(passenger);

	    CreateLowPriceReservationService service = new CreateLowPriceReservationService(input);
		service.execute();
	}

	@Test(expected = NoFlightAvailableForReservationException.class)
	public void testReservationFailNoPlaces() throws Exception {
	    CreateLowPriceReservationInput input = new CreateLowPriceReservationInput();
	    input.setDeparture("nowhere");
	    input.setDestination("nowhere");
	    PassengerView passenger = new PassengerView();
	    passenger.setId("L453260");
	    passenger.setId("John Williams");
	    input.setPassenger(passenger);

	    CreateLowPriceReservationService service = new CreateLowPriceReservationService(input);
		service.execute();
	}

}