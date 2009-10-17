package org.tripplanner.mediator.web.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.tripplanner.mediator.exception.MediatorDomainException;
import org.tripplanner.mediator.service.BookFlightService;
import org.tripplanner.mediator.view.*;

public class BookFlightTest extends DefaultServiceTest {

	@Test
	public void testOkExistingClient() throws Exception {
		BookFlightService service = new BookFlightService("Lisbon", "New York",
				"L4532602", "John Williams");

		ReservationView view = service.execute();
		assertNotNull("View cannot be null", view);
		assertNotNull("View's reservation code cannot be null", view
				.getReservationCode());
		assertEquals("View's client id is incorrect", "L4532602", view
				.getClientId());
	}

	@Test
	public void testOkNewClient() throws Exception {
		BookFlightService service = new BookFlightService("New York",
				"Rochester", "L123123123", "John Doe");

		ReservationView view = service.execute();
		assertNotNull("View cannot be null", view);
		assertNotNull("View's reservation code cannot be null", view
				.getReservationCode());
		assertEquals("View's client id is incorrect", "L123123123", view
				.getClientId());
	}

	@Test(expected = MediatorDomainException.class)
	public void testFailNoOrigin() throws Exception {
		BookFlightService service = new BookFlightService("nowhere",
				"New York", "L453260", "John Williams");
		service.execute();
	}

	@Test(expected = MediatorDomainException.class)
	public void testFailNoDestination() throws Exception {
		BookFlightService service = new BookFlightService("Lisbon",
				"everywhere", "L453260", "John Williams");
		service.execute();
	}

	@Test(expected = MediatorDomainException.class)
	public void testFailNoPlaces() throws Exception {
		BookFlightService service = new BookFlightService("nowhere",
				"everywhere", "L453260", "John Williams");
		service.execute();
	}
}