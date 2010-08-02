package step.framework.extensions.pipe.properties;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.config.tree.ConfigTree;
import step.framework.extensions.Extension;
import step.framework.extensions.ExtensionException;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.pipe.PipeFactory;
import step.framework.extensions.pipe.ServiceInterceptorPipe;
import step.framework.extensions.pipe.WebServiceInterceptorPipe;
import step.framework.service.Service;

public class PropertiesPipeFactory extends PipeFactory {
    
    //***********************************************************************
    // Config constants

    /** Default extensions properties file resource path */
    public static final String DEFAULT_EXTENSIONS_RESOURCE_PATH = "/extensions.properties";
    /** All extensions properties start with this value */
    private static final String EXTENSIONS_PROPERTY_NAME_PREFIX = "extensions";
    /** This property specifies a service intercept list */
    public static final String SERVICE_INTERCEPT_PROPERTY_NAME = EXTENSIONS_PROPERTY_NAME_PREFIX  + ".intercept.service";
    /** This property specifies a web service intercept list */
    public static final String WEB_SERVICE_INTERCEPT_PROPERTY_NAME = EXTENSIONS_PROPERTY_NAME_PREFIX  + ".intercept.web-service";

    /** Character sequence to start a property specifier */
    public static final String BEGIN_PROPERTY_SPECIFIER = "[";
    /** Character sequence to end a property specifier */
    public static final String END_PROPERTY_SPECIFIER = "]";
    
    //***********************************************************************
    // Intercept constants

    private static final int DEQUALIFY_RESULT_ARRAY_SIZE = 2;
    private static final int DEQUALIFY_NAMESPACE_IDX = 0;
    private static final int DEQUALIFY_LOCAL_NAME_IDX = 1;    
    
    //***********************************************************************
    // Members

    /** Logging */
    private static Log log = LogFactory.getLog(PropertiesPipeFactory.class);

    /** Configuration of service interception */
    private ConfigTree<List<String>> interceptServiceConfigTree;
    /** Configuration of web service interception */
    private ConfigTree<List<String>> interceptWebServiceConfigTree;
	
	private long localLastModified;
	private boolean validConfiguration;
    
    //***********************************************************************
    // Constructor
	
	public PropertiesPipeFactory()
	{
		reset(0);
	}
	
	private void reset(long lastModified)
	{
		this.localLastModified = lastModified;
		this.validConfiguration = false;
		
		interceptServiceConfigTree = new ConfigTree<List<String>>(new ServiceConfigPathParser());
        this.interceptServiceConfigTree.setName("Service interception");
		interceptWebServiceConfigTree = new ConfigTree<List<String>>(new WebServiceConfigPathParser());
        this.interceptWebServiceConfigTree.setName("Web Service interception");
	}
    
    //***********************************************************************
    // Pipe creators

	@SuppressWarnings("unchecked")
	public ServiceInterceptorPipe createServiceInterceptorPipe(Service svc) throws ExtensionException
	{
		if(!loadConfig())
			return null;
		
		List<String> extIds = prepareForServiceIntercept(svc);
		
		if(extIds == null)
			return new ServiceInterceptorPipe(null);
		
		List<Extension> exts = getExtensionList(extIds);
		List<ServiceInterceptor> interceptors = getServiceInterceptorList(exts);
		
		return new ServiceInterceptorPipe(interceptors);
	}
	
	public WebServiceInterceptorPipe createWebServiceInterceptorPipe(SOAPMessageContext smc) throws ExtensionException
	{
		if(!loadConfig())
			return null;
		
		List<String> extIds = prepareForWebServiceIntercept(smc);
		
		if(extIds == null)
			return new WebServiceInterceptorPipe(null);
		
		List<Extension> exts = getExtensionList(extIds);
		List<WebServiceInterceptor> interceptors = getWebServiceInterceptorList(exts);
		
		return new WebServiceInterceptorPipe(interceptors);
	}
    
    //***********************************************************************
    // Aux config

