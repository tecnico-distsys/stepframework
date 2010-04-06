package step.examples.tripplanner.mediator.view;

import java.io.Serializable;

public class AirportInformation implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;
	private String city;
	private String name;

	AirportInformation() { }

	public AirportInformation(String code, String city, String name) {
		this.code = code;
		this.city = city;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getCity() {
		return city;
	}

	public String getName() {
		return name;
	}
}
