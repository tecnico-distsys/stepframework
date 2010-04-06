package step.examples.tripplanner.mediator.view;

import java.io.Serializable;

public class ClientView implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String identification;
	private String name;
	
	ClientView() { }
	
	public ClientView(String identification, String name) {
		this.identification = identification;
		this.name = name;
	}

	public String getIdentification() {
		return identification;
	}
	
	public String getName() {
		return name;
	}
}
