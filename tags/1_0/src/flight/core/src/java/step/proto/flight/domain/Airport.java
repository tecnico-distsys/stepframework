package step.proto.flight.domain;

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
    private String city;

    private BigDecimal costPerUse;


    //
    // Constructors and persistence initializer -------------------------------
    //
    public Airport() {
	super();
    }
	
    public Airport(String city, BigDecimal costPerUse) {
	super();
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
	return "Airport: city " + this.getCity() + " cost-per-use " + this.getCostPerUse();
    }
    
}
