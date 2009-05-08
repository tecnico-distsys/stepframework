package step.example.flight;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import step.example.flight.domain.Airplane;
import step.example.flight.domain.Airport;
import step.example.flight.domain.Flight;
import step.example.flight.domain.FlightManager;
import step.example.flight.domain.FlightReservation;
import step.example.flight.domain.Passenger;
import step.framework.persistence.PersistenceUtil;

public class PopulateTestDomain {
	private static FlightManager manager = null;

	public static void main(String[] args) {

		System.out.println("Flight application - populate test domain");

		// create session and begin transaction
		//
		Session session = null;
		try {
			SessionFactory sessionFactory = PersistenceUtil.getSessionFactory();
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
		} catch (Exception e) {
			System.out
					.println("Error creating persistence session or transaction");
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
			return;
		}

		// populate database
		//
		boolean commit = false;
		try {
			manager = FlightManager.getInstance();
			populateAirports(session);
			populateAirplanes(session);
			populatePassengers(session);
			populateFlights(session);
			populateReservations(session);
			commit = true;
		} catch (ConstraintViolationException e) {
			System.out
					.println("Error populating domain due to constraints violation");
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
			System.out
					.println("This probably means that the domain is already populated");
		} catch (Exception e) {
			System.out.println("Error populating domain");
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
			commit = false;
		}

		// end transaction
		//
		try {
			if (commit) {
				System.out.println("Commit changes");
				session.getTransaction().commit();
			} else {
				System.out.println("Rollback changes");
				session.getTransaction().rollback();
			}
		} catch (Exception e) {
			System.out.println("Error populating domain");
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
		}

	}

	protected static void populateAirports(Session session) {
		System.out.println("Populating Airports...");
		{
			Airport airport = new Airport("Lisbon", new BigDecimal("1000"))
					.init();
			manager.addAirport(airport);
		}
		{
			Airport airport = new Airport("London", new BigDecimal("2000"))
					.init();
			manager.addAirport(airport);
		}
		{
			Airport airport = new Airport("New York", new BigDecimal("2500"))
					.init();
			manager.addAirport(airport);
		}
		{
			Airport airport = new Airport("Rochester", new BigDecimal("1500"))
					.init();
			manager.addAirport(airport);
		}
		{
			Airport airport = new Airport("Seoul", new BigDecimal("2150"))
					.init();
			manager.addAirport(airport);
		}
	}

	protected static void populateAirplanes(Session session) {
		System.out.println("Populating Airplanes...");
		{
			Airplane airplane = new Airplane("XY100", "Boeing 767", 220,
					new BigDecimal("10000")).init();
			manager.addAirplane(airplane);
		}
		{
			Airplane airplane = new Airplane("ZX200", "Airbus A320", 235,
					new BigDecimal("10350")).init();
			manager.addAirplane(airplane);
		}
		{
			Airplane airplane = new Airplane("XP300", "Airbus A380", 400,
					new BigDecimal("20000")).init();
			manager.addAirplane(airplane);
		}
	}

	protected static void populatePassengers(Session session) {
		System.out.println("Populating Passengers...");
		{
			Passenger passenger = new Passenger("L4532602", "John Williams")
					.init();
			manager.addPassenger(passenger);
		}
		{
			Passenger passenger = new Passenger("L4588542", "Mark Spencer")
					.init();
			manager.addPassenger(passenger);
		}
		{
			Passenger passenger = new Passenger("95733331", "Anne Smith")
					.init();
			manager.addPassenger(passenger);
		}
		{
			Passenger passenger = new Passenger("K0785673", "Jay Nina").init();
			manager.addPassenger(passenger);
		}
	}

