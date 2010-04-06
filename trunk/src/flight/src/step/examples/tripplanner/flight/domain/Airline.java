package step.examples.tripplanner.flight.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import step.examples.tripplanner.flight.exception.NoAvailableFlightsException;

import static step.examples.tripplanner.flight.domain.PassengerType.REGULAR;

public class Airline extends Airline_Base {

	public Airline(String name, String code) {
		setName(name);
		setCode(code);
	}

	//
	// Domain logic -----------------------------------------------------------
	//

	private static boolean isValidCode(String code) {
		return code.matches("[a-zA-Z]*") && code.length() == 2;
	}

	/**
	 * @throws IllegalArgumentException if code is not a 2-letter IATA code, or if the airline is
	 *		   already set, as the code is read-only.
	 */
	@Override
	public void setCode(String code) throws IllegalArgumentException {
		if (!isValidCode(code) || getFlightApp() != null) {
			throw new IllegalArgumentException(code);
		} else {
			super.setCode(code);
		}
	}

	@Override
	public void addFlight(Flight flight) {
		// check for existing flight with same code
		int code = flight.getCode();
		Flight existing = getFlight(code);

		if (existing != null && !flight.equals(existing)) {
			throw new IllegalArgumentException(""+code);
		} else {
			super.addFlight(flight);
		}
	}
	
	/**
	 * Orders FlightInstances first by cost and then by departure date
	 */
	protected class FlightComparator implements Comparator<FlightInstance> {

		public int compare(FlightInstance f1, FlightInstance f2) {
			int price = f1.calculatePrice(REGULAR).compareTo(f2.calculatePrice(REGULAR));

			return (price == 0) ? f1.getDeparture().compareTo(f2.getDeparture()) : price;
		}
	}

	/**
	 * Find existing flights from 'departFrom' to 'arriveAt' ordered by cost (and departure date)
	 *
	 * @return A SortedSet of flights ordered by cost-per-passenger (and departure date)
	 */
	public SortedSet<FlightInstance> findAvailableFlights() {
		SortedSet<FlightInstance> foundFlights = new TreeSet<FlightInstance>(
				new FlightComparator());

		for (Flight flight : getFlightSet()) {
			for (FlightInstance instance : flight.getInstanceSet()) {
				if (instance.getAvailableSeats() > 0) {
					foundFlights.add(instance);
				}
			}
		}
		return foundFlights;
	}

	
	/**
	 * Find existing flights from 'departFrom' to 'arriveAt' ordered by cost (and departure date)
	 *
	 * @return A SortedSet of flights ordered by cost-per-passenger (and departure date)
	 */
	public SortedSet<FlightInstance> findAvailableFlights(Set<Airport> departFrom,
			Set<Airport> arriveAt) {
		SortedSet<FlightInstance> foundFlights = new TreeSet<FlightInstance>(
				new FlightComparator());

		for (Flight flight : getFlightSet()) {
			if (departFrom.contains(flight.getOrigin())
					&& arriveAt.contains(flight.getDestination())) {
				for (FlightInstance instance : flight.getInstanceSet()) {
					if (instance.getAvailableSeats() > 0) {
						foundFlights.add(instance);
					}
				}
			}
		}
		return foundFlights;
	}

	/**
	 * Obtain a passenger given its passport ID
	 * 
	 * @param passport the passenger's passport ID
	 * @return the passenger with the given passport, or null if no passenger with that passport ID
	 * 		   has ever booked a flight with this airline.
	 */
	public Passenger getPassenger(String passport){
		for(Passenger p : getPassengerSet()){
			if(p.getPassport().equals(passport))
				return p;
		}
		return null;
	}

	/**
	 * Create a (or obtain an existing) passenger
	 * 
	 * @param passport the passenger's passport ID
	 * @param name the passenger's name
	 * @return a passenger with the given passport, create one if no passenger with that passport
	 * 		   ID is registered with this airline.
	 */
	public Passenger createPassenger(String passport, String name) {
		// decide whether it is a new passenger
		Passenger passenger = getPassenger(passport);
		if (passenger == null) {
			passenger = new Passenger(passport, name, REGULAR);
			addPassenger(passenger);
		}
		return passenger;
	}
	

	/**
	 * Obtain a flight given its code
	 * 
	 * @param code the flight number
	 * @return the flight with the given code, or null if no flight with such code exists.
	 */
	public Flight getFlight(int code){
		for(Flight f : getFlightSet()){
			if(f.getCode() == code)
				return f;
		}
		return null;
	}

	/**
	 * Obtain an airplane given its registration
	 * 
	 * @param registration the aircraft registration
	 * @return the airplane with the given registration, or null if no aircraft with such
	 * 		   registration exists.
	 */
	public Airplane getAirplane(String registration){
		for(Airplane a : getAirplaneSet()){
			if(a.getAircraftRegistration().equals(registration))
				return a;
		}
		return null;
	}

	/**
	 * Make a reservation for the cheapest flight from an origin airport to a destination point, and
	 * assign it to a passenger with given name and passport.
	 * 
	 * @param origin airport of departure
	 * @param destination airport of arrival
	 * @param passport passport ID of passenger booking the flight
	 * @param name name of passenger booking the flight
	 * @throws NoAvailableFlightsException if there are no flights available
	 * @return flight reservation for cheapest flight available, or null if there are no available
	 * 		   flights between <code>origin</code> and <code>destination</code>
	 */
	public Reservation reserveFlight(Set<Airport> departFrom, Set<Airport> arriveAt,
			Passenger passenger) throws NoAvailableFlightsException {
		// list flights from origin to destination ordered by
		// price-per-passenger (cheapest first)
		SortedSet<FlightInstance> foundFlights = findAvailableFlights(departFrom, arriveAt);

		// throw exception if set is empty
		if (foundFlights.isEmpty()) {
			throw new NoAvailableFlightsException();
		}

		// use cheapest and earliest flight
		FlightInstance flight = foundFlights.first();

		// create reservation (will return null if it could not complete the request)
		return flight.createReservation(passenger);
	}

	public Set<Reservation> getAllReservations() {
		Set<Reservation> result = new HashSet<Reservation>();
		
		for (Flight flight : getFlightSet()) {
			for (FlightInstance instance : flight.getInstanceSet()) {
				result.addAll(instance.getReservationSet());
			}
		}
		return result;
	}
	
	//
	// Output -----------------------------------------------------------------
	//

	public String toString() {
		return "Airline: name=" + getName() + " code=" + getCode();
	}
}
