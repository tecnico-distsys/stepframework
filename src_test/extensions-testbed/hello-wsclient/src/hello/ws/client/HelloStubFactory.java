package hello.ws.client;

import hello.wsdl.HelloPortType;
import hello.wsdl.HelloService;
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
public class HelloStubFactory extends
		StubFactory<HelloService, HelloPortType> {

	//
	// Singleton
	//

	/** Single instance created upon class loading. */
	private static final HelloStubFactory fINSTANCE = new HelloStubFactory();

	/** Return singleton instance. */
	public static synchronized HelloStubFactory getInstance() {
		return fINSTANCE;
	}

	/** Private constructor prevents construction outside this class. */
	private HelloStubFactory() {
	}

	//
	// base factory methods
	//
	public HelloService getService() {
		return new HelloService();
	}

	public HelloPortType getPort() {
		return this.getService().getHelloPort();
	}

	//
	// config-based factory methods
	//
	public final static String ENDPOINT_ADDRESS_CONFIG_PARAMETER_NAME = "hello.ws.EndpointAddress";

	public HelloPortType getPortUsingConfig() throws StubFactoryException {
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
