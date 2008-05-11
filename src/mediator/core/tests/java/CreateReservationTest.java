import step.proto.mediator.exception.DuplicateReservationException;
import step.proto.mediator.service.CreateReservationService;
import step.proto.mediator.view.ReservationView;

public class CreateReservationTest extends DefaultServiceTest {

    @Test
    public void testOkExistingClient() throws Exception {
	CreateReservationService service =
	    new CreateReservationService("L4532602", "John Williams", "LIS-NY-1-6");

	ReservationView view = service.execute();
	assertNotNull("View cannot be null", view);
	assertEquals("View's reservation code is incorrect", "LIS-NY-1-6", view.getReservationCode());
	assertEquals("View's client id is incorrect", "L4532602", view.getClientId());
    }

    @Test
    public void testOkNewClient() throws Exception {
	CreateReservationService service =
	    new CreateReservationService("L123123123", "John Doe", "LIS-NY-1-6");

	ReservationView view = service.execute();
	assertNotNull("View cannot be null", view);
	assertEquals("View's reservation code is incorrect", "LIS-NY-1-6", view.getReservationCode());
	assertEquals("View's client id is incorrect", "L123123123", view.getClientId());
    }

    @Test(expected=DuplicateReservationException.class)
    public void testFailDuplicateReservation() throws Exception {
	CreateReservationService service =
	    new CreateReservationService("L4532602", "John Williams", "LIS-NY-1-5");

	service.execute();
    }
}