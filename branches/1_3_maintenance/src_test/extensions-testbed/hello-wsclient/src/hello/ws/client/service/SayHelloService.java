package hello.ws.client.service;

import javax.xml.ws.WebServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hello.exception.ws.HelloWSException;
import hello.ws.client.HelloStubFactory;
import hello.wsdl.HelloPortType;
import hello.wsdl.ServiceError_Exception;

import step.framework.exception.RemoteServiceException;
import step.framework.ws.StubFactoryException;

public class SayHelloService extends HelloBaseService<String> {

	/** logging */
	private Log log = LogFactory.getLog(SayHelloService.class);

	/* members */
	private String name;

	/* constructor */
	public SayHelloService(String name) {
		this.name = name;
	}

	@Override
	protected String action() throws HelloWSException {
		try	{
			// create Web Service stub
			HelloPortType port = HelloStubFactory.getInstance().getPortUsingConfig();

			String greeting = port.sayHello(name);

			return greeting;
		} catch (ServiceError_Exception e) {
			// remote service error
			log.error(e);
			throw new RemoteServiceException(e);
		} catch (StubFactoryException e) {
			// stub creation error
			log.error(e);
			throw new RemoteServiceException(e);
		} catch (WebServiceException e) {
			// communication error (wrong address, connection closed, ...)
			log.error(e);
			throw new RemoteServiceException(e);
		}

	}

}
