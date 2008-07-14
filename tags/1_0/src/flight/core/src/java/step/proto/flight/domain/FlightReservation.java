package step.proto.flight.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class FlightReservation extends FlightDomainObject {

    private static final long serialVersionUID = 1L;
    
    //
    // Members ----------------------------------------------------------------
    //
    @Column(unique = true)
    private String code;
    
    @OneToOne
    private Passenger passenger;
    
    @ManyToOne
    private Flight flight;


    //
    // Constructors and persistence initializer -------------------------------
    //
    public FlightReservation() {
	super();
    }
	
    public FlightReservation(String code, Passenger passenger, Flight flight) {
	super();
	
	this.code = code;
	this.passenger = passenger;
	this.flight = flight;
    }

    public FlightReservation init() {
	// initialize inbound relationships managed by this class
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
    public void setCode(String reservationCode) {
	this.code = reservationCode;
    }

    public Passenger getPassenger() {
	return passenger;
    }
    public void setPassenger(Passenger passenger) {
	this.passenger = passenger;
    }
	
    public Flight getFlight() {
	return flight;
    }
    public void setFlight(Flight flight) {
	this.flight = flight;
    }

	
    //
    // Output -----------------------------------------------------------------
    //
    public String toString() {
	return "Flight reservation: code " + this.getCode() + " " +
	    this.getPassenger();
    }

}
