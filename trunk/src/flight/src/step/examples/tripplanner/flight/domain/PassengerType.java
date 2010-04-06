package step.examples.tripplanner.flight.domain;

import java.math.BigDecimal;

public enum PassengerType {
	REGULAR("Regular", "0.0"),
	SILVER("Silver", "0.01"),
	GOLD("Gold", "0.025"),
	PLATINUM("Platinum", "0.05");
	
	private String name;
	private BigDecimal discount;
	
	PassengerType (String name, String discount) {
		this.name = name;
		this.discount = new BigDecimal(discount);
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	
	public String toString() {
		return this.name;
	}
}
