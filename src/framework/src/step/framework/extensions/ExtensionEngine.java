package step.framework.extensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.config.ConfigUtil;
import step.framework.config.tree.ConfigTree;


/**
 *  The Extension Engine is a singleton object
 *  that represents the framework's extension engine.<br />
 *  <br />
 *  It is responsible for configuration loading and
 *  supports their execution.
 */
public class ExtensionEngine {

    //
    //  Singleton
    //

    /** Single instance created upon class loading. */
    private static final ExtensionEngine fINSTANCE = new ExtensionEngine();

    /** Return singleton instance. */
    public static synchronized ExtensionEngine getInstance() {
        synchronized(fINSTANCE) {
            // synchronized on instance also because of auto-init
            return fINSTANCE;
        }
    }

    /** Private constructor prevents construction outside this class. */
    private ExtensionEngine() {
        initAll();
    }


    //
    //  Members related to configuration file
    //

    /** Default extensions properties file resource path */
    public static final String DEFAULT_EXTENSIONS_RESOURCE_PATH = "/extensions.properties";

    /** All extensions properties start with this value */
    private static final String EXTENSIONS_PROPERTY_NAME_PREFIX = "extensions";

    /** This property enables (true) or disables extensions */
    public static final String EXTENSIONS_ENABLED_PROPERTY_NAME = EXTENSIONS_PROPERTY_NAME_PREFIX + ".enabled";

    /** This property specifies the known extension ids */
    public static final String EXTENSIONS_LIST_PROPERTY_NAME = EXTENSIONS_PROPERTY_NAME_PREFIX + ".list";;

    /** This property specifies a service intercept list */
    public static final String SERVICE_INTERCEPT_PROPERTY_NAME = EXTENSIONS_PROPERTY_NAME_PREFIX  + ".intercept.service";

    /** This property specifies a web service intercept list */
    public static final String WEB_SERVICE_INTERCEPT_PROPERTY_NAME = EXTENSIONS_PROPERTY_NAME_PREFIX  + ".intercept.web-service";

    /** Character sequence to start a property specifier */
    public static final String BEGIN_PROPERTY_SPECIFIER = "[";
    /** Character sequence to end a property specifier */
    public static final String END_PROPERTY_SPECIFIER = "]";


    //
    //  Members
    //

    /** Is the extension engine enabled? */
    private boolean enabledFlag;

    /**
     *  Has the extension engine initializer been invoked?
     *  The flag is set to true in init() regardless of initialization sucess.
     *  The flag is set to false in destroy() regardless of destruction sucess.
     *  It is used for one-time automatic initialization (autoInit).
     */
    private boolean initFlag;

    /** List of all known extensions */
    private List<Extension> extensionList;

    /** Configuration of service interception */
    private ConfigTree<InterceptConfigData> interceptServiceConfigTree;
    /** Configuration of web service interception */
    private ConfigTree<InterceptConfigData> interceptWebServiceConfigTree;

    /** Additional configuration properties */
    private Properties additionalConfig;

    /** extension-engine-scope context */
    private Map<String,Object> context;


    //
    //  Debug and logging members
    //

    /** Logging */
    private static Log log = LogFactory.getLog(ExtensionEngine.class);


    //
    //  Constructions helpers
    //

    // Construction helper - used also on destroy to reset engine
    // initFlag is exempt from this initialization
    private void initAll() {
        initEnabled();
        initExtensionList();
        initConfigTrees();
        initAdditionalConfig();
        initContext();
    }

    // Construction helper
    private void initEnabled() {
        this.enabledFlag = false;
    }

    // Construction helper
    private void initExtensionList() {
        this.extensionList = new LinkedList<Extension>();
    }

    // Construction helper
    private void initConfigTrees() {
        this.interceptServiceConfigTree = new ConfigTree<InterceptConfigData>(new ServiceConfigPathParser());
        this.interceptServiceConfigTree.setName("Service interception");

        this.interceptWebServiceConfigTree = new ConfigTree<InterceptConfigData>(new WebServiceConfigPathParser());;
        this.interceptWebServiceConfigTree.setName("Web Service interception");
    }

    // Construction helper
    private void initAdditionalConfig() {
        this.additionalConfig = null;
    }

    // Construction helper
    private void initContext() {
        this.context = Collections.synchronizedMap(new HashMap<String,Object>());
    }


    //
    //  Output
    //

    /**
     *  Returns a brief description of the extension engine status.<br />
     *  Format is subject to change.
     *
     *  @return String textual description of extension engine
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.getClass().getSimpleName());
        sb.append(": ");
        sb.append("extensionList={");
        {
            int i = this.extensionList.size();
            for(Extension extension : this.extensionList) {
                sb.append(extension);
                if(--i != 0) {
                    sb.append(", ");
                }
            }
        }
        sb.append("}");
        sb.append(" ]");
        return sb.toString();
    }


    //
    //  Property methods
    //

    // public-scoped accessors

    /** Check if extension engine is enabled. Can trigger automatic initialization. */
    public boolean isEnabled() {
        autoInit();
        return this.enabledFlag;
    }

