package step.examples.tripplanner.mediator.domain;

public class MediatorApp extends MediatorApp_Base {

	public MediatorApp() {
		setLastReservationCode(0);
	}

	//
	// Domain logic -----------------------------------------------------------
	//

	/**
	 * Obtain a client given its ID
	 * 
	 * @param identification the client's ID
	 * @return the client with the given ID, or null if no passenger with that passport ID
	 * 		   has ever used the mediator.
	 */
	public Client getClient(String identification){
		for(Client c : getClientSet()){
			if(c.getIdentification().equals(identification))
				return c;
		}
		return null;
	}

	/**
	 * Registers a new client
	 * 
	 * @param client Client to register
	 * @throws IllegalArgumentException if a client with the same identification is already
	 *         registered.
	 */
	@Override
	public void addClient(Client client) {
		// check for existing client with same identification
		String id = client.getIdentification();
		Client existing = getClient(id);
		
		if (existing != null && !client.equals(existing)) {
			throw new IllegalArgumentException(id);
		} else {
			super.addClient(client);
		}
	}
	
	/**
	 * Obtains an airport, given it's IATA codes.
	 * @param code IATA code of required airport
	 * @return the airport with the given code, or <code>null</code> if no airport is found
	 */
	public Airport getAirport(String code) {
		if (!Airport.isValidCode(code)) {
			throw new IllegalArgumentException(code);
		}
		
		for (Airport airport : getAirportSet()) {
			if (airport.getCode().equals(code)) {
				return airport;
			}
		}
		return null;
	}
	
	/**
	 * Registers a new Airport.
	 * @param airport Airport to register
	 * @throws IllegalArgumentException if an airport with the same IATA code is already registered.
	 */
	@Override
	public void addAirport(Airport airport) {
		// check for existing airport with same IATA code
		String code = airport.getCode();
		Airport existing = getAirport(code);
		
		if (existing != null && !airport.equals(existing)) {
			throw new IllegalArgumentException(code);
		} else {
			super.addAirport(airport);
		}
	}
}
