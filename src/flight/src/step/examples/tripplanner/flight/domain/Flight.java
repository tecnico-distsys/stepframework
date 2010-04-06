package step.examples.tripplanner.flight.domain;

import java.math.BigDecimal;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class Flight extends Flight_Base {

	public Flight(int code, LocalTime departure, Airport origin, Airport destination) {
		setCode(code);
		setDeparture(departure);
		setOrigin(origin);
		setDestination(destination);
		
		setBaseCost(calculateBaseCost());
	}

	//
	// Domain logic -----------------------------------------------------------
	//
	
	@Override
	public void addInstance(FlightInstance instance) {
		// check for existing flight with same code
		LocalDate date = instance.getDate();
		FlightInstance existing = getInstance(date);

		if (existing != null && !instance.equals(existing)) {
			throw new IllegalArgumentException(date.toString());
		} else {
			super.addInstance(instance);
		}
	}
	
	/**
	 * Obtains the flight's instance for given a date.
	 * @param date date of flight
	 * @return the flight instance on the given date, or <code>null</code> if no flight instance is
	 *         found
	 */
	public FlightInstance getInstance(LocalDate date) {
		for (FlightInstance instance : getInstanceSet()) {
			if (instance.getDate().equals(date)) {
				return instance;
			}
		}
		return null;
	}
	
	private BigDecimal calculateBaseCost() {
		BigDecimal totalCost = BigDecimal.ZERO;
		totalCost = totalCost.add(getOrigin().getCostPerUse());
		totalCost = totalCost.add(getDestination().getCostPerUse());

		return totalCost;
	}

	public String getCodeAsString() {
		return getAirline().getCode() + String.format("%04d", getCode());
	}
	
	//
	// Output -----------------------------------------------------------------
	//

	public String toString() {
		return "Flight: code=" + getCodeAsString() + " origin=(" + getOrigin()
				+ ") destination=(" + getDestination()
				+ ") departure=" + getDeparture()
				+ " base-cost=" + getBaseCost();
	}
}