    /** Obtain user-customized configuration of extension engine. Can trigger automatic initialization. */
    public Properties getConfig() {
        autoInit();
        return this.additionalConfig;
    }

    /**
     *  Obtain extension-engine context.<br />
     *  This context is unique for the whole extension engine.
     *  It can be used by different extensions to share data.
     */
    public Map<String,Object> getContext() {
        return this.context;
    }

    // package-scoped accessors - can only be accessed for classes in same package
    // (allow better unit testing)

    // initFlag is an internal implementation option

    /** Access the extension list */
    List<Extension> getExtensionList() {
        return this.extensionList;
    }

    /** Access the service interception configuration tree. Can trigger automatic initialization. */
    ConfigTree<InterceptConfigData> getInterceptServiceConfigTree() {
        autoInit();
        return this.interceptServiceConfigTree;
    }

    /** Access the service interception configuration tree. Can trigger automatic initialization. */
    ConfigTree<InterceptConfigData> getInterceptWebServiceConfigTree() {
        autoInit();
        return this.interceptWebServiceConfigTree;
    }


    //
    //  ExtensionEngine lifecycle: init
    //

    /**
     *  Initialize the extensions engine assuming the default extensions
     *  configuration resource path.<br />
     *  <br />
     *  @throws ExtensionEngineException when initialization fails
     */
    public synchronized void init() throws ExtensionEngineException {
        init(DEFAULT_EXTENSIONS_RESOURCE_PATH);
    }

    /**
     *  Initialize the extensions engine using the provided extensions
     *  configuration resource path.<br />
     *  <br />
     *  Extensions initialization has two main steps:<br />
     *  1st - load main configuration file and extension's
     *        configuration files<br />
     *  2nd - start the engine i.e. interpret configuration files and start
     *        all extensions<br />
     *  <br />
     *  If no main configuration file is found
     *  the engine disables itself and doesn't throw an exception.<br />
     *  If a configuration file exists but contains errors
     *  the engine throws an exception with the cause.<br />
     *  <br />
     *  @param extensionsPropertiesResourcePath extensions property file resource path
     *  @throws ExtensionEngineException when initialization failed
     */
    public synchronized void init(String extensionsPropertiesResourcePath) throws ExtensionEngineException {

        try {
            log.trace("entering init");

            // begin load config
            try {
                if(log.isDebugEnabled()) {
                    log.debug("load extension engine configuration from " + extensionsPropertiesResourcePath);
                }
                loadConfig(extensionsPropertiesResourcePath);
                log.debug("extension engine configuration loaded successfully");

            } catch(Throwable t) {
                log.debug("caught throwable when trying to load extensions configuration");
                log.debug(t);
                log.trace("throwable details", t);
                log.debug("disabling extension engine");
                this.enabledFlag = false;
            }
            // end load config

            // proceed only if extension engine is enabled
            if(!this.enabledFlag) {
                log.debug("extension engine disabled. Skipping further initialization.");
                return;
            }

            // begin start extensions
            try {
                log.debug("start extensions");

                for(Extension extension : extensionList) {
                    extension.start();
                }

                log.debug("extensions started successfully");

            } catch(Throwable t) {
                log.debug("caught throwable when trying to start extensions");
                log.debug(t);
                log.trace("throwable details", t);
                log.debug("disabling extension engine");
                this.enabledFlag = false;
                log.trace("throw exception with nested cause");
                throw new ExtensionEngineException("Failed to initialize extensions", t);
            }
            // end start extensions


        } finally {
            if(log.isInfoEnabled()) {
                log.info("Extension engine " + (this.enabledFlag ? "enabled" : "disabled"));
            }

            log.trace("set init flag");
            this.initFlag = true;

            log.trace("finally exiting init");
        }
    }

    // Helper method - used to do automatic initialization, on demand
    // should be called by all getters that assume init has been performed
    private synchronized void autoInit() {
        // try to initialize once, on-demand, using default init
        if(!initFlag) {
            try {
                log.trace("Attempting automatic extension engine initialization");
                init();
                log.trace("Automatic extension engine initialization successful!");
            } catch(ExtensionEngineException eee) {
                log.debug("Automatic extension engine initialization failed!");
                log.debug(eee);
                log.trace("extension engine exception details", eee);
            }
        }
    }


    //
    //  Load configuration
    //

