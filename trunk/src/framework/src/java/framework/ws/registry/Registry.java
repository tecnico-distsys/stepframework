package step.framework.ws.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *  Simplified JAX-R web service registry access class.
 */
public class Registry {

    /* Registry connection */
    private String _url;
    private static final String URL_PROPERTY = "url";

    private String _username;
    public static final String USERNAME_PROPERTY = "username";
    private String _password;
    public static final String PASSWORD_PROPERTY = "password";

    private String _HTTPProxyHost  = "";
    public static final String HTTP_PROXY_HOST_PROPERTY = "HTTPProxyHost";
    private String _HTTPProxyPort  = "";
    public static final String HTTP_PROXY_PORT_PROPERTY = "HTTPProxyPort";

    private String _HTTPSProxyHost = "";
    public static final String HTTPS_PROXY_HOST_PROPERTY = "HTTPSProxyHost";
    private String _HTTPSProxyPort = "";
    public static final String HTTPS_PROXY_PORT_PROPERTY = "HTTPSProxyPort";

    /* JAX-R objects references */
    private Connection _connection;
    private RegistryService _rs;
    private BusinessLifeCycleManager _blcm;
    private BusinessQueryManager _bqm;

    /* Locale */
    private Locale _locale = Locale.getDefault();
    public static final String LOCALE_PROPERTY = "locale";

    /* options */
    private boolean _optionOutputMessages = false;
    public static final String OPTION_OUTPUT_MESSAGES_PROPERTY = "optionOutputMessages";

    private boolean _optionValidateURI = false;
    public static final String OPTION_VALIDATE_URI_PROPERTY = "optionValidateURI";

    private boolean _optionWarnAboutLocale = true;
    public static final String OPTION_WARN_ABOUT_LOCALE_PROPERTY = "optionWarnAboutLocale";

    private boolean _optionWarnAboutLocalHostRegistration = true;
    public static final String OPTION_WARN_ABOUT_LOCAL_HOST_REGISTRATION_PROPERTY = "optionWarnAboutLocalHostRegistration";

    /* message prefix and suffix */
    private String _messagePrefix = "";
    private String _messageSuffix = "";

    /* logging */
    Log log = null;

    /* Conditionally prints messages with current prefix and suffix */
    private void printMsg(String message) {

        // create logger
        if(log == null)
            log = LogFactory.getLog(Registry.class);

        // complete message with prefix and suffix
        String completeMessage = _messagePrefix + message + _messageSuffix;

        // log message at info level
        log.info(completeMessage);

        // if option enabled, also print message to System.out
        if(_optionOutputMessages)
            System.out.println(_messagePrefix + message + _messageSuffix);
    }


    //
    // Constructors
    //

    /** Create registry using provided arguments. */
    public Registry(String URL, String username, String password) {
        setURL(URL);
        setUsername(username);
        setPassword(password);
    }

    /** Create registry using provided arguments. */
    public Registry(String URL, String username, String password,
                    String proxyHost, String proxyPort, boolean HTTPSProxy) {
        this(URL, username, password);
        if(HTTPSProxy) {
            _HTTPSProxyHost = proxyHost;
            _HTTPSProxyPort = proxyPort;
        } else {
            _HTTPProxyHost = proxyHost;
            _HTTPProxyPort = proxyPort;
        }
    }

    /** Uses a property file to load the registry configuration. */
    public Registry(File propertyFile) throws RegistryException {
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            loadFromInputStream(fis);
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Uses a property input stream to load the registry configuration. */
    public Registry(InputStream is) throws RegistryException {
        loadFromInputStream(is);
    }

    /** Uses a resource path to create an input stream and load registry configuration. */
    public Registry(String resourcePath) throws RegistryException {
        InputStream is = Registry.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IllegalArgumentException("Resource path " + resourcePath + " not found");
        }
        loadFromInputStream(is);
    }

    /** Uses a property set to load the registry configuration. */
    public Registry(Properties properties) {
        loadFromProperties(properties);
    }


    //
    // Private auxiliary methods
    //

