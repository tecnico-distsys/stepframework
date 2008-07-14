package step.proto.mediator.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import step.proto.mediator.exception.ClientException;
import step.proto.mediator.exception.DuplicateClientException;
import step.proto.mediator.exception.InvalidClientException;
import step.proto.mediator.exception.MediatorException;
import step.proto.mediator.exception.UnknownClientException;

/**
 * Root object of Mediator domain.
 */
@Entity
public class Mediator extends MediatorDomainObject {
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy="mediator")
    @MapKey(name="identification")
    private Map<String,Client> clients = new HashMap<String, Client>();
    
    private static Mediator instance = null;
    
    public static synchronized Mediator getMediator() {
	instance = loadSingleton(Mediator.class);
	if (instance == null) {
	    instance = new Mediator().init();
	}
	return instance;
    }

    /**
     *  Creates a new Mediator instance, with no clients.
     */
    protected Mediator() {
	clients = new HashMap<String, Client>();
    }

    public Mediator init() {
	// initialize inbound relationships managed by this class
	// (none)
	// persist to database
        save();
        return this;
    }

    public Set<Client> getClients() {
        return Collections.unmodifiableSet(new HashSet<Client>(clients.values()));
    }

    public Client getClient(String id) throws UnknownClientException {
        if (!clients.containsKey(id)) {
            throw new UnknownClientException(id);
        }
        return clients.get(id);
    }

    public Client createClient(String identification, String name) throws MediatorException {
        Client client = new Client(this, identification, name);
        validateAddClient(client);
        clients.put(client.getIdentification(), client);
        try {
            client.init();
        } catch (ClientException ex) {
            throw new InvalidClientException(client.getIdentification(), ex);
        }
        return client;
    }
    
    boolean reservationExists(String code) {
	for (Client client: this.clients.values()) {
	    if (client.reservationExists(code)) {
		return true;
	    }
	}
	return false;
    }

    public String toString() {
        return "Mediator\n- Clients: " + clients.keySet().toString();
    }

    protected void validateAddClient(Client client) throws MediatorException {
        if (clients.containsKey(client.getIdentification()))
            throw new DuplicateClientException(client.getIdentification());
    }

}
