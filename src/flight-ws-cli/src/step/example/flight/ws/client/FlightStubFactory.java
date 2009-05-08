package step.example.flight.ws.client;

import step.example.flight.wsdl.FlightPortType;
import step.example.flight.wsdl.FlightService;
import step.framework.config.Config;
import step.framework.ws.StubFactory;
import step.framework.ws.StubFactoryException;

/**
 * Flight stub factory
 * 
 * Besides the base getService() and getPort() factory methods it also supports
 * getPortUsingConfig() that reads the endpoint address from a configuration
 * file. See ENDPOINT_ADDRESS_CONFIG_PARAMETER_NAME for property name.
 * 
 * It could also be extended with a getPortUsingRegistry() method to look-up the
 * server location in a Web Services Registry.
 * 
 */
public class FlightStubFactory extends
		StubFactory<FlightService, FlightPortType> {

	//
	// Singleton
	//

	/** Single instance created upon class loading. */
	private static final FlightStubFactory fINSTANCE = new FlightStubFactory();

	/** Return singleton instance. */
	public static synchronized FlightStubFactory getInstance() {
		return fINSTANCE;
	}

	/** Private constructor prevents construction outside this class. */
	private FlightStubFactory() {
	}

	//
	// base factory methods
	//
	public FlightService getService() {
		return new FlightService();
	}

	public FlightPortType getPort() {
		return this.getService().getFlightPort();
	}

	//
	// config-based factory methods
	//
	public final static String ENDPOINT_ADDRESS_CONFIG_PARAMETER_NAME = "step.example.flight.ws.EndpointAddress";

	public FlightPortType getPortUsingConfig() throws StubFactoryException {
		Config config = Config.getInstance();
		String endpointAddress = config
				.getInitParameter(ENDPOINT_ADDRESS_CONFIG_PARAMETER_NAME);

		if (endpointAddress != null)
			// set endpoint address to configuration parameter value
			return getPort(endpointAddress);
		else
			// return default port
			return getPort();
	}

}
