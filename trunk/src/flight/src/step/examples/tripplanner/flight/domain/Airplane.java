package step.examples.tripplanner.flight.domain;

import java.math.BigDecimal;

public class Airplane extends Airplane_Base {
	public Airplane(String registration, String make, String model, int capacity,
			BigDecimal costPerUse) {
		setAircraftRegistration(registration);
		setMake(make);
		setModel(model);
		setCapacity(capacity);
		setCostPerUse(costPerUse);
	}

	//
	// Output -----------------------------------------------------------------
	//

	public String toString() {
		return "Airplane: aircraft-registration=" + this.getAircraftRegistration() + " model="
		+ this.getMake() + " " + this.getModel() + " capacity=" + this.getCapacity()
		+ " cost-per-use=" + this.getCostPerUse();
	}
}