	protected static void populateFlights(Session session) {
		System.out.println("Populating Flights...");
		{
			Airport origin = null;
			{
				Criteria criteria = session.createCriteria(Airport.class);
				criteria.add(Restrictions.eq("city", "Lisbon"));
				origin = (Airport) criteria.uniqueResult();
			}
			Airport destination = null;
			{
				Criteria criteria = session.createCriteria(Airport.class);
				criteria.add(Restrictions.eq("city", "New York"));
				destination = (Airport) criteria.uniqueResult();
			}
			Airplane airplane = null;
			{
				Criteria criteria = session.createCriteria(Airplane.class);
				criteria.add(Restrictions.eq("registration", "XY100"));
				airplane = (Airplane) criteria.uniqueResult();
			}
			Date dateTime = null;
			{
				Calendar calendar = new GregorianCalendar();
				calendar.set(2008, 6, 22, 8, 0, 0);
				dateTime = calendar.getTime();
			}

			Flight flight = new Flight("LIS-NY-1", dateTime, new BigDecimal(
					"1.2"), origin, destination, airplane, 4).init();
			manager.addFlight(flight);
		}
		{
			Airport origin = null;
			{
				Criteria criteria = session.createCriteria(Airport.class);
				criteria.add(Restrictions.eq("city", "New York"));
				origin = (Airport) criteria.uniqueResult();
			}
			Airport destination = null;
			{
				Criteria criteria = session.createCriteria(Airport.class);
				criteria.add(Restrictions.eq("city", "Rochester"));
				destination = (Airport) criteria.uniqueResult();
			}
			Airplane airplane = null;
			{
				Criteria criteria = session.createCriteria(Airplane.class);
				criteria.add(Restrictions.eq("registration", "XP300"));
				airplane = (Airplane) criteria.uniqueResult();
			}
			Date dateTime = null;
			{
				Calendar calendar = new GregorianCalendar();
				calendar.set(2008, 7, 22, 9, 30, 0);
				dateTime = calendar.getTime();
			}

			Flight flight = new Flight("NY-ROC-1", dateTime, new BigDecimal(
					"1.3"), origin, destination, airplane, 2).init();
			manager.addFlight(flight);
		}
	}

	protected static void populateReservations(Session session) {
		System.out.println("Populating Flight Reservations...");
		{
			Flight flight = null;
			{
				Criteria criteria = session.createCriteria(Flight.class);
				criteria.add(Restrictions.eq("number", "LIS-NY-1"));
				flight = (Flight) criteria.uniqueResult();
			}
			{
				Criteria criteria = session.createCriteria(Passenger.class);
				criteria.add(Restrictions.eq("passport", "L4532602"));
				Passenger passenger = (Passenger) criteria.uniqueResult();

				FlightReservation flightReservation = new FlightReservation(
						flight.getNumber() + "-1", passenger, flight).init();
				flight.addReservation(flightReservation);
			}
			{
				Criteria criteria = session.createCriteria(Passenger.class);
				criteria.add(Restrictions.eq("passport", "L4588542"));
				Passenger passenger = (Passenger) criteria.uniqueResult();

				FlightReservation flightReservation = new FlightReservation(
						flight.getNumber() + "-2", passenger, flight).init();
				flight.addReservation(flightReservation);
			}
			{
				Criteria criteria = session.createCriteria(Passenger.class);
				criteria.add(Restrictions.eq("passport", "95733331"));
				Passenger passenger = (Passenger) criteria.uniqueResult();

				FlightReservation flightReservation = new FlightReservation(
						flight.getNumber() + "-3", passenger, flight).init();
				flight.addReservation(flightReservation);
			}
			{
				Criteria criteria = session.createCriteria(Passenger.class);
				criteria.add(Restrictions.eq("passport", "K0785673"));
				Passenger passenger = (Passenger) criteria.uniqueResult();

				FlightReservation flightReservation = new FlightReservation(
						flight.getNumber() + "-4", passenger, flight).init();
				flight.addReservation(flightReservation);
			}
		}
		{
			Flight flight = null;
			{
				Criteria criteria = session.createCriteria(Flight.class);
				criteria.add(Restrictions.eq("number", "NY-ROC-1"));
				flight = (Flight) criteria.uniqueResult();
			}
			{
				Criteria criteria = session.createCriteria(Passenger.class);
				criteria.add(Restrictions.eq("passport", "95733331"));
				Passenger passenger = (Passenger) criteria.uniqueResult();

				FlightReservation flightReservation = new FlightReservation(
						flight.getNumber() + "-1", passenger, flight).init();
				flight.addReservation(flightReservation);
			}
			{
				Criteria criteria = session.createCriteria(Passenger.class);
				criteria.add(Restrictions.eq("passport", "K0785673"));
				Passenger passenger = (Passenger) criteria.uniqueResult();

				FlightReservation flightReservation = new FlightReservation(
						flight.getNumber() + "-2", passenger, flight).init();
				flight.addReservation(flightReservation);
			}
		}
	}

}
