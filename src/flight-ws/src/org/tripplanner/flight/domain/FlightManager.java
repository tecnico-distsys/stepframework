package org.tripplanner.flight.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.tripplanner.flight.exception.NoFlightAvailableForReservationException;

@Entity
public class FlightManager extends FlightDomainObject {

    private static final long serialVersionUID = 1L;

    //
    // Singleton --------------------------------------------------------------
    //
    private static FlightManager instance;

    public static synchronized FlightManager getInstance() {
        instance = loadSingleton(FlightManager.class);
        if (instance == null) {
            instance = new FlightManager().init();
        }
        return instance;
    }

    //
    // Members ----------------------------------------------------------------
    //
    @OneToMany
    @MapKey(name = "registration")
    private Map<String, Airplane> airplanes;

    @OneToMany
    @MapKey(name = "city")
    private Map<String, Airport> airports;

    @OneToMany
    @MapKey(name = "number")
    private Map<String, Flight> flights;

    @OneToMany
    @MapKey(name = "passport")
    private Map<String, Passenger> passengers;

    //
    // Constructors and persistence initializer -------------------------------
    //

    protected FlightManager() {
        initCollections();
    }

    public FlightManager init() {
        // initialize inbound relationships managed by this class
        // (none)
        // persist to database
        save();

        return this;
    }

    protected void initCollections() {
        if (this.airplanes == null) {
            this.airplanes = new HashMap<String, Airplane>();
        }
        if (this.airports == null) {
            this.airports = new HashMap<String, Airport>();
        }
        if (this.flights == null) {
            this.flights = new HashMap<String, Flight>();
        }
        if (this.passengers == null) {
            this.passengers = new HashMap<String, Passenger>();
        }
    }

    //
    // Property methods -------------------------------------------------------
    //
    public Set<Airplane> getAirplanes() {
        return Collections.unmodifiableSet(new HashSet<Airplane>(this.airplanes
                .values()));
    }

    public void removeAirplane(Airplane airplane) {
        this.airplanes.remove(airplane.getRegistration());
    }

    public void addAirplane(Airplane airplane) {
        this.airplanes.put(airplane.getRegistration(), airplane);
    }

    public Set<Airport> getAirports() {
        return Collections.unmodifiableSet(new HashSet<Airport>(this.airports
                .values()));
    }

    public void removeAirport(Airport airport) {
        this.airports.remove(airport.getCity());
    }

    public void addAirport(Airport airport) {
        this.airports.put(airport.getCity(), airport);
    }

    public Set<Flight> getFlights() {
        return Collections.unmodifiableSet(new HashSet<Flight>(this.flights
                .values()));
    }

    public void removeFlight(Flight flight) {
        this.flights.remove(flight.getNumber());
    }

    public void addFlight(Flight flight) {
        this.flights.put(flight.getNumber(), flight);
    }

    public Set<Passenger> getPassengers() {
        return Collections.unmodifiableSet(new HashSet<Passenger>(
                this.passengers.values()));
    }

    public void removePassenger(Passenger passenger) {
        this.passengers.remove(passenger.getPassport());
    }

    public void addPassenger(Passenger passenger) {
        this.passengers.put(passenger.getPassport(), passenger);
    }

    //
    // Domain logic -----------------------------------------------------------
    //

    /**
     *  Find existing flights from 'origin' to 'destination' ordered by price
     *
     *  @return A SortedSet of flights ordered by price-per-passenger
     */
    public SortedSet<Flight> findAvailableFlights(String origin,
            String destination) {
        SortedSet<Flight> foundFlights =
            new TreeSet<Flight>(new FlightComparator());

        for (Flight flight : this.flights.values()) {
            if (flight.getOrigin().getCity().equalsIgnoreCase(origin)
                && flight.getDestination().getCity().equalsIgnoreCase(destination)) {
                foundFlights.add(flight);
            }
        }

        return foundFlights;
    }

    /**
     *  Create a flight reservation for the cheapest flight
     *  between 'origin' and 'destination'
     *
     *  @return A flight reservation
     */
    public FlightReservation reserveFlight(String origin, String destination,
            String id, String name)
            throws NoFlightAvailableForReservationException {
        // list flights from origin to destination ordered by
        // price-per-passenger (cheapest first)
        SortedSet<Flight> foundFlights = findAvailableFlights(origin,
                destination);

        // throw exception if set is empty
        if (foundFlights.isEmpty()) {
            throw new NoFlightAvailableForReservationException(
                    "No flight available for reservation from " + origin
                            + " to " + destination);
        }

        // use cheapest flight
        Flight flight = foundFlights.first();

        // decide whether it is a new passenger
        Passenger passenger = this.passengers.get(id);
        if (passenger == null) {
            passenger = new Passenger(id, name).init();
            this.addPassenger(passenger);
        }

        // create reservation
        return flight.createReservation(passenger);
    }


    /**
     *  Create a flight reservation for the specified flight
     *
     *  @return A flight reservation
     */
    public FlightReservation reserveFlight(String flightNumber,
            String id, String name)
            throws NoFlightAvailableForReservationException {

        // check received flight number
        if(flightNumber == null && flightNumber.length() == 0) {
            throw new NoFlightAvailableForReservationException(
                "Flight number cannot be empty!");
        }

        // find flight
        Flight flightToBook = null;
        for(Flight flight : this.flights.values()) {
            if(flight.getNumber().equals(flightNumber)) {
                flightToBook = flight;
                break;
            }
        }

        if(flightToBook == null) {
            throw new NoFlightAvailableForReservationException(
                "Flight number " + flightNumber + " not found!");
        }

        // decide whether it is a new passenger
        Passenger passenger = this.passengers.get(id);
        if (passenger == null) {
            passenger = new Passenger(id, name).init();
            this.addPassenger(passenger);
        }

        // create reservation
        return flightToBook.createReservation(passenger);
    }

}
