package step.framework.extensions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.config.ConfigUtil;


/**
 *  Each Extension object represents a configured extension instance.<br />
 *  Each extension can have a listener (optional)
 *  a service interceptor (optional) or a web service interceptor (optional).
 *  <br />
 *  It is responsible for specific extension configuration loading and
 *  supports the execution of the listener and of the interceptors.<br />
 *  <br />
 */
public class Extension {

    //
    //  Members related to configuration file
    //

    /** Minimum length of extension identifier */
    public static final int EXTENSION_ID_MIN_LENGTH = 1;
    /** Maximum length of extension identifier */
    public static final int EXTENSION_ID_MAX_LENGTH = 30;

    /** Extension resource path prefix */
    public static final String EXTENSION_RESOURCE_PATH_PREFIX = "/extension-";
    /** Extension resource path suffix */
    public static final String EXTENSION_RESOURCE_PATH_SUFFIX = ".properties";

    /** All extension properties start with this value */
    private static final String EXTENSION_PROPERTY_NAME_PREFIX = "extension";

    /** This property enables (true) or disables the extension */
    public static final String EXTENSION_ENABLED_PROPERTY_NAME = EXTENSION_PROPERTY_NAME_PREFIX + ".enabled";

    /** This property specifies the full class name of the extension's listener */
    public static final String EXTENSION_LISTENER_PROPERTY_NAME = EXTENSION_PROPERTY_NAME_PREFIX + ".listener";

    /** This property specifies the full class name of the extension's service interceptor */
    public static final String EXTENSION_SERVICE_INTERCEPTOR_PROPERTY_NAME = EXTENSION_PROPERTY_NAME_PREFIX + ".service-interceptor";

    /** This property specifies the full class name of the extension's web service interceptor */
    public static final String EXTENSION_WEB_SERVICE_INTERCEPTOR_PROPERTY_NAME = EXTENSION_PROPERTY_NAME_PREFIX + ".web-service-interceptor";


    //
    //  Members
    //

    /** A reference to the extension engine */
    private ExtensionEngine engine;

    /** extension instance identifier */
    private String id;

    /** extension enabled? */
    private boolean enabled;

    /** extension listener full class name */
    private String listenerClassName;
    /** extension listener class */
    private Class listenerClass;
    /** extension listener class instance */
    private ExtensionListener listenerInstance;

    /** extension service interceptor full class name */
    private String serviceInterceptorClassName;
    /** extension service interceptor class */
    private Class serviceInterceptorClass;

    /** extension web service interceptor full class name */
    private String webServiceInterceptorClassName;
    /** extension web service interceptor class */
    private Class webServiceInterceptorClass;

    /** additional configuration properties */
    private Properties additionalConfig;

    /** extension-instance-scope context */
    private Map<String,Object> context;


    //
    //  Debug and logging members
    //

    /** Logging */
    private Log log = LogFactory.getLog(Extension.class);

    /** Description of service interceptor to use in trace and error messages */
    private final String SERVICE_INTERCEPTOR_DESC = "service interceptor";
    /** Description of web service interceptor to use in trace and error messages */
    private final String WEB_SERVICE_INTERCEPTOR_DESC = "web service interceptor";
    /** Description of listener to use in trace and error messages */
    private final String LISTENER_DESC = "listener";


    //
    // Constructors
    //
    Extension(ExtensionEngine engine, String id) {
        initEngine(engine);
        initId(id);
        initEnabled();
        initContext();
        initListenerMembers();
        initServiceInterceptorMembers();
        initWebServiceInterceptorMembers();
    }

    // Construction helper
    private void initEngine(ExtensionEngine engine) {
        ExtensionsUtil.throwIllegalArgIfNull(engine,
                                             "Extension engine can't be null");
        this.engine = engine;
    }

    // Construction helper
    private void initId(String id) {
        ExtensionsUtil.throwIllegalArgIfNull(id,
                                             "Extension identifier can't be null");

        // trim forward and trailing whitespace from id
        id = id.trim();
        if(id.length() < EXTENSION_ID_MIN_LENGTH) {
            throw new IllegalArgumentException("Extension identifier must be at least " +
                                               EXTENSION_ID_MIN_LENGTH +
                                               " characters long");
        }
        if(id.length() > EXTENSION_ID_MAX_LENGTH) {
            throw new IllegalArgumentException("Extension identifier must be at most " +
                                               EXTENSION_ID_MAX_LENGTH +
                                               " characters long");
        }
        this.id = id;
    }

    // Construction helper
    private void initEnabled() {
        this.enabled = false;
    }

