package step.examples.tripplanner.flight.view;

import java.io.Serializable;

public class AirportInformation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	AirportInformation() { }
	
	public AirportInformation(String iataCode, String icaoCode, String city) {
		this.iataCode = iataCode;
		this.icaoCode = icaoCode;
		this.city = city;
	}

	private String iataCode;
	private String icaoCode;
	private String city;

	public String getIATACode() {
		return iataCode;
	}

	public String getICAOCode() {
		return icaoCode;
	}

	public String getCity() {
		return city;
	}

}
