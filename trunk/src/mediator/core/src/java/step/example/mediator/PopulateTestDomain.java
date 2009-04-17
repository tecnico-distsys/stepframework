package step.example.mediator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import step.example.mediator.domain.Client;
import step.example.mediator.domain.Mediator;
import step.example.mediator.exception.MediatorDomainException;
import step.framework.persistence.PersistenceUtil;

public class PopulateTestDomain {
	private static Mediator mediator = null;

	public static void main(String[] args) {

		System.out.println("Mediator application - populate test domain");

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
			mediator = Mediator.getMediator();
			populateClients(session);
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

	protected static void populateClients(Session session)
			throws MediatorDomainException {
		System.out.println("Populating Clients...");
		mediator.createClient("L4532602", "John Williams");
		mediator.createClient("L4588542", "Mark Spencer");
		mediator.createClient("95733331", "Anne Smith");
		mediator.createClient("K0785673", "Jay Nina");
	}

	protected static void populateReservations(Session session)
			throws Exception {
		System.out.println("Populating Mediator Reservations...");

		Client client = mediator.getClient("L4532602");
		client.createReservation("LIS-NY-1-1");

		client = mediator.getClient("L4588542");
		client.createReservation("LIS-NY-1-2");

		client = mediator.getClient("95733331");
		client.createReservation("LIS-NY-1-3");
		client.createReservation("NY-ROC-1-1");

		client = mediator.getClient("K0785673");
		client.createReservation("LIS-NY-1-4");
		client.createReservation("NY-ROC-1-2");

	}

}
