package step.examples.tripplanner.flight.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class FlightInstance extends FlightInstance_Base {
    
    public  FlightInstance() {
        super();
    }

    public FlightInstance(Flight flight, LocalDate date, Airplane airplane) {
		setFlight(flight);
		setDate(date);
		setAirplane(airplane);

		setCostPerPassenger(calculateCostPerPassenger());
	}
	
	//
	// Domain logic -----------------------------------------------------------
	//

	public int getAvailableSeats() {
		return getAirplane().getCapacity() - getReservationCount();
	}

	public DateTime getDeparture() {
		return getFlight().getDeparture().toDateTime(getDate().toDateMidnight());
	}
	
	protected BigDecimal calculateCostPerPassenger() {
		BigDecimal totalCost = BigDecimal.ZERO;
		totalCost = totalCost.add(getFlight().getBaseCost());
		totalCost = totalCost.add(getAirplane().getCostPerUse());

		BigDecimal capacityBigDecimal = new BigDecimal(getAirplane().getCapacity());

		BigDecimal costPerPassenger = BigDecimal.ZERO;
		costPerPassenger = costPerPassenger.add(totalCost);
		costPerPassenger = costPerPassenger.divide(capacityBigDecimal,
				RoundingMode.HALF_UP);

		return costPerPassenger;
	}
	
	/**
	 * The margin is calculated as half the occupation rate.
	 * @return the current profit margin for new reservations
	 */
	private BigDecimal currentMargin() {
		BigDecimal occupationRate = BigDecimal.valueOf(getReservationCount())
			.divide(BigDecimal.valueOf(getAirplane().getCapacity()), RoundingMode.HALF_UP);
		
		return occupationRate.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
	}

	/** Price equals the cost times the profit margin minus any discount that may apply*/
	public BigDecimal calculatePrice(PassengerType type) {
		BigDecimal price = getCostPerPassenger();
		price = price.add(price.multiply(currentMargin()));
		return price.multiply(BigDecimal.ONE.subtract(type.getDiscount()));
	}

	protected Reservation createReservation(Passenger passenger) {
		if (getReservationCount() == getAirplane().getCapacity())
			return null;
		
		int code = getLastReservation() + 1;
		Reservation reservation = new Reservation(code, passenger, this);
		setLastReservation(code);

		return reservation;
	}

	//
	// Output -----------------------------------------------------------------
	//

	public String toString() {
		return "Flight: code=" + getFlight().getCodeAsString() + " origin=("
				+ getFlight().getOrigin() + ") destination=(" + getFlight().getDestination()
				+ ") departure=" + getDeparture() + " (" + getAirplane() + ") cost-per-passenger="
				+ getCostPerPassenger() + ", " + getReservationCount() + " reservations";
	}
}
