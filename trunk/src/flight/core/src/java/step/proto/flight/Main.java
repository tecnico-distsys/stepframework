package step.proto.flight;

import step.proto.flight.service.CreateFlightReservationService;
import step.proto.flight.view.ReservationVoucher;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello from flight");
        
        System.out.println("Executing service...");
        CreateFlightReservationService svc = new CreateFlightReservationService("new york", "rochester", "id11", "Smith");
        ReservationVoucher reservationVoucher = svc.execute();

        System.out.println("Result:");
        System.out.println(reservationVoucher.getReservationCode() + ", " + reservationVoucher.getFlightNumber());
        
    }
    
}
