package step.framework.extensions;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;

public final class Extension {

    public static final String LOCAL_CONFIG_PREFIX = "/extension-";
    public static final String LOCAL_CONFIG_SUFFIX = ".properties";
	
	public static final int GLOBAL_PROPERTY_LEVEL = 0;
	public static final int LOCAL_PROPERTY_LEVEL = 1;
	public static final int CONTEXT_PROPERTY_LEVEL = 2;
	public static final int RECURSIVE_PROPERTY_LEVEL = 3;
	
	private String id;
	
	private ExtensionListener extensionListener;
	private ServiceInterceptor serviceInterceptor;
	private WebServiceInterceptor webServiceInterceptor;
	
	private QName[] wspNamespaces;
	
	private String localPropPath;
	private long localLastModified;
	
	private Properties globalProp;
	private Properties localProp;
	private Map<String, Object> contextProp;	
	
	Extension(String id, QName[] wspNamespaces, Properties globalProp)
	{
		this.id = id;
		this.wspNamespaces = (wspNamespaces == null) ? new QName[0] : wspNamespaces;
		
		this.extensionListener = null;
		this.serviceInterceptor = null;
		this.webServiceInterceptor = null;
		
		this.localPropPath = LOCAL_CONFIG_PREFIX + id + LOCAL_CONFIG_SUFFIX;
		this.localLastModified = 0;
		
		this.globalProp = (globalProp == null) ? new Properties() : globalProp;
		loadLocalProp();
		this.contextProp = new HashMap<String, Object>();
	}
	
	public String getID()
	{
		return id;
	}
	
	public QName[] getWSPNamespaces()
	{
		return wspNamespaces;
	}
    
    //***********************************************************************
    // Lifecycle
	
	void init() throws ExtensionException
	{
		if(extensionListener != null)
			extensionListener.extensionInitialized();
	}
	
	void destroy() throws ExtensionException
	{
		if(extensionListener != null)
			extensionListener.extensionDestroyed();
	}
    
    //***********************************************************************
    // Properties
	
	public Object getProperty(String name)
	{
		return getProperty(name, RECURSIVE_PROPERTY_LEVEL);
	}
	
	public void setProperty(String name, Object value)
	{
		contextProp.put(name, value);
	}
	
	public Object getProperty(String name, int level)
	{
		switch(level)
		{
			case GLOBAL_PROPERTY_LEVEL:
			{
				return globalProp.getProperty(name);
			}
			case LOCAL_PROPERTY_LEVEL:
			{
				loadLocalProp();
				return localProp.getProperty(name);
			}
			case CONTEXT_PROPERTY_LEVEL:
			{
				return contextProp.get(name);
			}
			case RECURSIVE_PROPERTY_LEVEL:
			{
				Object value = null;
				if((value = getProperty(name, CONTEXT_PROPERTY_LEVEL)) == null)
					if((value = getProperty(name, LOCAL_PROPERTY_LEVEL)) == null)
						value = getProperty(name, GLOBAL_PROPERTY_LEVEL);

				return value;
			}
			default:
			{
				return getProperty(name, RECURSIVE_PROPERTY_LEVEL);
			}
		}
	}
    
    //***********************************************************************
    // Member configuration
	
	void setExtensionListener(ExtensionListener extensionListener)
	{
		this.extensionListener = extensionListener;
	}
	
	ExtensionListener getExtensionListener()
	{
		return extensionListener;
	}
	
	void setServiceInterceptor(ServiceInterceptor serviceInterceptor)
	{
		this.serviceInterceptor = serviceInterceptor;
	}
	
	public ServiceInterceptor getServiceInterceptor()
	{
		return serviceInterceptor;
	}
	
	public boolean interceptsServices()
	{
		return serviceInterceptor != null;
	}
	
	void setWebServiceInterceptor(WebServiceInterceptor webServiceInterceptor)
	{
		this.webServiceInterceptor = webServiceInterceptor;
	}
	
	public boolean interceptsWebServices()
	{
		return webServiceInterceptor != null;
	}
	
	public WebServiceInterceptor getWebServiceInterceptor()
	{
		return webServiceInterceptor;
	}
    
    //***********************************************************************
    // Aux
	
	private void loadLocalProp()
	{
		try
		{
			URL url = Extension.class.getResource(localPropPath);

			if(url == null)
			{
				localProp = new Properties();
				return;
			}
			
			File file = new File(url.toURI());
			long lastModified = file.lastModified();
			
			if(lastModified <= localLastModified)
				return;
			
			System.out.println("[PROPERTIES] loading local properties");
			localProp = new Properties();
			localProp.load(new FileInputStream(file));
			
	    	localLastModified = lastModified;
	    	
	        if(localProp == null)
	        	localProp = new Properties();
		}
		catch(Exception e)
		{
			System.out.println("No local properties file found.");
			localProp = new Properties();
			return;
		}
	}
}