    // Construction helper
    private void initContext() {
        this.context = Collections.synchronizedMap(new HashMap<String,Object>());
    }

    // Construction helper
    private void initListenerMembers() {
        this.listenerClassName = null;
        this.listenerClass = null;
        this.listenerInstance = null;
    }

    // Construction helper
    private void initServiceInterceptorMembers() {
        this.serviceInterceptorClassName = null;
        this.serviceInterceptorClass = null;
    }

    // Construction helper
    private void initWebServiceInterceptorMembers() {
        this.webServiceInterceptorClassName = null;
        this.webServiceInterceptorClass = null;
    }


    //
    //  Listener methods enum
    //
    private enum ListenerMethod {
        EXTENSION_INITIALIZED, EXTENSION_DESTROYED
    }


    //
    // Output
    //

    /**
     *  Returns a brief description of the extension status.
     *  Format is subject to change.
     *
     *  @return String textual description of extension
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.getClass().getSimpleName());
        sb.append(": ");
        sb.append("id=");
        sb.append(this.id);
        sb.append(", ");
        sb.append("enabled=");
        sb.append(this.enabled);
        if(this.listenerClass != null) {
            sb.append(", ");
            sb.append("listener=");
            sb.append(this.listenerClass.getName());
        }
        if(this.serviceInterceptorClass != null) {
            sb.append(", ");
            sb.append("service-interceptor=");
            sb.append(this.serviceInterceptorClass.getName());
        }
        if(this.webServiceInterceptorClass != null) {
            sb.append(", ");
            sb.append("web-service-interceptor=");
            sb.append(this.webServiceInterceptorClass.getName());
        }
        sb.append("]");
        return sb.toString();
    }


    //
    //  Property
    //

    // public-scoped accessors

    /** Check if extension is enabled */
    public boolean isEnabled() {
        return this.enabled;
    }

    /** Obtain extension identifier */
    public String getId() {
        return this.id;
    }

    /** Obtain user-customized configuration of extension */
    public Properties getConfig() {
        return this.additionalConfig;
    }

    /**
     *  Obtain extension context.<br />
     *  This context is unique for each extension instance.
     */
    public Map<String,Object> getContext() {
        return this.context;
    }

    // package-scoped accessors - can only be accessed for classes in same package
    // (allow better unit testing)

    /** Check if extension is configured to intercept services */
    boolean interceptsServices() {
        return (this.serviceInterceptorClassName != null);
    }

    /** Check if extension is configured to intercept web services */
    boolean interceptsWebServices() {
        return (this.webServiceInterceptorClassName != null);
    }

    /** access engine */
    ExtensionEngine getEngine() {
        return this.engine;
    }

    /** access listener class name */
    String getListenerClassName() {
        return this.listenerClassName;
    }

    /** access listener class */
    Class getListenerClass() {
        return this.listenerClass;
    }

    // no accessor is provided for listenerInstance because it is an internal implementation option

    /** access service interceptor class name */
    String getServiceInterceptorClassName() {
        return this.serviceInterceptorClassName;
    }

    /** access service interceptor class */
    Class getServiceInterceptorClass() {
        return this.serviceInterceptorClass;
    }

    /** access web service interceptor class name */
    String getWebServiceInterceptorClassName() {
        return this.webServiceInterceptorClassName;
    }

    /** access service interceptor class */
    Class getWebServiceInterceptorClass() {
        return this.webServiceInterceptorClass;
    }


    //
    //  Configuration loading
    //

