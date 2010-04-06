package step.examples.tripplanner.mediator.view;

import java.io.Serializable;

public class ReservationView implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int code;
	private ClientView client;
	

	ReservationView() { }
	
	public ReservationView(int code, ClientView client) {
		this.code = code;
		
		this.client = client;
	}

	public int getCode() {
		return code;
	}
	
	public ClientView getClient() {
		return client;
	}
}
