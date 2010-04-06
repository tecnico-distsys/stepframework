package step.examples.tripplanner.flight.domain;

import java.math.BigDecimal;

public class Airport extends Airport_Base {

	public Airport(String iata, String icao, String city, BigDecimal costPerUse) {
		setIATACode(iata);
		setICAOCode(icao);
		setCity(city);
		setCostPerUse(costPerUse);
	}

	//
	// Domain logic -----------------------------------------------------------
	//

	public static boolean isValidCode(String code) {
		return code.matches("[a-zA-Z]*") && code.length() >= 3 && code.length() <= 4;
	}

	/**
	 * @throws IllegalArgumentException if IATA code is not a 3-letter code, or if the airport is
	 *		   already registered, as the code is read-only.
	 */
	@Override
	public void setIATACode(String code) throws IllegalArgumentException {
		if (!(isValidCode(code) && code.length() == 3) 
			|| getFlightApp() != null) {
			throw new IllegalArgumentException(code);
		} else {
			super.setIATACode(code);
		}
	}
	
	/**
	 * @throws IllegalArgumentException if the airport is already registered the code is read-only.
	 */
	@Override
	public void setICAOCode(String code) throws IllegalArgumentException {
		if (!(code.length() == 4 && code.matches("[a-zA-Z]*"))
			|| getFlightApp() != null) {
			throw new IllegalArgumentException(code);
		} else {
			super.setICAOCode(code);
		}
	}

	//
	// Output -----------------------------------------------------------------
	//

	public String toString() {
		return "Airport: IATA/ICAO code=" + getIATACode() + "/" + getICAOCode()
				+ " city=" + getCity() + " cost-per-use=" + getCostPerUse();
	}
}