    /**
     *  Load extensions configuration.
     */
    private void loadConfig(String resourcePath) throws ExtensionEngineException {
        try {
            log.trace("entering loadConfig");

            log.trace("load extensions properties");
            log.trace(resourcePath);
            Properties props = ConfigUtil.getResourceAsProperties(resourcePath);
            if(props == null) {
                log.trace("extensions properties not found");
                throw new ExtensionEngineException("Extensions properties not found (" +
                    resourcePath + ")");
            }

            // log all properties
            if(log.isTraceEnabled()) {
                log.trace("extensions config properties (key: 'value')");
                
                // typesafe for-each loop
                for (String key :props.stringPropertyNames()) {
                	String value = (String) props.get(key);
                	log.trace(key + ": '" + value + "'");
                }
            
            }

            // enabled
            String enabledPropertyValue = props.getProperty(EXTENSIONS_ENABLED_PROPERTY_NAME);
            if(enabledPropertyValue != null && enabledPropertyValue.trim().equalsIgnoreCase("true")) {
                this.enabledFlag = true;
            }

            // check if enabled
            if(!this.enabledFlag) {
                log.trace("extensions disabled. Skipping remaining configuration.");
                return;
            }

            // extensions list
            String extensionsListPropertyValue = props.getProperty(EXTENSIONS_LIST_PROPERTY_NAME);
            List<String> idList = createExtensionIdList(extensionsListPropertyValue);
            // check if list is null
            if(idList == null || idList.size() == 0) {
                if(log.isWarnEnabled()) {
                    log.warn("No extensions declared in " + EXTENSIONS_LIST_PROPERTY_NAME +
                        " property");
                }
            }
            // check if list has duplicates
            Set<String> idSet = new HashSet<String>(idList);
            if(idSet.size() < idList.size()) {
                throw new ExtensionEngineException("Duplicate extension declared in " +
                    EXTENSIONS_LIST_PROPERTY_NAME + " property");
            }

            // load extensions config
            log.debug("load configuration for each extension");
            for(String id : idList) {
                Extension extension = new Extension(this, id);
                extension.loadConfig();
                this.extensionList.add(extension);
            }

            // load intercept config
            log.trace("load intercept configs");
            loadInterceptConfig(props);
            // log intercept configuration trees
            if(log.isTraceEnabled()) {
                log.trace("Intercept configuration trees:");
                log.trace(interceptServiceConfigTree.printToString());
                log.trace(interceptWebServiceConfigTree.printToString());
            }

            // keep additional properties
            log.trace("remove extensions properties and keep the remaining ones as additional configuration");
            ConfigUtil.removePropertiesWithPrefix(props, EXTENSIONS_PROPERTY_NAME_PREFIX);
            this.additionalConfig = props;

        } finally {
            log.trace("finally exiting loadConfig");
        }
    }

    // Helper function to parse the extensions id list property
    private List<String> createExtensionIdList(String propertyValue) {
        ExtensionsUtil.throwIllegalArgIfNull(propertyValue,
                                             "extension list property value can't be null");
        // split using comma
        String[] splitResult = propertyValue.split(",");
        if(splitResult == null) {
            // empty list
            return new ArrayList<String>();
        }
        // trim results
        for(int i = 0; i < splitResult.length; i++) {
            splitResult[i] = splitResult[i].trim();
        }
        // return list
        List<String> list = Arrays.asList(splitResult);
        return list;
    }

    // Helper method to convert an extension id list to an extension list
    // Throws an exception if an unknown id is found or
    // if a condition argument is not met
    private List<Extension> createInterceptExtensionList(List<String> idList,
                                                         boolean mustInterceptServices,
                                                         boolean mustInterceptWebServices)
    throws ExtensionEngineException {
        List<Extension> resultList = new ArrayList<Extension>();
        for(String id : idList) {
            boolean idFound = false;
            for(Extension extension : this.extensionList) {
                if(id.equals(extension.getId())) {

                    if(mustInterceptServices && !extension.interceptsServices()) {
                        throw new ExtensionEngineException("Extension " + id +
                                                           " must specify a service interceptor");
                    }
                    if(mustInterceptWebServices && !extension.interceptsWebServices()) {
                        throw new ExtensionEngineException("Extension " + id +
                                                           " must specify a web service interceptor");
                    }

                    resultList.add(extension);
                    idFound = true;
                }
            }
            if(!idFound) {
                throw new ExtensionEngineException("Unknown extension id " + id);
            }
        }
        return resultList;
    }


