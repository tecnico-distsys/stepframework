package step.framework.wsconfig;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.config.Config;

/**
 *  JAX-WS SOAP Handler that intercepts web service message flow
 *  and configures extensions according to the defined policy.
 */
public class WSConfigHandler  implements SOAPHandler<SOAPMessageContext> {
	
	private static final String ENABLED_PROPERTY_NAME = "wsconfig.enabled";
	private static final String CONFIGURATOR_PROPERTY_NAME = "wsconfig.configurator";
	private static final String DEFAULT_CONFIGURATOR = "step.framework.wsconfig.WSPolicyConfigurator";

    //***********************************************************************
    // Members
	
	/** Web Service Configurator */
	private WSConfigurator configurator;

    /** Logging */
    private Log log;
    
    //***********************************************************************
    // Constructor
    
    public WSConfigHandler() throws WSConfigurationException
    {
    	this.log = LogFactory.getLog(WSConfigHandler.class);
    	
    	initConfigurator();
    }
    
    private void initConfigurator() throws WSConfigurationException
    {
		String className = null;
		
		try
		{
			className = loadClassName();
		}
		catch(WSConfigurationException e)
		{
			configurator = null;
			return;
		}
			
		if(className == null)
			instantiateConfigurator(DEFAULT_CONFIGURATOR);
		else
			instantiateConfigurator(className);
    }
    
    private String loadClassName() throws WSConfigurationException
    {
		log.debug("Trying to load configurator name from config");
		
		try
		{
			Config.getInstance().load();
			String filename = Config.getInstance().getInitParameter(CONFIGURATOR_PROPERTY_NAME);

			if(filename == null)
				log.debug("Loading default configurator");
				
			return filename;
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
        	if(!isEnabled())
            {
                log.trace("WSConfigurator disabled - ignoring message");
            	return true;
            }
            else
        	{
            	return configurator.config();
        	}
        }
        catch(WSConfigurationException e)
        {
        	log.error(e.getMessage(), e);
        	throw new RuntimeException(e);
        }
    }
    
    public boolean handleFault(SOAPMessageContext smc)
    {
    	//TODO: what to do in case of fault???
        return true;
    }

    public void close(MessageContext messageContext)
    {
        log.trace("WSConfigHandler - close()");
        
        try
        {
        	if(!isEnabled())
            {
                log.trace("WSConfigurator disabled - ignoring close");
            	return;
            }
            else
        	{
            	configurator.reset();
        	}
		}
        catch (WSConfigurationException e)
        {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
    }

    //***********************************************************************
    // Auxiliary methods
    
    public boolean isEnabled()
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
}
