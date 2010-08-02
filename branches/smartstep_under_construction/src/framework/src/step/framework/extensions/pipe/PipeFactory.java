package step.framework.extensions.pipe;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.config.Config;
import step.framework.extensions.Extension;
import step.framework.extensions.ExtensionException;
import step.framework.extensions.ExtensionRepository;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.pipe.properties.PropertiesPipeFactory;
import step.framework.extensions.pipe.policy.PolicyPipeFactory;
import step.framework.service.Service;

public abstract class PipeFactory {
	
	private static final String FACTORY_PROPERTY = "extensions.factory";
	private static final String NULLFACTORY = "null";
	private static final String PROPERTIESFACTORY = "properties";
	private static final String POLICYFACTORY = "policy";

	private static PipeFactory instance;

	@SuppressWarnings("unchecked")
	public static final ServiceInterceptorPipe getServiceInterceptorPipe(Service svc) throws ExtensionException
	{
		createInstance();
		
		ServiceInterceptorPipe pipe = instance.createServiceInterceptorPipe(svc);
		
		if(pipe == null)
			throw new ExtensionException("PipeFactory failed to create a valid pipe.");
		
		return pipe;
	}
	
	public static final WebServiceInterceptorPipe getWebServiceInterceptorPipe(SOAPMessageContext smc) throws ExtensionException
	{
		createInstance();
		
		WebServiceInterceptorPipe pipe = instance.createWebServiceInterceptorPipe(smc);
		
		if(pipe == null)
			throw new ExtensionException("PipeFactory failed to create a valid pipe.");
		
		return pipe;
	}
	
	private static void createInstance() throws ExtensionException
	{
		if(instance != null)
			return;
		
		instance = loadInstance();
		
		if(instance == null)
			throw new ExtensionException("Failed to load PipeFactory");
    }
    
    @SuppressWarnings("unchecked")
	private static PipeFactory loadInstance()
    {
    	String className = null;
		try
		{
			System.out.println("[DEBUG] Loading pipe configuration");
			className = Config.getInstance().getInitParameter(FACTORY_PROPERTY);
			if(className == null || className.equals(NULLFACTORY))
			{
				System.out.println("[DEBUG] Using " + NullPipeFactory.class.getName());
				return null;
			}
			if(className.equals(PROPERTIESFACTORY))
			{
				System.out.println("[DEBUG] Using " + PropertiesPipeFactory.class.getName());
				return new PropertiesPipeFactory();
			}
			if(className.equals(POLICYFACTORY))
			{
				System.out.println("[DEBUG] Using " + PolicyPipeFactory.class.getName());
				return new PolicyPipeFactory();
			}
			//TODO: testing only, remove when done
			if(className.equals("test"))
			{
				System.out.println("[DEBUG] Using " + TestPipeFactory.class.getName());
				return new TestPipeFactory();
			}
			
    		Class<PipeFactory> clazz = (Class<PipeFactory>) Class.forName(className);
			System.out.println("[DEBUG] Using " + clazz.getName());
    		
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
    
    protected List<Extension> getExtensionList(List<String> ids) throws ExtensionException
    {
    	List<Extension> extensions = new ArrayList<Extension>();
    	
    	for(int i=0; i<ids.size(); i++)
		{
			String id = ids.get(i);
			Extension ext = getExtensionRepository().getExtension(id);
			
			if(ext == null)
				throw new ExtensionException("No extension found with id \"" + id + "\"");
			
			extensions.add(ext);
		}
    	
    	return extensions;
    }
    
    protected List<ServiceInterceptor> getServiceInterceptorList(List<Extension> exts) throws ExtensionException
    {
		List<ServiceInterceptor> interceptors = new ArrayList<ServiceInterceptor>();
		
    	for(int i=0; i<exts.size(); i++)
		{
			Extension ext = exts.get(i);
			
			if(!ext.interceptsServices())
				throw new ExtensionException("Extension \"" + ext.getID() + "\" does not intercept services");
			
			interceptors.add(ext.getServiceInterceptor());
		}
    	
    	return interceptors;
    }
    
    protected List<WebServiceInterceptor> getWebServiceInterceptorList(List<Extension> exts) throws ExtensionException
    {
		List<WebServiceInterceptor> interceptors = new ArrayList<WebServiceInterceptor>();
		
    	for(int i=0; i<exts.size(); i++)
		{
			Extension ext = exts.get(i);
			
			if(!ext.interceptsWebServices())
				throw new ExtensionException("Extension \"" + ext.getID() + "\" does not intercept web services");
			
			interceptors.add(ext.getWebServiceInterceptor());
		}
    	
    	return interceptors;
    }

	@SuppressWarnings("unchecked")
	protected abstract ServiceInterceptorPipe createServiceInterceptorPipe(Service svc) throws ExtensionException;
	protected abstract WebServiceInterceptorPipe createWebServiceInterceptorPipe(SOAPMessageContext smc) throws ExtensionException;
}
