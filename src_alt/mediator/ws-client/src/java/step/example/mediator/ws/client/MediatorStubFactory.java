package step.example.mediator.ws.client;

import step.example.mediator.wsdl.MediatorPortType;
import step.example.mediator.wsdl.MediatorService;
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
public class MediatorStubFactory extends
		StubFactory<MediatorService, MediatorPortType> {

	//
	// Singleton
	//

	/** Single instance created upon class loading. */
	private static final MediatorStubFactory fINSTANCE = new MediatorStubFactory();

	/** Return singleton instance. */
	public static synchronized MediatorStubFactory getInstance() {
		return fINSTANCE;
	}

	/** Private constructor prevents construction outside this class. */
	private MediatorStubFactory() {
	}

	//
	// base factory methods
	//
	public MediatorService getService() {
		return new MediatorService();
	}

	public MediatorPortType getPort() {
		return this.getService().getMediatorPort();
	}

	//
	// config-based factory methods
	//
	public final static String ENDPOINT_ADDRESS_CONFIG_PARAMETER_NAME = "step.example.mediator.ws.EndpointAddress";

	public MediatorPortType getPortUsingConfig() throws StubFactoryException {
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
