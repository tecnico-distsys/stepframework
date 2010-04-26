package step.framework.wsconfig;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.config.Config;
import step.framework.extensions.WebServiceInterceptorManager;
import step.framework.ws.SOAPUtil;

/**
 *  JAX-WS SOAP Handler that intercepts web service message flow
 *  and configures extensions according to the defined policy.
 */
public class WSConfigHandler  implements SOAPHandler<SOAPMessageContext> {
	
	private static final String ENABLED_PROPERTY_NAME = "wsconfig.enabled";
	private static final String CONFIGURATOR_PROPERTY_NAME = "wsconfig.configurator";
	private static final String DEFAULT_CONFIGURATOR = "step.framework.wsconfig.policy.WSPolicyConfigurator";

    //***********************************************************************
    // Members

    /** Web Service interceptor manager */
    private WebServiceInterceptorManager manager;
    
	/** Web Service Configurator */
	private WSConfigurator configurator;

    /** Logging */
    private Log log = LogFactory.getLog(WSConfigHandler.class);
    
    //***********************************************************************
    // Constructor
    
    public WSConfigHandler() throws WSConfigurationException
    {
        this.manager = new WebServiceInterceptorManager();    	
    	initConfigurator();
    }
    
    private void initConfigurator() throws WSConfigurationException
    {
		String className = loadClassName();
			
		if(className == null)
		{
			log.debug("Loading default configurator");
			instantiateConfigurator(DEFAULT_CONFIGURATOR);
		}
		else
			instantiateConfigurator(className);
    }
    
    private String loadClassName() throws WSConfigurationException
    {
		log.debug("Trying to load configurator name from config");
		
		try
		{
			Config.getInstance().load();
			return Config.getInstance().getInitParameter(CONFIGURATOR_PROPERTY_NAME);
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
    }
    
    @SuppressWarnings("unchecked")
	private void instantiateConfigurator(String className) throws WSConfigurationException
    {
		log.debug("Initializing configurator \"" + className + "\"");
		
    	try
    	{
    		Class<WSConfigurator> clazz = (Class<WSConfigurator>) Class.forName(className);
    		
    		this.configurator = clazz.newInstance();
    	}
    	catch(Exception e)
    	{
    		throw new WSConfigurationException("Could not initialize configurator \"" + className + "\"", e);
    	}
    	
		log.debug("Configurator \"" + className + "\" successfully initialized");
    }

    //***********************************************************************
    // SOAPHandler interface
    
    public Set<QName> getHeaders()
    {
        log.trace("WSConfigHandler - getHeaders()");
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc)
    {
        log.trace("WSConfigHandler - handleMessage()");
                
        try
        {
        	//TODO: save extensions state before
        	if(isEnabled())
        		configure(smc);
        	
        	boolean extensionsResult = manager.interceptHandleMessageWebServiceHandler(smc);

        	if(isEnabled())
        		reset();
        	
        	return extensionsResult; 
        }
        catch(WSConfigurationException e)
        {
        	log.error(e.getMessage(), e);
        	throw new RuntimeException(e);
        }
    }
    
    public boolean handleFault(SOAPMessageContext smc)
    {
    	//TODO: configure in case of fault???
    	
        log.trace("WSConfigHandler - handleFault()");
        return manager.interceptHandleFaultWebServiceHandler(smc);
    }

    public void close(MessageContext messageContext)
    {
        log.trace("WSConfigHandler - close()");
        manager.interceptCloseWebServiceHandler(messageContext);        
    }

    //***********************************************************************
    // Auxiliary methods
    
    private boolean isEnabled()
    {
    	try
    	{
    		Config.getInstance().load();
    		String enabled = Config.getInstance().getInitParameter(ENABLED_PROPERTY_NAME);
    		
    		return Boolean.parseBoolean(enabled);
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    }
    
    private void configure(SOAPMessageContext smc) throws WSConfigurationException
    {
		boolean isServerSide = SOAPUtil.isServerSideMessage(smc);
		boolean isOutbound = SOAPUtil.isOutboundMessage(smc);		

		if(!isServerSide && isOutbound)
			configurator.configClientOutbound(smc);
		if(isServerSide && !isOutbound)
			configurator.configServerInbound(smc);
		if(isServerSide && isOutbound)
			configurator.configServerOutbound(smc);
		if(!isServerSide && !isOutbound)
			configurator.configClientInbound(smc);
    }
    
	private void reset() throws WSConfigurationException
    {
    	try
    	{
    		//TODO: reset extension engine to state before configuration
    	}
    	catch(Exception e)
    	{
    		throw new WSConfigurationException(e);
    	}
    }
}
