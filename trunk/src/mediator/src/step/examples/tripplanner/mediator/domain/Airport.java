package step.examples.tripplanner.mediator.domain;

public class Airport extends Airport_Base {

	public Airport(String code, String city) {
		this(code, city, "");
	}

	public Airport(String code, String city, String name) {
		setCode(code);
		setCity(city);
		setName(name);
	}

	//
	// Domain logic -----------------------------------------------------------
	//

	/**
	 * Check if the code given could be a valid IATA code.
	 * 
	 * @param code the code to check
	 * @return true if it received a valid IATA code (any three-letter code is considered valid)
	 */
	public static boolean isValidCode(String code) {
		return code.matches("[a-zA-Z]*") && code.length() == 3;
	}

	/**
	 * Sets the Airport IATA code, if it's valid and not already registered.
	 * 
	 * @throws IllegalArgumentException if IATA code is not a 3-letter code, or if the airport is
	 *		   already registered, as the code is read-only.
	 */
	@Override
	public void setCode(String code) throws IllegalArgumentException {
		if (!isValidCode(code) || getMediator() != null) {
			throw new IllegalArgumentException(code);
		} else {
			super.setCode(code);
		}
	}
	
}
