package org.tripplanner.flight.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Airport extends FlightDomainObject {

	private static final long serialVersionUID = 1L;

	//
	// Members ----------------------------------------------------------------
	//

	@Column(unique = true)
	private String code;

	private String city;

	private BigDecimal costPerUse;

	//
	// Constructors and persistence initializer -------------------------------
	//
	public Airport() {
		super();
	}

	public Airport(String code, String city, BigDecimal costPerUse) {
		super();
		this.code = code;
		this.city = city;
		this.costPerUse = costPerUse;
	}

	public Airport init() {
		// initialize inbound relationships managed by this class
		// (none)
		// persist to database
		save();

		return this;
	}

	//
	// Property methods -------------------------------------------------------
	//
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public BigDecimal getCostPerUse() {
		return costPerUse;
	}

	public void setCostPerUse(BigDecimal costPerUse) {
		this.costPerUse = costPerUse;
	}

	//
	// Output -----------------------------------------------------------------
	//
	public String toString() {
        final String FORMAT = "Airport: code %s city %s cost %s";
		return String.format(
            FORMAT, this.getCode(), this.getCity(), this.getCostPerUse().toString());
	}

}
