package step.examples.tripplanner.mediator;

import step.examples.tripplanner.mediator.service.AddAirportService;
import step.framework.Bootstrap;

public class Setup {
	public static void main(String args[]) throws Exception {
		// initialize Fenix-Framework
		Bootstrap.init();
		try {
			System.out.println("Populating airport list...");
			prepareAirports();
		} catch (Exception ex) {
			System.err.println("Problem setting up domain (" + ex.toString() + ")...");
			System.err.println("\tDid you make changes to an instrumented class?");
			System.err.println("\tMaybe you already populated the domain?...");
			System.err.println("\nCaught exception:");
			ex.printStackTrace();
		}
	}
	
	static void prepareAirports() {
		new AddAirportService("LIS", "Lisboa").execute();
		new AddAirportService("OPO", "Porto", "Francisco Sá Carneiro").execute();
		new AddAirportService("FAO", "Faro").execute();
		new AddAirportService("AMS", "Amsterdão", "Schiphol").execute();
		new AddAirportService("TLS", "Toulouse", "Blagnac").execute();
		new AddAirportService("CDG", "Paris", "Charles de Gaule").execute();
		new AddAirportService("ORY", "Paris", "Orly").execute();
		new AddAirportService("LGW", "London", "Gatwick").execute();
		new AddAirportService("LHR", "London", "Heathrow").execute();
	}
}