    /**
     *  Load an extension's configuration.
     */
    void loadConfig() throws ExtensionEngineException {
        try {
            log.trace("entering loadConfig");

            String resourcePath = EXTENSION_RESOURCE_PATH_PREFIX +
                                  this.id +
                                  EXTENSION_RESOURCE_PATH_SUFFIX;
            if(log.isTraceEnabled()) {
                log.trace("load " + this.id + " extension properties from " + resourcePath);
            }

            Properties props = ConfigUtil.getResourceAsProperties(resourcePath);
            if(props == null) {
                 if(log.isTraceEnabled()) {
                    log.trace(this.id + " extension properties not found");
                }
                throw new ExtensionEngineException("Extension " + this.id +
                                                   " properties not found (" +
                                                   resourcePath + ")");
            }

            // log all properties
            if(log.isTraceEnabled()) {
                log.trace("extension " + this.id + " config properties (key: 'value')");
                for (Enumeration e = props.propertyNames() ; e.hasMoreElements() ;) {
                    String key = (String) e.nextElement();
                    String value = (String) props.get(key);
                    log.trace(key + ": '" + value + "'");
                }
            }

            // enabled
            String enabledPropertyValue = props.getProperty(EXTENSION_ENABLED_PROPERTY_NAME);
            this.enabled = ConfigUtil.recognizeAsTrue(enabledPropertyValue);

            // listener
            String listenerPropertyValue = props.getProperty(EXTENSION_LISTENER_PROPERTY_NAME);
            if(listenerPropertyValue != null && listenerPropertyValue.trim().length() > 0) {
                this.listenerClassName = listenerPropertyValue;
            }

            // service interceptor
            String serviceInterceptorPropertyValue = props.getProperty(EXTENSION_SERVICE_INTERCEPTOR_PROPERTY_NAME);
            if(serviceInterceptorPropertyValue != null && serviceInterceptorPropertyValue.trim().length() > 0) {
                this.serviceInterceptorClassName = serviceInterceptorPropertyValue;
            }

            // web service interceptor
            String webServiceInterceptorPropertyValue = props.getProperty(EXTENSION_WEB_SERVICE_INTERCEPTOR_PROPERTY_NAME);
            if(webServiceInterceptorPropertyValue != null && webServiceInterceptorPropertyValue.trim().length() > 0) {
                this.webServiceInterceptorClassName = webServiceInterceptorPropertyValue;
            }

            // keep additional properties
            if(log.isTraceEnabled()) {
                log.trace("remove extension " + this.id + " properties and keep the remaining ones as additional configuration");
            }
            ConfigUtil.removePropertiesWithPrefix(props, EXTENSION_PROPERTY_NAME_PREFIX);
            this.additionalConfig = props;

        } catch(ExtensionEngineException e) {
            log.trace("disabling extension " + this.id);
            this.enabled = false;
            log.trace("rethrow");
            throw e;
        } finally {
            log.trace("finally exiting loadConfig");
        }
    }


    //
    //  Start Extension
    //

    /**
     *  Initialize an extension.<br />
     *  Test interceptor instances are created to check if their creation
     *  is possible.<br />
     *  If a listener is configured, it is invoked here.<br />
     *  <br />
     */
    void start() throws ExtensionException {
        try {
            log.trace("entering start");

            // loadConfig() is called by engine before init()

            // check if enabled
            if(!this.enabled) {
                log.debug("extension " + this.id +
                    " disabled. Skipping further initialization.");
                return;
            }

            // class names have been validated already by loadConfig()

            // load interceptor classes and create test instances
            initServiceInterceptorClass();
            initWebServiceInterceptorClass();

            // load listener class, create instance and execute its initialization
            initListenerClassAndInstance();
            invokeListener(ListenerMethod.EXTENSION_INITIALIZED);

        } finally {
            if(log.isInfoEnabled()) {
                log.info("Extension " + this.id + " " +
                    (this.enabled ? "enabled" : "disabled"));
            }
            log.trace("finally exiting start");
        }
    }

    // Helper method to initialize service interceptor class
    private void initServiceInterceptorClass() throws ExtensionException {
        String desc = SERVICE_INTERCEPTOR_DESC;
        if(this.serviceInterceptorClassName == null) {
            log.trace("skipping " + desc + " class loading");
        } else {
            this.serviceInterceptorClass = loadClass(this.serviceInterceptorClassName, desc);
            // test if instance creation works
            createServiceInterceptorInstance();
        }
    }

    // Helper method to initialize web service interceptor class
    private void initWebServiceInterceptorClass() throws ExtensionException {
        String desc = WEB_SERVICE_INTERCEPTOR_DESC;
        if(this.webServiceInterceptorClassName == null) {
            log.trace("skipping " + desc + " class loading");
        } else {
            this.webServiceInterceptorClass = loadClass(this.webServiceInterceptorClassName, desc);
            // test if instance creation works
            createWebServiceInterceptorInstance();
        }
    }

    // Helper method to initialize listener class and instance
    private void initListenerClassAndInstance() throws ExtensionException {
        String desc = LISTENER_DESC;
        if(this.listenerClassName == null) {
            log.trace("skipping " + desc + " class loading");
        } else {
            this.listenerClass = loadClass(this.listenerClassName, desc);
            // create listener instance
            this.listenerInstance = createListenerInstance();
        }
    }


    //
    //  Generic class loading and instance creation
    //

    // Helper method to load class
    private Class loadClass(String className, String desc) throws ExtensionException {
        ExtensionsUtil.throwIllegalArgIfNull(className,
                                             "class name used to load class can't be null");
        // if necessary provide a default description
        if(desc == null || desc.trim().length() == 0) {
            desc = className;
        }
        try {
            log.trace("load " + desc + " class");
            return Class.forName(className);
        } catch(Throwable t) {
            log.trace("failed to load " + desc + " class");
            throw new ExtensionException("Could not load " + this.id +
                                         " extension's " + desc + " class",
                                         t);
        }
    }

