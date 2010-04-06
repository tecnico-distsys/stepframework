package step.examples.tripplanner.flight.domain;

public class Passenger extends Passenger_Base {

	public Passenger(String passportNumber, String name, PassengerType type) {
		setPassport(passportNumber);
		setName(name);
		setType(type);
	}

	//
	// Output -----------------------------------------------------------------
	//

	public String toString() {
		return "Passenger: passport=" + getPassport() + " name=" + getName() + " type=" + getType();
	}
}
