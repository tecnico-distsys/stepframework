package org.tripplanner.flight.domain;

import java.math.*;
import java.util.*;

import javax.persistence.*;


@Entity
public class Flight extends FlightDomainObject {

	private static final long serialVersionUID = 1L;

	//
	// Members ----------------------------------------------------------------
	//
	@Column(unique = true)
	private String number;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;

	private BigDecimal costPerPassenger;
	private BigDecimal pricePerPassenger;

    /** last issued reservation identifier */
	private int lastReservationId;

	@ManyToOne
	private Airport origin;

	@ManyToOne
	private Airport destination;

	@ManyToOne
	private Airplane airplane;

	@OneToMany(mappedBy = "flight")
	private Set<FlightReservation> reservations;

	//
	// Constructors and persistence initializer -------------------------------
	//
	public Flight() {
		super();
		initReservations();
	}

	public Flight(String number, Date dateTime, BigDecimal profitMargin,
			Airport origin, Airport destination, Airplane airplane) {
		this();

		this.number = number;
		this.dateTime = dateTime;
		this.origin = origin;
		this.destination = destination;
		this.airplane = airplane;

		this.calculateCostPerPassenger();
		this.calculatePricePerPassenger(profitMargin);
		this.lastReservationId = 0;
	}

	public Flight(String number, Date dateTime, BigDecimal profitMargin,
			Airport origin, Airport destination, Airplane airplane,
			int initialReservationId) {
		this(number, dateTime, profitMargin, origin, destination, airplane);
		this.lastReservationId = initialReservationId;
	}

	public Flight init() {
		// initialize inbound relationships managed by this class
		// (none)
		// persist to database
		save();

		return this;
	}

	protected void initReservations() {
		if (this.reservations == null) {
			this.reservations = new HashSet<FlightReservation>();
		}
	}

	//
	// Property methods -------------------------------------------------------
	//
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getCapacity() {
		return this.airplane.getCapacity();
	}

	public BigDecimal getCostPerPassenger() {
		return costPerPassenger;
	}

	public BigDecimal getPricePerPassenger() {
		return pricePerPassenger;
	}

	public void setPricePerPassenger(BigDecimal pricePerPassenger) {
		this.pricePerPassenger = pricePerPassenger;
	}

	public Airport getOrigin() {
		return origin;
	}

	public void setOrigin(Airport origin) {
		this.origin = origin;
	}

	public Airport getDestination() {
		return destination;
	}

	public void setDestination(Airport destination) {
		this.destination = destination;
	}

	public Airplane getAirplane() {
		return airplane;
	}

	public void setAirplane(Airplane airplane) {
		this.airplane = airplane;
	}

	public Set<FlightReservation> getReservations() {
		return Collections.unmodifiableSet(reservations);
	}

	public void removeReservation(FlightReservation reservation) {
		this.reservations.remove(reservation);
	}

	public void addReservation(FlightReservation reservation) {
		this.reservations.add(reservation);
	}

	//
	// Output -----------------------------------------------------------------
	//
	public String toString() {
		return "Flight: date-time " + this.getDateTime()
				+ " cost-per-passenger " + this.getCostPerPassenger()
				+ " price-per-passenger " + this.getPricePerPassenger() + " "
				+ this.getAirplane() + " origin " + this.getOrigin()
				+ " destination " + this.getDestination() + " "
				+ this.getReservations().size() + " reservations";
	}

	//
	// Domain logic -----------------------------------------------------------
	//

    /** Calculate passenger cost */
	protected void calculateCostPerPassenger() {
		BigDecimal totalCost = BigDecimal.ZERO;
		totalCost = totalCost.add(this.getAirplane().getCostPerUse());
		totalCost = totalCost.add(this.getOrigin().getCostPerUse());
		totalCost = totalCost.add(this.getDestination().getCostPerUse());

		BigDecimal capacityBigDecimal = new BigDecimal(this.getCapacity());

		BigDecimal costPerPassenger = BigDecimal.ZERO;
		costPerPassenger = costPerPassenger.add(totalCost);
		costPerPassenger =
		    costPerPassenger.divide(capacityBigDecimal, RoundingMode.HALF_UP);

		this.costPerPassenger = costPerPassenger;
	}

	/** Price equals the cost times the profit margin */
	protected void calculatePricePerPassenger(BigDecimal profitMargin) {
		this.pricePerPassenger = this.costPerPassenger.multiply(profitMargin);
	}

    /** Create new reservation for this flight */
	protected FlightReservation createReservation(Passenger passenger) {
		//String code = this.number + "-" + ++this.lastReservationId;
		int randomInt = new Random().nextInt();
		if (randomInt < 0) randomInt = -randomInt;
		String code = this.number + "-" + randomInt;

		FlightReservation reservation =
		    new FlightReservation(code, passenger, this).init();
		this.addReservation(reservation);

		return reservation;
	}

}
