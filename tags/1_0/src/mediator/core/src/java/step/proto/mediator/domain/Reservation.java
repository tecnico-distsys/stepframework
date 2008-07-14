package step.proto.mediator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Reservation extends MediatorDomainObject {
    private static final long serialVersionUID = 1L;
    
    @Column(unique=true)
    private String code;
    
    @ManyToOne
    private Client client;
    
    protected Reservation() { }
    
    public Reservation(Client client, String code) {
	this.client = client;
	this.code = code;
    }
    
    public Reservation init() {
	// initialize inbound relationships managed by this class
	// (none)
	// persist to database
        save();
        
        return this;
    }

    public String getCode() {
        return code;
    }

    public Client getClient() {
        return client;
    }
    
    public boolean equals(Object obj) {
	if (this == obj) return true;
	if (! (obj instanceof Reservation)) return false;
	
	Reservation reservation = (Reservation) obj;
	return this.code == reservation.getCode() || this.code.equals(reservation.getCode());
    }
    
    public int hashCode() {
	return code == null ? 0 : code.hashCode();
    }
}
