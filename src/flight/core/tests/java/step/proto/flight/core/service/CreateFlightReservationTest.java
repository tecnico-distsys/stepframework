package step.proto.flight.core.service;
import step.proto.flight.exception.NoFlightAvailableForReservationException;
import step.proto.flight.service.CreateFlightReservationService;
import step.proto.flight.view.ReservationVoucher;

import static org.junit.Assert.*;
import org.junit.Test;

public class CreateFlightReservationTest extends DefaultServiceTest {

    @Test
    public void testReservationOkExistingClient() throws Exception {
	CreateFlightReservationService service =
	    new CreateFlightReservationService("Lisbon", "New York", "L4532602", "John Williams");

	ReservationVoucher voucher = service.execute();
	assertNotNull("Voucher cannot be null", voucher);
	assertNotNull("Voucher's reservation code cannot be null", voucher.getReservationCode());
	assertEquals("Voucher's flight number is incorrect", "LIS-NY-1", voucher.getFlightNumber());
    }

    @Test
    public void testReservationOkNewClient() throws Exception {
	CreateFlightReservationService service =
	    new CreateFlightReservationService("New York", "Rochester", "L123123123", "John Doe");

	ReservationVoucher voucher = service.execute();
	assertNotNull("Voucher cannot be null", voucher);
	assertNotNull("Voucher's reservation code cannot be null", voucher.getReservationCode());
	assertEquals("Voucher's flight number is incorrect", "NY-ROC-1", voucher.getFlightNumber());
    }

    @Test(expected=NoFlightAvailableForReservationException.class)
    public void testReservationFailNoOrigin() throws Exception {
	CreateFlightReservationService service =
	    new CreateFlightReservationService("nowhere", "New York", "L453260", "John Williams");
	service.execute();
    }

    @Test(expected=NoFlightAvailableForReservationException.class)
    public void testReservationFailNoDestination() throws Exception {
	CreateFlightReservationService service =
	    new CreateFlightReservationService("Lisbon", "everywhere", "L453260", "John Williams");
	service.execute();
    }

    @Test(expected=NoFlightAvailableForReservationException.class)
    public void testReservationFailNoPlaces() throws Exception {
	CreateFlightReservationService service =
	    new CreateFlightReservationService("nowhere", "everywhere", "L453260", "John Williams");
	service.execute();
    }
}