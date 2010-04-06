package step.examples.tripplanner.flight;

import java.math.BigDecimal;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import step.framework.Bootstrap;

import step.examples.tripplanner.flight.service.*;


public class Setup {
	public static void main(String args[]) throws Exception {
		// initialize Fenix-Framework
		Bootstrap.init();
		try {
			System.out.println("Populating airport list...");
			prepareAirports();
			System.out.println("Setting up airline...");
			newAirline();
			System.out.println("Registering fleet...");
			defineFlights();
			System.out.println("Defining flight availability throughout the year...");
			defineAirplanes();
			System.out.println("Defining operated flights...");
			defineFlightInstances();
		} catch (Exception ex) {
			System.err.println("Problem setting up domain (" + ex.toString() + ")...");
			System.err.println("\tDid you make changes to an instrumented class?");
			System.err.println("\tMaybe you already populated the domain?...");
			System.err.println("\nCaught exception:");
			ex.printStackTrace();
		}
	}
	
	static void prepareAirports() {
		new AddAirportService("LIS", "LPPT", "Lisboa", BigDecimal.valueOf(2628)).execute();
		new AddAirportService("OPO", "LPPR", "Porto", BigDecimal.valueOf(267776,2)).execute();
		new AddAirportService("FAO", "LPFR", "Faro", BigDecimal.valueOf(266496,2)).execute();
		new AddAirportService("AMS", "EHAM", "Amsterdão", BigDecimal.valueOf(902122,2)).execute();
		new AddAirportService("TLS", "LFBO", "Toulouse", BigDecimal.valueOf(23004,1)).execute();
	}

	static void newAirline() {
		new CreateAirlineService("TAP Portugal", "TP").execute();
	}

	static void defineFlights() {
		// Lisboa <-> Porto
		new DefineFlightService(1950, "LIS", "OPO", new LocalTime( 7,  5)).execute();
		new DefineFlightService(1952, "LIS", "OPO", new LocalTime( 8, 10)).execute();
		new DefineFlightService(1956, "LIS", "OPO", new LocalTime( 9, 15)).execute();
		new DefineFlightService(1976, "LIS", "OPO", new LocalTime(10, 20)).execute();
		new DefineFlightService(1960, "LIS", "OPO", new LocalTime(13,  0)).execute();
		new DefineFlightService(1970, "LIS", "OPO", new LocalTime(15, 45)).execute();
		new DefineFlightService(1978, "LIS", "OPO", new LocalTime(18,  0)).execute();
		new DefineFlightService(1980, "LIS", "OPO", new LocalTime(20, 10)).execute();
		new DefineFlightService(1988, "LIS", "OPO", new LocalTime(21, 55)).execute();
		new DefineFlightService(1951, "OPO", "LIS", new LocalTime( 6,  5)).execute();
		new DefineFlightService(1953, "OPO", "LIS", new LocalTime( 7, 45)).execute();
		new DefineFlightService(1957, "OPO", "LIS", new LocalTime( 8, 40)).execute();
		new DefineFlightService(1961, "OPO", "LIS", new LocalTime(10, 55)).execute();
		new DefineFlightService(1963, "OPO", "LIS", new LocalTime(12, 50)).execute();
		new DefineFlightService(1967, "OPO", "LIS", new LocalTime(14, 40)).execute();
		new DefineFlightService(1971, "OPO", "LIS", new LocalTime(16, 30)).execute();
		new DefineFlightService( 675, "OPO", "LIS", new LocalTime(18, 30)).execute();
		new DefineFlightService(1983, "OPO", "LIS", new LocalTime(20,  5)).execute();
		
		// Lisboa <-> Faro
		new DefineFlightService(1907, "LIS", "FAO", new LocalTime( 9, 35)).execute();
		new DefineFlightService(1913, "LIS", "FAO", new LocalTime(15, 55)).execute();
		new DefineFlightService(1917, "LIS", "FAO", new LocalTime(21, 50)).execute();
		new DefineFlightService(1900, "FAO", "LIS", new LocalTime( 6,  5)).execute();
		new DefineFlightService(1908, "FAO", "LIS", new LocalTime(11,  5)).execute();
		new DefineFlightService(1912, "FAO", "LIS", new LocalTime(17, 25)).execute();

		// Lisboa <-> Toulouse
		new DefineFlightService( 418, "LIS", "TLS", new LocalTime( 8,  0)).execute();
		new DefineFlightService( 422, "LIS", "TLS", new LocalTime(14,  5)).execute();
		new DefineFlightService( 421, "TLS", "LIS", new LocalTime(11, 40)).execute();
		new DefineFlightService( 423, "TLS", "LIS", new LocalTime(17, 45)).execute();
		
		// Porto <-> Amsterdão
		new DefineFlightService( 652, "OPO", "AMS", new LocalTime( 9, 45)).execute();
		new DefineFlightService( 651, "AMS", "OPO", new LocalTime(14, 20)).execute();
		
		// Lisboa <-> Amsterdão
		new DefineFlightService( 668, "LIS", "AMS", new LocalTime( 7, 50)).execute();
		new DefineFlightService( 660, "LIS", "AMS", new LocalTime(14,  5)).execute();
		new DefineFlightService( 665, "AMS", "LIS", new LocalTime(12, 35)).execute();
		new DefineFlightService( 661, "AMS", "LIS", new LocalTime(18, 50)).execute();
	}

	static void defineAirplanes() {
		new AddAirplaneService("CS-TPL", "Embraier", "ERJ 145 EP", 45, BigDecimal.valueOf(3295)).execute();
		new AddAirplaneService("CS-TTQ", "Airbus", "A319-111", 128, BigDecimal.valueOf(9510)).execute();
		new AddAirplaneService("CS-TTR", "Airbus", "A319-112", 128, BigDecimal.valueOf(9450)).execute();
		new AddAirplaneService("CS-TNE", "Airbus", "A320-211", 157, BigDecimal.valueOf(11390)).execute();	
		new AddAirplaneService("CS-TNH", "Airbus", "A320-214", 157, BigDecimal.valueOf(11240)).execute();
		new AddAirplaneService("CS-TOM", "Airbus", "A330-200", 268, BigDecimal.valueOf(25000)).execute();
	}

	static void defineFlightInstances() {
		//Lisboa <-> Toulouse
		new DefineFlightInstanceService(418, new LocalDate(2010,3,10), "CS-TPL").execute();
		new DefineFlightInstanceService(421, new LocalDate(2010,3,10), "CS-TPL").execute();
		new DefineFlightInstanceService(422, new LocalDate(2010,3,10), "CS-TPL").execute();
		new DefineFlightInstanceService(423, new LocalDate(2010,3,10), "CS-TPL").execute();

		//Porto <-> Amsterdão
		new DefineFlightInstanceService(652, new LocalDate(2010,3,15), "CS-TNE").execute();
		new DefineFlightInstanceService(651, new LocalDate(2010,3,15), "CS-TNH").execute();
		new DefineFlightInstanceService(652, new LocalDate(2010,4,24), "CS-TNH").execute();
		new DefineFlightInstanceService(651, new LocalDate(2010,4,24), "CS-TNE").execute();

		//Lisboa <-> Amsterdão
		new DefineFlightInstanceService(668, new LocalDate(2010,4,24), "CS-TTR").execute();
		new DefineFlightInstanceService(665, new LocalDate(2010,4,24), "CS-TTR").execute();
		new DefineFlightInstanceService(660, new LocalDate(2010,4,24), "CS-TTQ").execute();
		new DefineFlightInstanceService(661, new LocalDate(2010,4,24), "CS-TTQ").execute();
	}
}