    /**
     *  Load extensions configuration.
     */
	private boolean loadConfig()
	{
		long lastModified = 0;
		try
		{
            log.trace("entering loadConfig");
            log.trace(DEFAULT_EXTENSIONS_RESOURCE_PATH);
			URL url = PropertiesPipeFactory.class.getResource(DEFAULT_EXTENSIONS_RESOURCE_PATH);

			//file not found
			if(url == null)
			{
	            log.trace("properties file not found");
	            log.trace("reseting any previous configuration");
				//reset any loaded configuration
				reset(0);
				return false;
			}
			
			File file = new File(url.toURI());
			lastModified = file.lastModified();
			
			//file hasn't been updated since last load attempt
			if(lastModified <= localLastModified)
			{
	            log.trace("properties file didn't change since last load attempt");
				return validConfiguration;
			}
			
			//file exists and was updated
            log.trace("properties file changed since last update");
            log.trace("reloading configuration");
            reset(lastModified);
			Properties props = new Properties();
			props.load(new FileInputStream(file));

            // log all properties
            if(log.isTraceEnabled())
            {
                log.trace("extensions config properties (key: 'value')");
                
                // typesafe for-each loop
                for (String key : props.stringPropertyNames())
                	log.trace(key + ": '" + props.getProperty(key) + "'");
            }
            
            // load intercept config
            log.trace("load intercept configs");
            loadInterceptConfig(props);
            // log intercept configuration trees
            if(log.isTraceEnabled())
            {
                log.trace("Intercept configuration trees:");
                log.trace(interceptServiceConfigTree.printToString());
                log.trace(interceptWebServiceConfigTree.printToString());
            }
            
            return (validConfiguration = true);
		}
		catch(Exception e)
		{
			log.error(e);
			reset(lastModified);
			return false;
		}
        finally
        {
            log.trace("finally exiting loadConfig");
        }
	}

    // Helper method to load interception configuration
    private void loadInterceptConfig(Properties props) throws ExtensionException
    {
        for (String key : props.stringPropertyNames())
        {
        	if (key.startsWith(SERVICE_INTERCEPT_PROPERTY_NAME))
                addToConfigTree(props, interceptServiceConfigTree, key);
            else if (key.startsWith(WEB_SERVICE_INTERCEPT_PROPERTY_NAME))
                addToConfigTree(props, interceptWebServiceConfigTree, key);
        }
    }

    // Helper method to add an intercept configuration to a configuration tree
    private void addToConfigTree(Properties props, ConfigTree<List<String>> tree, String key) throws ExtensionException
    {
        String value = props.getProperty(key);

        // trim whitespace
        value = value.trim();

        // ignore empty
        if(value.length() == 0)
        {
            log.trace("ignoring empty " + key);
            return;
        }

        List<String> configData = createExtensionIdList(value);
        
        String specifier = parseSpecifier(key);
        // an empty specifier gets or sets the default configuration

        // check if configuration already exists
        // (currently it's not necessary because java.util.Properties.load
        // keeps last found value of a duplicate key)
        if(tree.getConfig(specifier) != null)
            throw new ExtensionException("Config key " + key + " already specified!");
        
        // store configuration
        tree.setConfig(specifier, configData);
    }

    // Helper function to parse the extensions id list property
    private List<String> createExtensionIdList(String propertyValue)
    {
        if(propertyValue == null)
        	throw new IllegalArgumentException("Extension list property value can't be null");
        
        // split using comma
        String[] splitResult = propertyValue.split(",");
        if(splitResult == null)
            return new ArrayList<String>();
        
        // trim results
        for(int i = 0; i < splitResult.length; i++)
            splitResult[i] = splitResult[i].trim();
        
        // return list
        List<String> list = Arrays.asList(splitResult);
        return list;
    }

    // Helper method to parse a intercept specifier
    private String parseSpecifier(String key) throws ExtensionException
    {
        final int idxBegin = key.indexOf(BEGIN_PROPERTY_SPECIFIER);
        if(idxBegin == -1)
            throw new ExtensionException("Begin specifier " + BEGIN_PROPERTY_SPECIFIER +
                                         " not found in property key " + key);
        
        final int idxEnd = key.indexOf(END_PROPERTY_SPECIFIER);
        if(idxEnd == -1)
            throw new ExtensionException("End specifier " + END_PROPERTY_SPECIFIER +
                                         " not found in property key " + key);
        
        if(idxEnd < idxBegin)
            throw new ExtensionException("Begin specifier " + BEGIN_PROPERTY_SPECIFIER +
                                               " must appear before end specifier " + END_PROPERTY_SPECIFIER +
                                               " in property key " + key);
        
        String specifier = key.substring(idxBegin+1, idxEnd);
        return specifier;
    }
    
    //***********************************************************************
    // Aux intercept

    // Helper method to prepare for interception
    // Returns appropriate intercept config data or null if invocation isn't necessary
    @SuppressWarnings("unchecked")
	private List<String> prepareForServiceIntercept(Service serviceInstance)
	{
        // check argument
        if(serviceInstance == null)
        	throw new IllegalArgumentException("Service instance can't be null");

        // obtain service instance class name
        String fullClassName = serviceInstance.getClass().getName();
        if(log.isTraceEnabled())
            log.trace("service instance full class name: " + fullClassName);

        // get config tree
        if(log.isTraceEnabled())
            log.trace("Using configuration tree: " + interceptServiceConfigTree.getName());

        // the config path is the full class name
        String configPath = fullClassName;

        // find most specific config
        List<String> configData = interceptServiceConfigTree.findMostSpecificConfig(configPath);

        // return null if no service interception config found
        if(configData == null)
        {
            log.trace("no service interception configuration found; ignoring service execution");
            return null;
        }

        // return
        return configData;
    }

