package step.examples.tripplanner.flight.view;

import java.io.Serializable;
import java.util.Date;

public class ReservationVoucher implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int reservationCode;
	private String flightNumber;
	private Date flightDate;

	ReservationVoucher() { }
	
	public ReservationVoucher(int reservationCode, String flightNumber,
			Date flightDate) {
		this.reservationCode = reservationCode;
		this.flightNumber = flightNumber;
		this.flightDate = flightDate;
	}

	public int getReservationCode() {
		return reservationCode;
	}
	
	public String getFlightNumber() {
		return flightNumber;
	}

	public Date getFlightDate() {
		return flightDate;
	}
}
