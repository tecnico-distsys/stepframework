package org.tripplanner.mediator.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.tripplanner.mediator.exception.DuplicateReservationException;
import org.tripplanner.mediator.service.CreateReservationService;
import org.tripplanner.mediator.view.*;

public class CreateReservationTest extends DefaultServiceTest {

	@Test
	public void testOkExistingClient() throws Exception {
		CreateReservationService service = new CreateReservationService(
				"L4532602", "John Williams", "LIS-NY-1-6");

		ReservationView view = service.execute();
		assertNotNull("View cannot be null", view);
		assertEquals("View's reservation code is incorrect", "LIS-NY-1-6", view
				.getReservationCode());
		assertEquals("View's client id is incorrect", "L4532602", view
				.getClientId());
	}

	@Test
	public void testOkNewClient() throws Exception {
		CreateReservationService service = new CreateReservationService(
				"L123123123", "John Doe", "LIS-NY-1-6");

		ReservationView view = service.execute();
		assertNotNull("View cannot be null", view);
		assertEquals("View's reservation code is incorrect", "LIS-NY-1-6", view
				.getReservationCode());
		assertEquals("View's client id is incorrect", "L123123123", view
				.getClientId());
	}

	@Test(expected = DuplicateReservationException.class)
	public void testFailDuplicateReservation() throws Exception {
		CreateReservationService service = new CreateReservationService(
				"L4532602", "John Williams", "LIS-NY-1-5");

		service.execute();
	}
}