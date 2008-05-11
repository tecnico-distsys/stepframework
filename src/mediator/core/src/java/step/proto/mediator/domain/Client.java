package step.proto.mediator.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import step.proto.mediator.exception.ClientException;
import step.proto.mediator.exception.DuplicateReservationException;

@Entity
public class Client extends MediatorDomainObject {
    private static final long serialVersionUID = 1L;
    
    @Column(unique=true)
    private String identification;
    private String name;
    
    @ManyToOne
    private Mediator mediator;
    
    @OneToMany(mappedBy="client")
    private Set<Reservation> reservations = new HashSet<Reservation>();
    
    protected Client() { }
    
    public Client(Mediator mediator, String identification, String name) {
	this.mediator = mediator;
	this.identification = identification;
	this.name = name;
    }
    
    public Client init() throws ClientException {
	// initialize inbound relationships managed by this class
	// (none)
	// persist to database
        save();
        
        return this;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Set<Reservation> getReservations() {
        return Collections.unmodifiableSet(reservations);
    }

    public Reservation createReservation(String code) throws ClientException {
        validateAddReservation(code);
        Reservation reservation = new Reservation(this, code).init();
        reservations.add(reservation);
        return reservation;
    }

    protected void validateAddReservation(String code) throws ClientException {
	if (this.mediator.reservationExists(code)) {
	    throw new DuplicateReservationException(code);
	}
    }

    boolean reservationExists(String code) {
	for (Reservation reservation: this.reservations) {
	    if (reservation.getCode().equals(code)) {
		return true;
	    }
	}
	return false;
    }

}