    /** Use an input stream to load the registry configuration. */
    private void loadFromInputStream(InputStream is) throws RegistryException {
        try {
            try {
                Properties properties = new Properties();
                properties.load(is);
                loadFromProperties(properties);
            } finally {
                if(is != null)
                    is.close();
            }
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Use a property set to load the registry configuration. */
    private void loadFromProperties(Properties properties) {

        String propertyName, property;

        /* required properties */

        propertyName = URL_PROPERTY;
        property = properties.getProperty(propertyName);
        if (property == null) {
            // try alias
            property = properties.getProperty(URL_PROPERTY);
            if (property == null) {
                throw new IllegalArgumentException(propertyName + " property is missing from file!");
            }
        }
        setURL(property);

        propertyName = USERNAME_PROPERTY;
        property = properties.getProperty(propertyName);
        if(property == null)
            throw new IllegalArgumentException(propertyName + " property is missing from file!");
        setUsername(property);

        propertyName = PASSWORD_PROPERTY;
        property = properties.getProperty(propertyName);
        if(property == null)
            throw new IllegalArgumentException(propertyName + " property is missing from file!");
        setPassword(property);


        /* optional properties */

        String host = properties.getProperty(HTTP_PROXY_HOST_PROPERTY);
        String port = properties.getProperty(HTTP_PROXY_PORT_PROPERTY);
        if(host!= null && port != null)
            setHTTPProxy(host, port);

        String secureHost = properties.getProperty(HTTPS_PROXY_HOST_PROPERTY);
        String securePort = properties.getProperty(HTTPS_PROXY_PORT_PROPERTY);
        if(secureHost!= null && securePort != null)
            setHTTPSProxy(secureHost, securePort);

        String locale = properties.getProperty(LOCALE_PROPERTY);
        if(locale != null)
            setLocale(new Locale(locale));

        String optionOutputMessages = properties.getProperty(OPTION_OUTPUT_MESSAGES_PROPERTY);
        if(optionOutputMessages != null)
            setOptionOutputMessages(new Boolean(optionOutputMessages).booleanValue());

        String optionValidateURI = properties.getProperty(OPTION_VALIDATE_URI_PROPERTY);
        if(optionValidateURI!= null)
            setOptionValidateURI(new Boolean(optionValidateURI).booleanValue());

        String optionWarnAboutLocale = properties.getProperty(OPTION_WARN_ABOUT_LOCALE_PROPERTY);
        if(optionWarnAboutLocale != null)
            setOptionWarnAboutLocale(new Boolean(optionWarnAboutLocale).booleanValue());

    }


    //
    // Operation methods
    //

    /** Establish connection with registry server */
    public void connect(boolean authenticate) throws RegistryException {
        try {
            _messagePrefix = "connect(): ";

            printMsg("Establishing connection to registry server " + _url);

            Properties props = new Properties();
            props.setProperty("javax.xml.registry.queryManagerURL", _url);
            props.setProperty("javax.xml.registry.lifeCycleManagerURL", _url);
            props.setProperty("com.sun.xml.registry.http.proxyHost", _HTTPProxyHost);
            props.setProperty("com.sun.xml.registry.http.proxyPort", _HTTPProxyPort);
            props.setProperty("com.sun.xml.registry.https.proxyHost", _HTTPSProxyHost);
            props.setProperty("com.sun.xml.registry.https.proxyPort", _HTTPSProxyPort);

            ConnectionFactory factory = ConnectionFactory.newInstance();
            factory.setProperties(props);
            _connection = factory.createConnection();
            printMsg("Connected to registry server");

            _rs = _connection.getRegistryService();
            _blcm = _rs.getBusinessLifeCycleManager();
            _bqm = _rs.getBusinessQueryManager();
            printMsg("JAX-R objects initialized");

            if(authenticate) {
                printMsg("Performing authentication for user " + _username);
                PasswordAuthentication passwdAuth = new PasswordAuthentication(_username, _password.toCharArray());
                Set creds = new HashSet();
                creds.add(passwdAuth);
                _connection.setCredentials(creds);
                printMsg("Authentication credentials set");
            }
        } catch(JAXRException e) {
            throw new RegistryException(e);
        }
    }


    /** Terminate connection with registry server */
    public void disconnect() throws RegistryException {
        try {
            _messagePrefix = "disconnect(): ";
            if (_connection == null) {
                printMsg("Connection is null");
            } else {
                _connection.close();
                printMsg("Closed connection to registry server");
            }
        } catch(JAXRException e) {
            throw new RegistryException(e);
        }
    }


    /** Publish a web service in the registry server
        Returns true if the service registration was performed successfully, false otherwise.
        The registration key generated by the registry is stored in the parameter registration. */
    public boolean publish(Registration registration) throws RegistryException {
        try {
            _messagePrefix = "publish(): ";

            printMsg("Publishing the web service");

            InternationalString is = _blcm.createInternationalString(_locale, registration.getOrganizationName());
            Organization org = _blcm.createOrganization(is);
            is = _blcm.createInternationalString(_locale, "");
            org.setDescription(is);
            Collection orgs = new ArrayList();
            orgs.add(org);
            printMsg("Created organization object with name " + registration.getOrganizationName());

            if(!registration.hasClassification()) {
                printMsg("Registration doesn't have a classification");
            } else {
                ClassificationScheme classificationScheme = _bqm.findClassificationSchemeByName(null, registration.getClassificationScheme());
                if(classificationScheme != null) {
                    printMsg("Found classification scheme " + registration.getClassificationScheme());
                } else {
                    printMsg("Classification scheme " + registration.getClassificationScheme() + " not found");
                    return false;
                }

                Classification classification =
                    _blcm.createClassification(classificationScheme, registration.getClassificationName(),  registration.getClassificationValue());
                printMsg("Created classification " + registration.getClassificationName() + " " + registration.getClassificationValue());

                Collection classifications = new ArrayList();
                classifications.add(classification);
                org.addClassifications(classifications);
                printMsg("Added classification to organization");
            }

            is = _blcm.createInternationalString(_locale, registration.getServiceName());
            Service service = _blcm.createService(is);
            is = _blcm.createInternationalString(_locale, "");
            service.setDescription(is);
            printMsg("Created service with name " + registration.getServiceName());

            ServiceBinding binding = _blcm.createServiceBinding();
            is = _blcm.createInternationalString(_locale, "");
            binding.setDescription(is);
            binding.setValidateURI(_optionValidateURI);
            String uri = registration.getServiceBindingAccessURI();
            binding.setAccessURI(uri);
            printMsg("Created service binding with URI: " + uri);

            // local host warning check
            if(_optionWarnAboutLocalHostRegistration && isLocalHostRegistration(uri)) {
                printMsg("WARNING: registering a localhost address." +
                         " You probably want to use the machine name or IP instead.");
            }

            Collection serviceBindings = new ArrayList();
            serviceBindings.add(binding);
            service.addServiceBindings(serviceBindings);
            printMsg("Added service binding to service");

            Collection services = new ArrayList();
            services.add(service);
            org.addServices(services);
            printMsg("Added service to organization");

            printMsg("Saving organization");
            BulkResponse response = _blcm.saveOrganizations(orgs);
            Collection exceptions = response.getExceptions();
            if(exceptions == null) {
                printMsg("Organization registered");
                Collection keys = response.getCollection();
                Iterator keyIterator = keys.iterator();
                if(keyIterator.hasNext()) {
                    Key organizationKey = (Key) keyIterator.next();
                    String organizationID = organizationKey.getId();
                    printMsg("Organization ID is: " + organizationID);

                    RegistrationKey regKey = new RegistrationKey();
                    regKey.setKey(organizationID);

                    registration.setRegistrationKey(regKey);
                    return true;
                } else {
                    printMsg("Organization ID not found");
                    return false;
                }
            } else {
                printMsg("Organization not registered");
                Iterator exceptionIterator = exceptions.iterator();
                Exception exception = null;
                while (exceptionIterator.hasNext()) {
                    exception = (Exception) exceptionIterator.next();
                    System.err.println("Registration exception: " + exception.toString());
                }
                return false;
            }
        } catch(JAXRException e) {
            throw new RegistryException(e);
        }
    }


    /** Query the registry for web services that satisfy the provided name pattern */
    public Registration[] query(NamePatternQuery query) throws RegistryException {
        _messagePrefix = "query(namePattern): ";
        NamePatternQuery[] namePatterns = new NamePatternQuery[1];
        namePatterns[0] = query;
        return query(namePatterns);
    }


    /** Query the registry for web services that satisfy one of the provided name patterns  */
    public Registration[] query(NamePatternQuery[] queryArray) throws RegistryException {
        try {
            _messagePrefix = "query(namePatterns): ";

            if(queryArray == null)
                throw new IllegalArgumentException();

            printMsg("Querying registry for organization name patterns");

            Collection namePatterns = new ArrayList();
            for(int i = 0; i < queryArray.length; i++) {
                namePatterns.add(_blcm.createInternationalString(_locale, queryArray[i].getNamePattern()));
                printMsg("Name pattern: " + queryArray[i].getNamePattern());
            }

            Collection findQualifiers = new ArrayList();
            findQualifiers.add(FindQualifier.SORT_BY_NAME_ASC);
            printMsg("Defined sort order");
            findQualifiers.add(FindQualifier.OR_ALL_KEYS);
            printMsg("Defined logical operation for query");

            printMsg("Finding organizations with name patterns");
            BulkResponse response = _bqm.findOrganizations(findQualifiers,
                                                           namePatterns,
                                                           null,
                                                           null,
                                                           null,
                                                           null);
            Collection orgs = response.getCollection();
            printMsg("Found " + orgs.size() + " organizations");

            printMsg("Preparing results");
            Registration[] result = fillWSRegistrationArray(orgs);

            if(result != null)
                printMsg("Returning " + result.length + " results");
            else
                printMsg("Returning null results");

            return result;
        } catch(JAXRException e) {
            throw new RegistryException(e);
        }
    }


    /** Query the registry for web services that have the provided classification */
    public Registration[] query(ClassificationQuery query) throws RegistryException {
        _messagePrefix = "query(classification): ";
        ClassificationQuery[] classifications = new ClassificationQuery[1];
        classifications[0] = query;
        return query(classifications);
    }


    /** Query the registry for web services that have one of the provided classifications */
    public Registration[] query(ClassificationQuery[] queryArray) throws RegistryException {
        try {
            _messagePrefix = "query(classifications): ";

            if(queryArray == null)
                throw new IllegalArgumentException();

            printMsg("Querying registry for classifications");

            Collection classifications = new ArrayList();
            for(int i = 0; i < queryArray.length; i++) {
                printMsg("Classification: " + queryArray[i].getClassificationScheme() + " " +
                         queryArray[i].getClassificationName() + " " +
                         queryArray[i].getClassificationValue());

                ClassificationScheme scheme = _bqm.findClassificationSchemeByName(null, queryArray[i].getClassificationScheme());
                if(scheme == null) {
                    printMsg("Classification scheme " + queryArray[i].getClassificationScheme() + " not found");
                    return null;
                }

                Classification classification =
                    _blcm.createClassification(scheme,
                                               _blcm.createInternationalString(_locale, queryArray[i].getClassificationName()),
                                               queryArray[i].getClassificationValue());
                if(classification == null) {
                    printMsg("Unable to create classification " + queryArray[i].getClassificationName() + " " + queryArray[i].getClassificationValue());
                    return null;
                }

                classifications.add(classification);
                printMsg("Classification added to query criteria");
            }

            Collection findQualifiers = new ArrayList();
            findQualifiers.add(FindQualifier.OR_ALL_KEYS);
            printMsg("Defined logical operation for query");

            printMsg("Finding organizations with classifications");
            BulkResponse response = _bqm.findOrganizations(findQualifiers,
                                                           null,
                                                           classifications,
                                                           null,
                                                           null,
                                                           null);
            Collection orgs = response.getCollection();
            printMsg("Found " + orgs.size() + " organizations");

            printMsg("Preparing results");
            Registration[] result = fillWSRegistrationArray(orgs);

            if(result != null)
                printMsg("Returning " + result.length + " results");
            else
                printMsg("Returning null results");

            return result;
        } catch(JAXRException e) {
            throw new RegistryException(e);
        }
    }


    /** Query all web services. */
    public Registration[] queryAll() throws RegistryException {
        _messagePrefix = "queryAll(): ";

        printMsg("Querying all web services on registry");
        NamePatternQuery query = new NamePatternQuery();
        query.setNamePattern("%%");

        NamePatternQuery[] queryArray = new NamePatternQuery[1];
        queryArray[0] = query;

        return query(queryArray);
    }


    /** Check if URI contains a local host reference */
    private boolean isLocalHostRegistration(String uri) {
        final String LOCAL_HOST_DNS = "localhost";
        final String LOCAL_HOST_IP = "127.0.0.1";

        return (uri.indexOf(LOCAL_HOST_DNS) > 0 ||
                uri.indexOf(LOCAL_HOST_IP) > 0);
    }


    /** Iterate through an organization registry object and create and fill a web service registration array */
    private Registration[] fillWSRegistrationArray(Collection organizations) throws JAXRException, RegistryException {

        if(organizations == null)
            throw new IllegalArgumentException();

        if(organizations.size() == 0)
            return null;


        /* This method assumes that there are several organizations but that each organization only has one service,
           each service only has one service binding and one classification.
           If several exist, the result contains the values of the last one that is found. */

        Registration[] registrationArray = new Registration[organizations.size()];
        int i = -1;
        Iterator organizationIterator = organizations.iterator();
        while(organizationIterator.hasNext()) {
            Organization org = (Organization) organizationIterator.next();
            i++;
            printMsg("Organization #" + i);

            RegistrationKey registrationKey = new RegistrationKey(org.getKey());
            printMsg("Key: " + registrationKey.getKey());

            registrationArray[i] = new Registration(registrationKey);

            registrationArray[i].setOrganizationName(org.getName().getValue(_locale));
            if(_optionWarnAboutLocale && registrationArray[i].getOrganizationName() == null)
                throw new JAXRException("Organization name is null! Make sure the query locale is the same as the publish locale!");
            printMsg("Organization name: " + registrationArray[i].getOrganizationName());

            Collection services = org.getServices();
            Iterator serviceIterator = services.iterator();
            while (serviceIterator.hasNext()) {
                Service service = (Service) serviceIterator.next();
                registrationArray[i].setServiceName(service.getName().getValue(_locale));
                printMsg("Service name: " + registrationArray[i].getServiceName());

                Collection serviceBindings = service.getServiceBindings();
                Iterator serviceBindingsIterator = serviceBindings.iterator();
                while (serviceBindingsIterator.hasNext()) {
                    ServiceBinding serviceBinding = (ServiceBinding) serviceBindingsIterator.next();
                    registrationArray[i].setServiceBindingAccessURI(serviceBinding.getAccessURI());
                    printMsg("Service binding access URI: " + registrationArray[i].getServiceBindingAccessURI());
                }
            }

            Collection classifications = org.getClassifications();
            Iterator classificationIterator = classifications.iterator();
            while (classificationIterator.hasNext()) {
                Classification classification = (Classification) classificationIterator.next();

                ClassificationScheme classificationScheme = classification.getClassificationScheme();
                String scheme = classificationScheme.getName().getValue(_locale);
                printMsg("Classification scheme: " + scheme);

                String name = classification.getName().getValue(_locale);
                printMsg("Classification name: " + name);

                String value = classification.getValue();
                printMsg("Classification value: " + value);

                registrationArray[i].setClassification(scheme, name, value);
            }

        }

        return registrationArray;
    }


    /** Delete a web service from the registry server.
        Returns true if the service deletion was performed successfully, false otherwise */
    public boolean delete(RegistrationKey registrationKey) throws RegistryException {
        try {
            _messagePrefix = "delete(key): ";

            printMsg("Deleting the web service");
            String keyString = registrationKey.getKey();
            if(keyString == null) {
                printMsg("Key is null, nothing to delete");
                return false;
            }

            Key key = _blcm.createKey(keyString);
            if(key == null) {
                printMsg("Key not found, nothing to delete");
                return false;
            }

            String id = key.getId();
            Collection keys = new ArrayList();
            keys.add(key);
            printMsg("Organization key: " + id);

            BulkResponse response = _blcm.deleteOrganizations(keys);
            Collection exceptions = response.getExceptions();
            if (exceptions == null) {
                printMsg("Deleted organization");
                return true;
            }

            printMsg("Organization not deleted");
            Iterator exceptionIterator = exceptions.iterator();
            Exception exception = null;
            while (exceptionIterator.hasNext()) {
                exception = (Exception) exceptionIterator.next();
                printMsg("Deletion exception: " + exception.toString());
            }
            return false;
        } catch(JAXRException e) {
            throw new RegistryException(e);
        }
    }


    /** Delete all web services that satisfy the name pattern query.
        Returns true if all service deletions were performed successfully, false otherwise */
    public boolean delete(NamePatternQuery query) throws RegistryException {
        _messagePrefix = "delete(namePattern): ";
        boolean result = true;

        printMsg("Deleting all web services that satisfy the name pattern");

        NamePatternQuery[] queryArray = new NamePatternQuery[1];
        queryArray[0] = query;

        return delete(queryArray);
    }


    /** Delete all web services that satisfy one of the name pattern queries.
        Returns true if all service deletions were performed successfully, false otherwise */
    public boolean delete(NamePatternQuery[] queryArray) throws RegistryException {
        _messagePrefix = "delete(namePatterns): ";
        boolean result = true;

        printMsg("Deleting all web services that satisfy one of the name patterns");

        Registration[] registrationArray = query(queryArray);

        if(registrationArray != null) {
            for(int i=0; i<registrationArray.length; i++) {
                result = result && delete(registrationArray[i].getRegistrationKey());
            }
        }
        return result;
    }


    /** Delete all web services that satisfy the classification query.
        Returns true if all service deletions were performed successfully, false otherwise */
    public boolean delete(ClassificationQuery query) throws RegistryException {
        _messagePrefix = "delete(classification): ";
        boolean result = true;

        printMsg("Deleting all web services that satisfy the classification queries");

        ClassificationQuery[] queryArray = new ClassificationQuery[1];
        queryArray[0] = query;

        return delete(queryArray);
    }


    /** Delete all web services that satisfy one of the classification queries.
        Returns true if all service deletions were performed successfully, false otherwise */
    public boolean delete(ClassificationQuery[] queryArray) throws RegistryException {
        _messagePrefix = "delete(classifications): ";
        boolean result = true;

        printMsg("Deleting all web services that satisfy one of the classification queries");

        Registration[] registrationArray = query(queryArray);

        if(registrationArray != null) {
            for(int i=0; i<registrationArray.length; i++) {
                result = result && delete(registrationArray[i].getRegistrationKey());
            }
        }
        return result;
    }


    /** Delete all web services.
        Returns true if all service deletions were performed successfully, false otherwise */
    public boolean deleteAll() throws RegistryException {
        _messagePrefix = "deleteAll(): ";

        printMsg("Deleting all web services on registry");
        NamePatternQuery query = new NamePatternQuery();
        query.setNamePattern("%%");
        NamePatternQuery[] queryArray = new NamePatternQuery[1];
        queryArray[0] = query;

        return delete(queryArray);
    }


    //
    // Property methods (getters and setters)
    //

    public String getHTTPProxyHost() {
        return _HTTPProxyHost;
    }
    public String getHTTPProxyPort() {
        return _HTTPProxyPort;
    }
    public void setHTTPProxy(String proxyHost, String proxyPort) {
        _HTTPProxyHost = proxyHost;
        _HTTPProxyPort = proxyPort;
    }
    public String getHTTPSProxyHost() {
        return _HTTPSProxyHost;
    }
    public String getHTTPSProxyPort() {
        return _HTTPSProxyPort;
    }
    public void setHTTPSProxy(String proxyHost, String proxyPort) {
        _HTTPSProxyHost = proxyHost;
        _HTTPSProxyPort = proxyPort;
    }
    public Locale getLocale() {
        return _locale;
    }
    public void setLocale(Locale _locale) {
        this._locale = _locale;
    }
    public boolean isOptionOutputMessages() {
        return _optionOutputMessages;
    }
    public void setOptionOutputMessages(boolean outputMessages) {
        _optionOutputMessages = outputMessages;
    }
    public boolean isOptionValidateURI() {
        return _optionValidateURI;
    }
    public void setOptionValidateURI(boolean validateURI) {
        _optionValidateURI = validateURI;
    }
    public boolean isOptionWarnAboutLocale() {
        return _optionWarnAboutLocale;
    }
    public void setOptionWarnAboutLocale(boolean warnAboutLocale) {
        _optionWarnAboutLocale = warnAboutLocale;
    }
    public boolean isOptionWarnAboutLocalHostRegistration() {
        return _optionWarnAboutLocalHostRegistration;
    }
    public void setOptionWarnAboutLocalHostRegistration(boolean warnAboutLocalHostRegistration) {
        _optionWarnAboutLocalHostRegistration = warnAboutLocalHostRegistration;
    }
    public String getPassword() {
        return _password;
    }
    public void setPassword(String _password) {
        this._password = _password;
    }

    public String getURL() {
        return _url;
    }
    public void setURL(String _url) {
        this._url = _url;
    }
    public String getUsername() {
        return _username;
    }
    public void setUsername(String _username) {
        this._username = _username;
    }

}
