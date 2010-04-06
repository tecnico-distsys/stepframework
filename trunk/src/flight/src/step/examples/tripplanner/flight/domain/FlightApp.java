package step.examples.tripplanner.flight.domain;

import java.util.HashSet;
import java.util.Set;

public class FlightApp extends FlightApp_Base {

	//
	// Domain logic -----------------------------------------------------------
	//

	/**
	 * Registers a new Aircraft.
	 * @param aircraft Aircraft to register
	 * @throws IllegalArgumentException if an aircraft with the same registration already exists
	 */
	@Override
	public void addAircraft(Aircraft aircraft) {
		// check for existing airport with same IATA or ICAO codes
		String registration = aircraft.getAircraftRegistration();
		Aircraft existing = getAircraft(registration);
		
		if (existing != null && !aircraft.equals(existing)) {
			throw new IllegalArgumentException(registration);
		} else {
			super.addAircraft(aircraft);
		}
	}

	/**
	 * Registers a new Airport.
	 * @param airport Airport to register
	 * @throws IllegalArgumentException if an airport with the same IATA or ICAO code is already
	 *		   registered.
	 */
	@Override
	public void addAirport(Airport airport) {
		// check for existing airport with same IATA or ICAO codes
		String code = airport.getIATACode();
		Airport existing = getAirport(code);

		if (existing == null) {
			code = airport.getICAOCode();
			existing = getAirport(code);
		}
		
		if (existing != null && !airport.equals(existing)) {
			throw new IllegalArgumentException(code);
		} else {
			super.addAirport(airport);
		}
	}

	/**
	 * Obtains an aircraft, given it's registration.
	 * @param registration aircraft registration number
	 * @return the aircraft with the given registration number, or <code>null</code> if no aircraft
	 *         is found
	 */
	public Aircraft getAircraft(String registration) {
		for (Aircraft aircraft : getAircraftSet()) {
			if (aircraft.getAircraftRegistration().equals(registration)) {
				return aircraft;
			}
		}
		return null;
	}

	/**
	 * Obtains an airport, given it's IATA or ICAO codes.
	 * @param code IATA or ICAO code of required airport
	 * @return the airport with the given code, or <code>null</code> if no airport is found
	 */
	public Airport getAirport(String code) {
		if (!Airport.isValidCode(code)) {
			throw new IllegalArgumentException(code);
		}

		for (Airport airport : getAirportSet()) {
			if (airport.getIATACode().equals(code) || airport.getICAOCode().equals(code)) {
				return airport;
			}
		}
		return null;
	}
	
	/**
	 * Obtains all airports in a given city.
	 * @param city name of the city to search for airports
	 * @return the set of airports found in the city
	 */
	public Set<Airport> getAirportsInCity(String city) {
		Set<Airport> airports = new HashSet<Airport>();
		
		for (Airport airport : getAirportSet()) {
			if (airport.getCity().equals(city)) {
				airports.add(airport);
			}
		}
		
		return airports;
	}
	
	public Set<Airport> findAirport(String codeOrCity) {
		Set<Airport> airports;
		
		if (Airport.isValidCode(codeOrCity)) {
			// IATA/ICAO code search
			airports = new HashSet<Airport>();
			airports.add(getAirport(codeOrCity));
		} else {
			// City search
			airports = getAirportsInCity(codeOrCity);
		}

		return airports;
	}
}
