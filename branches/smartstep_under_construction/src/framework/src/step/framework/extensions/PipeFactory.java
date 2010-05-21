package step.framework.extensions;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.config.Config;
import step.framework.service.Service;

public abstract class PipeFactory {
	
	private static final String FACTORY_PROPERTY = "extensions.factory";
	private static final String NULLFACTORY = "null";
	private static final String PROPERTIESFACTORY = "properties";
	private static final String POLICYFACTORY = "policy";

	private static PipeFactory instance;

	@SuppressWarnings("unchecked")
	public static final ServiceInterceptorPipe getServiceInterceptorPipe(Service svc)
	{
		createInstance();
		return instance.createServiceInterceptorPipe(svc);
	}
	
	public static final WebServiceInterceptorPipe getWebServiceInterceptorPipe(SOAPMessageContext smc)
	{
		createInstance();
		return instance.createWebServiceInterceptorPipe(smc);
	}
	
	private static void createInstance()
	{
		if(instance != null)
			return;
		
		instance = loadInstance();
		
		if(instance == null)
			instance = new NullPipeFactory();
    }
    
    @SuppressWarnings("unchecked")
	private static PipeFactory loadInstance()
    {
    	String className = null;
		try
		{
			className = Config.getInstance().getInitParameter(FACTORY_PROPERTY);
			if(className == null || className.equals(NULLFACTORY))
				return null;
			if(className.equals(PROPERTIESFACTORY))
				return new PropertiesPipeFactory();
			if(className.equals(POLICYFACTORY))
				return new PolicyPipeFactory();
			
    		Class<PipeFactory> clazz = (Class<PipeFactory>) Class.forName(className);
    		
    		return clazz.newInstance();
		}
		catch(Exception e)
		{
			System.out.println("[DEBUG] Error loading factory: " + className);
			e.printStackTrace();
			return null;
		}
    }
	
	//******************************************************************
	//pipe factory abstract implementation
	
	protected PipeFactory() {
	}
	
	protected ExtensionRepository getExtensionRepository()
	{
		return ExtensionRepository.getInstance();
	}

	@SuppressWarnings("unchecked")
	protected abstract ServiceInterceptorPipe createServiceInterceptorPipe(Service svc);
	protected abstract WebServiceInterceptorPipe createWebServiceInterceptorPipe(SOAPMessageContext smc);
}
