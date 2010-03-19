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
            log.trace("Starting SayHelloService action");

			log.debug("create Web Service stub");
			HelloPortType port = HelloStubFactory.getInstance().getPortUsingConfig();

            log.debug("Invoke web service. Parameter = " + name);
			String greeting = port.sayHello(name);

            log.debug("Return value = " + greeting);
			return greeting;

        } catch (ServiceError_Exception see) {
            log.trace("Caught " + see.getClass());
			// remote service error
			log.error(see);
            log.trace("Throw RemoteServiceException with nested exception");
			throw new RemoteServiceException(see);

        } catch (StubFactoryException sfe) {
            log.trace("Caught " + sfe.getClass());
			// stub creation error
			log.error(sfe);
            log.trace("Throw RemoteServiceException with nested exception");
			throw new RemoteServiceException(sfe);

        } catch (WebServiceException wse) {
            log.trace("Caught " + wse.getClass());
			// communication error (wrong address, connection closed, ...)
			log.error(wse);
            log.trace("Throw RemoteServiceException with nested exception");
			throw new RemoteServiceException(wse);

        } finally {
            log.trace("Finally exiting SayHelloService action");
        }
	}

}
