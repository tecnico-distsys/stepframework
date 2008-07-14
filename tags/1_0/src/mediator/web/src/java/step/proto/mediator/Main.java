package step.proto.mediator;

import step.proto.mediator.service.BookFlightService;
import step.proto.mediator.view.ReservationView;

public class Main {

    public static void main(String[] args) {
	
        System.out.println("Hello from mediator");
        	
	try {
	    ReservationView reservation = new BookFlightService("New York", "Rochester", "id213", "Cardoso").execute();
	    System.out.println("Reservation " + reservation.getReservationCode() + " for client " 
			       + reservation.getClientId() + " completed.");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end mediator invocation");
        }
    }
}
