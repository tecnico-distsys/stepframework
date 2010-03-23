package step.framework.ws.handler;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.sun.xml.ws.handler.HandlerException;

import step.framework.config.Config;
import step.framework.config.ConfigException;

/**
 * This JAX-WS Handler can be configured to throw a runtime exception type
 * in any of the SOAP message handling methods. It is useful for testing
 * proper error handling in the handler processing chain.<br /><br />
 *
 * The throw method and runtime exception classname are specified 
 * in the "exception-thrower-handler.properties" file.
 */
public class ExceptionThrowerHandler implements SOAPHandler<SOAPMessageContext> {

    //
    // Handler interface methods
    // 
	public Set<QName> getHeaders() {
        System.out.println(this + "> getHeaders()");
		return null;
	}

	public boolean handleMessage(SOAPMessageContext context) {
        System.out.println(this + "> handleMessage()");
        exceptionThrower("handleMessage", getMessageDirection(context));
        return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
        System.out.println(this + "> handleFault()");
       	exceptionThrower("handleFault", getMessageDirection(context));
        return true;
	}

	public void close(MessageContext messageContext) {
        System.out.println(this + "> close()");
	    return;
	}


    //
    // Private implementation members and methods
    //
    private static final String PROPERTIES_FILE = "/exception-thrower-handler.properties";

	private static final String THROW_METHOD_PROPERTY = "throw-method";
	private static final String MESSAGE_DIRECTION_PROPERTY = "message-direction";
	private static final String RUNTIME_EXCEPTION_CLASSNAME_PROPERTY = "runtime-exception-classname";

	private static final String MESSAGE_DIRECTION_INBOUND = "inbound";
	private static final String MESSAGE_DIRECTION_OUTBOUND = "outbound";
	private static final String MESSAGE_DIRECTION_INOUT = "inout";


	/** method name in which the exception should be thrown. If null, no exception is thrown. */
    private String throwMethod;

    /** direction of the message: inbound, outbound or inout (throw exception
     * in both cases) */
    private String messageDirection;

    /** 
     * Full classname of the runtime exception to throw.
     * Type must be a java.lang.RuntimeException.
     * If null, HandlerException is thrown.
     */
    private String runtimeExceptionClassname;


	@PostConstruct
    public void init() {
		// loading configuration file
    	Config config = Config.getInstance();
    	try {
			config.load(PROPERTIES_FILE);

			// fetching necessary properties
			throwMethod = config.getInitParameter(THROW_METHOD_PROPERTY);
			messageDirection = config.getInitParameter(MESSAGE_DIRECTION_PROPERTY);
			runtimeExceptionClassname = config.getInitParameter(RUNTIME_EXCEPTION_CLASSNAME_PROPERTY);

			System.out.println(THROW_METHOD_PROPERTY + "=" + throwMethod);
			System.out.println(MESSAGE_DIRECTION_PROPERTY + "=" + messageDirection);
			System.out.println(RUNTIME_EXCEPTION_CLASSNAME_PROPERTY + "=" + runtimeExceptionClassname);

		} catch (ConfigException e) {
			System.out.println(this +  "> caught exception: " + e.getMessage());
		}
    }
    
    private void exceptionThrower(String methodName, String direction) {
        if (throwMethod == null || methodName == null || 
        		!throwMethod.equals(methodName) ||
        		!messageDirection.equals(MESSAGE_DIRECTION_INOUT) && !messageDirection.equals(direction))
            // do nothing
            return;

        if(runtimeExceptionClassname == null)
            throw new HandlerException("This exception is being thrown by a handler for testing purposes!");

        Class ec = null;
        RuntimeException rte = null;
        try {
        	// search and instantiate the runtime exception class
            ec = Class.forName(runtimeExceptionClassname);
            rte = (RuntimeException) ec.newInstance();
        } catch(Exception e) {
            // catch, among other exceptions: ClassNotFoundException,
            // InstantiationException, IllegalAccessException
            System.out.println(this + "> could not throw " +
                               runtimeExceptionClassname  +
                               " in method " +
                               throwMethod +
                               " because of " +
                               e.getClass().toString() +
                               " with message " +
                               e.getMessage());
        }

    	// throw the instance of the runtime exception class
        if (rte != null) {
            System.out.println(this + "> throwing runtime exception " + rte.getClass());
            throw rte;
        }
    }

	private String getMessageDirection(MessageContext context) {
        boolean out = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		return out ? MESSAGE_DIRECTION_OUTBOUND : MESSAGE_DIRECTION_INBOUND;
	}

}