    // Helper method to prepare for interception
    // Returns appropriate intercept config data or null if invocation isn't necessary
    private List<String> prepareForWebServiceIntercept(SOAPMessageContext smc)
    {
        // check argument
        if(smc == null)
        	throw new IllegalArgumentException("SOAP message context cannot be null");

        // use SOAP message context to extract Web Service config path
        String configPath = buildWebServiceConfigPath(smc);
        if(log.isTraceEnabled())
            log.trace("Extracted config path: '" + configPath + "'");

        // get config tree
        if(log.isTraceEnabled())
            log.trace("Using configuration tree: " + interceptWebServiceConfigTree.getName());

        // find most specific config
        List<String> configData = interceptWebServiceConfigTree.findMostSpecificConfig(configPath);

        // return null if no service interception config found
        if(configData == null)
        {
            log.trace("no web service interception configuration found; ignoring web service execution");
            return null;
        }

        // return
        return configData;
    }

    /**
     *  Web Service configuration path methods
     *
	 *
     * Helper method to build a Web Service interception configuration path
     * from a SOAP message context
     *
     * example SOAP message context contents:
     *   (handler,javax.xml.ws.wsdl.service,{http://calc}CalcService)
     *   (handler,javax.xml.ws.wsdl.port,{http://calc}CalcPort)
     *   (handler,javax.xml.ws.wsdl.operation,{http://calc}sum)
     *
     *  In practice, wsdl.operation is not available in server-side inbound
     *  messages, so it's not used.
     * 
     * @param smc
     * @return	config path
     */
    private String buildWebServiceConfigPath(SOAPMessageContext smc) 
    {
        if(smc == null)
        	throw new IllegalArgumentException("SOAP message context cannot be null");

        // read SOAP message name properties
        QName qualifiedServiceName = (QName) smc.get(MessageContext.WSDL_SERVICE);
        QName qualifiedPortName = (QName) smc.get(MessageContext.WSDL_PORT);

        if(qualifiedServiceName == null)
        	throw new IllegalStateException("Could not find qualified service name in soap message context");
        
        if(qualifiedPortName == null)
        	throw new IllegalStateException("Could not find qualified port name in soap message context");

        String qualifiedService = qualifiedServiceName.toString();
        String qualifiedPort = qualifiedPortName.toString();

        if(log.isTraceEnabled())
        {
            log.trace("SOAP message names follow");
            log.trace(MessageContext.WSDL_SERVICE + ": '" + qualifiedService + "'");
            log.trace(MessageContext.WSDL_PORT + ": '" + qualifiedPort + "'");
        }

        // extract namespace and service name
        String[] dequalifiedService = dequalifyName(qualifiedService);
        String[] dequalifiedPort = dequalifyName(qualifiedPort);

        String namespace = dequalifiedService[DEQUALIFY_NAMESPACE_IDX];
        String service = dequalifiedService[DEQUALIFY_LOCAL_NAME_IDX];
        String port = dequalifiedPort[DEQUALIFY_LOCAL_NAME_IDX];

        // warn if the namespaces in port and operation are different from
        // the namespace in service (the one that is being used)
        if(log.isWarnEnabled())
        {
            String portNamespace = dequalifiedPort[DEQUALIFY_NAMESPACE_IDX];
            if(!(namespace.equals(portNamespace)))
            {
                log.warn("The port namespace is different from the service namespace.");
                log.warn("service ns: '" + namespace + "', port ns: '" + portNamespace + "'");
                log.warn("Extension engine is assuming they are the same for configuration purposes.");
            }
        }

        // create config path
        String configPath = WebServiceConfigPathParser.composeConfigPath(namespace, service, port);
        if(log.isTraceEnabled()) 
        {
            log.trace("Web Service config path source items");
            log.trace("namespace: '" + namespace + "'");
            log.trace("service: '" + service + "'");
            log.trace("port: '" + port + "'");
        }

        return configPath;
    }

    // Helper method to extract namespace and local name from a qualified name
    // String[0] - namespace
    // String[1] - local name
    private String[] dequalifyName(String qualifiedName)
    {
        final String BEGIN_NS = "{";
        final String END_NS = "}";
        final int idxBegin = qualifiedName.indexOf(BEGIN_NS);
        final int idxEnd = qualifiedName.indexOf(END_NS);
        
        if(idxBegin == -1 || idxEnd == -1 || idxBegin > idxEnd)
            throw new IllegalArgumentException("Expecting web service name with namespace " +
                                               " delimited by " + BEGIN_NS + " and " + END_NS);

        String[] result = new String[DEQUALIFY_RESULT_ARRAY_SIZE];
        result[DEQUALIFY_NAMESPACE_IDX] = qualifiedName.substring(idxBegin+1, idxEnd);
        result[DEQUALIFY_LOCAL_NAME_IDX] = qualifiedName.substring(idxEnd+1, qualifiedName.length());
        
        return result;
    }
}
