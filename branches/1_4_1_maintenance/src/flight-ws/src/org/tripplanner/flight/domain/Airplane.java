package org.tripplanner.flight.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Airplane extends FlightDomainObject {

	private static final long serialVersionUID = 1L;

	//
	// Members ----------------------------------------------------------------
	//
	@Column(unique = true)
	private String registration;
	private String model;
	private Integer capacity;
	private BigDecimal costPerUse;

	//
	// Constructors and persistence initializer -------------------------------
	//
	public Airplane() {
		super();
	}

	public Airplane(String registrationNumber, String model, Integer capacity,
			BigDecimal costPerUse) {
		super();
		this.registration = registrationNumber;
		this.model = model;
		this.capacity = capacity;
		this.costPerUse = costPerUse;
	}

	public Airplane init() {
		// initialize inbound relationships managed by this class
		// (none)
		// persist to database
		save();

		return this;
	}

	//
	// Property methods -------------------------------------------------------
	//
	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
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
		return "Airplane: registration " + this.getRegistration() + " model "
				+ this.getModel() + " capacity " + this.getCapacity()
				+ " cost-per-use " + this.getCostPerUse();
	}

}