    // Helper method to create class instance
    private Object createInstance(Class classObject, String desc) throws ExtensionException {
        ExtensionsUtil.throwIllegalArgIfNull(classObject,
                                             "class used to create instance can't be null");
        // if necessary provide a default description
        if(desc == null || desc.trim().length() == 0) {
            desc = classObject.getName();
        }
        try {
            log.trace("create " + desc + " instance");
            return classObject.newInstance();
        } catch(Throwable t) {
            log.trace("failed to create " + desc + " instance");
            throw new ExtensionException("Could not create " + this.id +
                                         " extension's " + desc + " instance",
                                         t);
        }
    }


    //
    //  Listener and Interceptors instantiation
    //

    /**
     *  Create a listener class instance.
     */
    private ExtensionListener createListenerInstance() throws ExtensionException {
        final String desc = LISTENER_DESC;
        Object listenerObject = createInstance(this.listenerClass, desc);
        try {
            ExtensionListener el = (ExtensionListener) listenerObject;
            return el;

        } catch(ClassCastException e) {
            log.trace("failed to cast " + desc + " object");
            throw new ExtensionException("Failed to cast " + this.id +
                                         " extension's " + desc + " to proper type." +
                                         " Class must implement " +
                                         ExtensionListener.class.getName(), e);
        }
    }

    /**
     *  Create a service interceptor class instance.
     */
    ServiceInterceptor createServiceInterceptorInstance() throws ExtensionException {
        final String desc = SERVICE_INTERCEPTOR_DESC;
        Object serviceInterceptorObject = createInstance(this.serviceInterceptorClass,
                                                         desc);
        try {
            ServiceInterceptor si = (ServiceInterceptor) serviceInterceptorObject;
            return si;

        } catch(ClassCastException e) {
            log.trace("failed to cast " + desc + " object");
            throw new ExtensionException("Failed to cast " + this.id +
                                         " extension's " + desc + " to proper type." +
                                         " Class must implement " +
                                         ServiceInterceptor.class.getName(), e);
        }
    }

    /**
     *  Create a web service interceptor class instance.
     */
    WebServiceInterceptor createWebServiceInterceptorInstance() throws ExtensionException {
        final String desc = WEB_SERVICE_INTERCEPTOR_DESC;
        Object webServiceInterceptorObject = createInstance(this.webServiceInterceptorClass,
                                                            desc);
        try {
            WebServiceInterceptor wsi = (WebServiceInterceptor) webServiceInterceptorObject;
            return wsi;

        } catch(ClassCastException e) {
            log.trace("failed to cast " + desc + " object");
            throw new ExtensionException("Failed to cast " + this.id +
                                         " extension's " + desc + " to proper type." +
                                         " Class must implement " +
                                         ServiceInterceptor.class.getName(), e);
        }
    }


    //
    //  Invoke Listener
    //

    // Helper method to invoke listener methods and handle errors
    private void invokeListener(ListenerMethod listenerMethod) throws ExtensionException {
        if(this.listenerInstance != null) {
            log.trace("invoke listener on " + listenerMethod);
            // create parameter
            ExtensionListenerParameter param =
                new ExtensionListenerParameter(this.engine, this);
            // invoke method
            switch(listenerMethod) {
                case EXTENSION_INITIALIZED:
                    this.listenerInstance.extensionInitialized(param);
                    break;
                case EXTENSION_DESTROYED:
                    this.listenerInstance.extensionDestroyed(param);
                    break;
            }
        }
    }


    //
    //  Stop Extension
    //

    /**
     *  Stop an extension.<br />
     *  This method is called at the end of an extension's life.<br />
     *  If a listener is configured, it is invoked here.<br />
     *  <br />
     */
    void stop() throws ExtensionException {
        try {
            log.trace("entering stop");

            // check if enabled
            if(!this.enabled) {
                log.debug("extension " + this.id +
                    " disabled. Skipping destruction.");
                return;
            }

            // call listener
            invokeListener(ListenerMethod.EXTENSION_DESTROYED);

        } finally {
            // disable and reset members (all internal references to null)
            this.enabled = false;
            this.engine = null;
            initListenerMembers();
            initServiceInterceptorMembers();
            initWebServiceInterceptorMembers();
            this.additionalConfig = null;
            this.context = null;

            log.trace("finally exiting stop");
        }
    }

}
