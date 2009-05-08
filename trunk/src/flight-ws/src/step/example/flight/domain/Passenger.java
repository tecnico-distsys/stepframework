package step.example.flight.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Passenger extends FlightDomainObject {

	private static final long serialVersionUID = 1L;

	//
	// Members ----------------------------------------------------------------
	//
	@Column(unique = true)
	private String passport;

	private String name;

	//
	// Constructors and persistence initializer -------------------------------
	//
	public Passenger() {
		super();
	}

	public Passenger(String passportNumber, String name) {
		super();
		this.passport = passportNumber;
		this.name = name;
	}

	public Passenger init() {
		// initialize inbound relationships managed by this class
		// (none)
		// persist to database
		save();

		return this;
	}

	//
	// Property methods -------------------------------------------------------
	//
	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//
	// Output -----------------------------------------------------------------
	//
	public String toString() {
		return "Passenger: passport " + this.getPassport() + " name "
				+ this.getName();
	}

}
