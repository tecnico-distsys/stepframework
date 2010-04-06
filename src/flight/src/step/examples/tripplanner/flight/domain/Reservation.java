package step.examples.tripplanner.flight.domain;

import static step.examples.tripplanner.flight.domain.PassengerType.*;

public class Reservation extends Reservation_Base {

	public Reservation(int code, Passenger passenger, FlightInstance flight) {
		setCode(code);
		setPassenger(passenger);
		setFlight(flight);
		setType(passenger.getType());
	}

	//
	// Domain logic -----------------------------------------------------------
	//

	@Override
	public void setType(PassengerType type) {
		super.setType(type);
		upgradePassenger();
	}
	
	private void upgradePassenger() {
		switch (getPassenger().getReservationCount()) {
		case 5: getPassenger().setType(SILVER);
				 break;
		case 10: getPassenger().setType(GOLD);
				 break;
		case 25: getPassenger().setType(PLATINUM);
				 break;
		}
	}
	
	//
	// Output -----------------------------------------------------------------
	//

	public String toString() {
		return "Flight reservation: code=" + getFlight().getFlight().getCodeAsString() + "-" + getCode()
				+ " type=" + this.getType() +" (" + this.getPassenger() + ")";
	}
}