    // Helper method to load interception configuration
    private void loadInterceptConfig(Properties props) throws ExtensionEngineException {

// JORGE: Replaced with typesafe for-each loop
    	
//        for (Enumeration e = props.propertyNames() ; e.hasMoreElements() ;) {
//            String key = (String) e.nextElement();
//            if (key.startsWith(SERVICE_INTERCEPT_PROPERTY_NAME)) {
//                addToConfigTree(props, interceptServiceConfigTree, key, true, false);
//            } else if (key.startsWith(WEB_SERVICE_INTERCEPT_PROPERTY_NAME)) {
//                addToConfigTree(props, interceptWebServiceConfigTree, key, false, true);
//            }
//        }
        for (String key : props.stringPropertyNames()) {
        	if (key.startsWith(SERVICE_INTERCEPT_PROPERTY_NAME)) {
                addToConfigTree(props, interceptServiceConfigTree, key, true, false);
            } else if (key.startsWith(WEB_SERVICE_INTERCEPT_PROPERTY_NAME)) {
                addToConfigTree(props, interceptWebServiceConfigTree, key, false, true);
            }
        }
    }

    // Helper method to add an intercept configuration to a configuration tree
    private void addToConfigTree(Properties props,
                                 ConfigTree<InterceptConfigData> tree,
                                 String key,
                                 boolean mustInterceptServices,
                                 boolean mustInterceptWebServices)
    throws ExtensionEngineException {

        String value = (String) props.get(key);

        // trim whitespace
        value = value.trim();

        // ignore empty
        if(value.length() == 0) {
            log.trace("ignoring empty " + key);
            return;
        }

        List<String> idList = createExtensionIdList(value);
        List<Extension> extList = createInterceptExtensionList(idList,
                                                               mustInterceptServices,
                                                               mustInterceptWebServices);

        String specifier = parseSpecifier(key);
        // an empty specifier gets or sets the default configuration

        // check if configuration already exists
        // (currently it's not necessary because java.util.Properties.load
        // keeps last found value of a duplicate key)
        InterceptConfigData previousData = tree.getConfig(specifier);
        if(previousData != null) {
            throw new ExtensionEngineException("Config key " + key +
                " already specified!");
        }
        // store configuration
        InterceptConfigData configData = new InterceptConfigData(extList);
        tree.setConfig(specifier, configData);
    }

    // Helper method to parse a intercept specifier
    private String parseSpecifier(String key) throws ExtensionEngineException {
        final int idxBegin = key.indexOf(BEGIN_PROPERTY_SPECIFIER);
        if(idxBegin == -1) {
            throw new ExtensionEngineException("begin specifier " +
                                               BEGIN_PROPERTY_SPECIFIER +
                                               " not found in property key " +
                                               key);
        }
        final int idxEnd = key.indexOf(END_PROPERTY_SPECIFIER);
        if(idxEnd == -1) {
            throw new ExtensionEngineException("end specifier " +
                                               END_PROPERTY_SPECIFIER +
                                               " not found in property key " +
                                               key);
        }
        if(idxEnd < idxBegin) {
            throw new ExtensionEngineException("begin specifier " +
                                               BEGIN_PROPERTY_SPECIFIER +
                                               " must appear before end specifier " +
                                               END_PROPERTY_SPECIFIER +
                                               " in property key " +
                                               key);
        }
        String specifier = key.substring(idxBegin+1, idxEnd);
        return specifier;
    }


    //
    //  ExtensionEngine lifecycle: destroy
    //

    /**
     *  Destroy the extensions engine.<br />
     *  <br />
     *  @throws ExtensionEngineException when destruction failed
     */
    public synchronized void destroy() throws ExtensionEngineException {
        try {
            log.trace("entering destroy");

            // check if enabled
            if(!this.enabledFlag) {
                log.trace("extensions disabled. Skipping destruction.");
                return;
            }

            // stop extensions (in reverse order of initialization)
            ListIterator<Extension> listIterator = extensionList.listIterator(extensionList.size());
            while(listIterator.hasPrevious()) {
                Extension extension = listIterator.previous();
                try {
                    extension.stop();
                } catch(ExtensionException e) {
                    log.debug("Failed to stop extension " + extension.getId());
                    log.debug(e);
                    log.trace("exception details", e);
                    log.debug("Proceeding to previous extension");
                }
            }

            log.debug("Extension engine destroyed successfully");

        } catch(Throwable t) {
            log.debug("caught throwable when trying to destroy extension engine");
            log.debug(t);
            log.trace("throwable details", t);
            log.debug("throw exception with nested cause");
            throw new ExtensionEngineException("Failed to destroy extensions", t);
        } finally {
            // re-init engine
            initAll();

            log.trace("unset init flag");
            this.initFlag = false;

            log.trace("finally exiting destroy");
        }
    }

    // Helper method - best-effort attempt to run destroy when
    // extension engine object is about to be garbage collected
    protected void finalize() throws Throwable {
        try {
            if(initFlag) {
                destroy();
            }
        } finally {
            super.finalize();
        }
    }

    //**********************************************************************************
    //Automatic configuration liaison methods
    
    public Extension getExtension(String id)
    {
    	for(int i=0; i<extensionList.size(); i++)
    	{
    		Extension extension = extensionList.get(i);
    		if(extension.getId().equals(id))
    			return extension;
    	}
    	
    	return null;
    }
}
