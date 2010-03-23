package step.framework.ws.registry;

import java.io.*;
import java.net.PasswordAuthentication;
import java.util.*;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *  Simplified JAX-R web service registry access class.
 */
@SuppressWarnings("unchecked")
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
    private boolean _optionValidateURI = false;
    public static final String OPTION_VALIDATE_URI_PROPERTY = "optionValidateURI";

    private boolean _optionWarnAboutLocale = true;
    public static final String OPTION_WARN_ABOUT_LOCALE_PROPERTY = "optionWarnAboutLocale";

    private boolean _optionWarnAboutLocalHostRegistration = true;
    public static final String OPTION_WARN_ABOUT_LOCAL_HOST_REGISTRATION_PROPERTY = "optionWarnAboutLocalHostRegistration";

    /* logging */
    Log log = LogFactory.getLog(Registry.class);


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
            log.debug("caught IOException when trying to load property file");
            log.debug(e);
            log.trace("exception details", e);
            log.debug("throwing registry exception");
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
            log.debug("resource not found when trying to load property file");
            log.debug("throwing illegal argument exception");
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
            log.debug("caught IOException when trying to load the registry configuration from an input stream");
            log.debug(e);
            log.trace("exception details", e);
            log.debug("throwing registry exception");
            throw new RegistryException(e);
        }
    }

    /** Use a property set to load the registry configuration. */
    private void loadFromProperties(Properties properties) {

        String propertyName, property;

        /* required properties */

        propertyName = URL_PROPERTY;
        property = properties.getProperty(propertyName);
        if(property == null)
            throw new IllegalArgumentException(propertyName + " property is missing from file!");
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
            log.info("Establishing connection to registry server " + _url);

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
            log.info("Connection established");

            _rs = _connection.getRegistryService();
            _blcm = _rs.getBusinessLifeCycleManager();
            _bqm = _rs.getBusinessQueryManager();
            log.trace("JAX-R objects initialized");

            if(authenticate) {
                log.info("Performing authentication for user " + _username);
                PasswordAuthentication passwdAuth = new PasswordAuthentication(_username, _password.toCharArray());
                Set creds = new HashSet();
                creds.add(passwdAuth);
                _connection.setCredentials(creds);
                log.debug("Authentication credentials set");
            }
        } catch(JAXRException e) {
            log.debug("caught JAXRException on connect method");
            log.debug(e);
            log.trace("exception details", e);
            log.debug("throwing registry exception");
            throw new RegistryException(e);
        }
    }


    /** Terminate connection with registry server */
    public void disconnect() throws RegistryException {
        try {
            if (_connection == null) {
                log.warn("Trying to disconnect null connection");
            } else {
                _connection.close();
                log.info("Closed connection to registry server " + _url);
            }
        } catch(JAXRException e) {
            log.debug("caught JAXRException on disconnect method");
            log.debug(e);
            log.trace("exception details", e);
            log.debug("throwing registry exception");
            throw new RegistryException(e);
        }
    }


    /** Publish a web service in the registry server
        Returns true if the service registration was performed successfully, false otherwise.
        The registration key generated by the registry is stored in the parameter registration. */
    public boolean publish(Registration registration) throws RegistryException {
        try {
            log.info("Publishing the web service");

            InternationalString is = _blcm.createInternationalString(_locale, registration.getOrganizationName());
            Organization org = _blcm.createOrganization(is);
            is = _blcm.createInternationalString(_locale, "");
            org.setDescription(is);
            Collection orgs = new ArrayList();
            orgs.add(org);
            log.trace("Created organization object with name " + registration.getOrganizationName());

            if(!registration.hasClassification()) {
                log.trace("Registration doesn't have a classification");
            } else {
                ClassificationScheme classificationScheme = _bqm.findClassificationSchemeByName(null, registration.getClassificationScheme());
                if(classificationScheme != null) {
                    log.trace("Found classification scheme " + registration.getClassificationScheme());
                } else {
                    log.error("Classification scheme " + registration.getClassificationScheme() + " not found");
                    return false;
                }

                Classification classification =
                    _blcm.createClassification(classificationScheme, registration.getClassificationName(),  registration.getClassificationValue());
                if(log.isTraceEnabled()) {
                    log.trace("Created classification " + registration.getClassificationName() + " " + registration.getClassificationValue());
                }

                Collection classifications = new ArrayList();
                classifications.add(classification);
                org.addClassifications(classifications);
                log.trace("Added classification to organization");
            }

            is = _blcm.createInternationalString(_locale, registration.getServiceName());
            Service service = _blcm.createService(is);
            is = _blcm.createInternationalString(_locale, "");
            service.setDescription(is);
            log.debug("Created service with name " + registration.getServiceName());

            ServiceBinding binding = _blcm.createServiceBinding();
            is = _blcm.createInternationalString(_locale, "");
            binding.setDescription(is);
            binding.setValidateURI(_optionValidateURI);
            String uri = registration.getServiceBindingAccessURI();
            binding.setAccessURI(uri);
            log.debug("Created service binding with URI: " + uri);

            // local host warning check
            if(_optionWarnAboutLocalHostRegistration && isLocalHostRegistration(uri)) {
                if(log.isWarnEnabled()) {
                    log.warn("WARNING: registering a localhost address." +
                             " You probably want to use the machine name or IP instead.");
                }
            }

            Collection serviceBindings = new ArrayList();
            serviceBindings.add(binding);
            service.addServiceBindings(serviceBindings);
            log.trace("Added service binding to service");

            Collection services = new ArrayList();
            services.add(service);
            org.addServices(services);
            log.trace("Added service to organization");

            log.info("Saving organization");
            BulkResponse response = _blcm.saveOrganizations(orgs);
            Collection exceptions = response.getExceptions();
            if(exceptions == null) {
                log.info("Organization registered");
                Collection keys = response.getCollection();
                Iterator keyIterator = keys.iterator();
                if(keyIterator.hasNext()) {
                    Key organizationKey = (Key) keyIterator.next();
                    String organizationID = organizationKey.getId();
                    log.info("Organization ID is: " + organizationID);

                    RegistrationKey regKey = new RegistrationKey();
                    regKey.setKey(organizationID);

                    registration.setRegistrationKey(regKey);
                    return true;
                } else {
                    log.error("Organization ID not found");
                    return false;
                }
            } else {
                log.error("Organization not registered");
                Iterator exceptionIterator = exceptions.iterator();
                Exception exception = null;
                while (exceptionIterator.hasNext()) {
                    exception = (Exception) exceptionIterator.next();
                    System.err.println("Registration exception: " + exception.toString());
                }
                return false;
            }
        } catch(JAXRException e) {
            log.debug("caught JAXRException on publish method");
            log.debug(e);
            log.trace("exception details", e);
            log.debug("throwing registry exception");
            throw new RegistryException(e);
        }
    }


    /** Query the registry for web services that satisfy the provided name pattern */
    public Registration[] query(NamePatternQuery query) throws RegistryException {
        NamePatternQuery[] namePatterns = new NamePatternQuery[1];
        namePatterns[0] = query;
        return query(namePatterns);
    }


    /** Query the registry for web services that satisfy one of the provided name patterns  */
    public Registration[] query(NamePatternQuery[] queryArray) throws RegistryException {
        try {
            if(queryArray == null) {
                throw new IllegalArgumentException("Name pattern query array is null");
            }

            log.info("Querying registry for organization name patterns");

            Collection namePatterns = new ArrayList();
            for(int i = 0; i < queryArray.length; i++) {
                namePatterns.add(_blcm.createInternationalString(_locale, queryArray[i].getNamePattern()));
                log.info("Name pattern: " + queryArray[i].getNamePattern());
            }

            Collection findQualifiers = new ArrayList();
            findQualifiers.add(FindQualifier.SORT_BY_NAME_ASC);
            log.trace("Defined sort order");
            findQualifiers.add(FindQualifier.OR_ALL_KEYS);
            log.trace("Defined logical operation for query");

            log.debug("Finding organizations with name patterns");
            BulkResponse response = _bqm.findOrganizations(findQualifiers,
                                                           namePatterns,
                                                           null,
                                                           null,
                                                           null,
                                                           null);
            Collection orgs = response.getCollection();
            log.info("Found " + orgs.size() + " organizations");

            log.debug("Preparing results");
            Registration[] result = fillWSRegistrationArray(orgs);

            if(result != null)
                log.debug("Returning " + result.length + " results");
            else
                log.debug("Returning null results");

            return result;
        } catch(JAXRException e) {
            log.debug("caught JAXRException on name pattern query method");
            log.debug(e);
            log.trace("exception details", e);
            log.debug("throwing registry exception");
            throw new RegistryException(e);
        }
    }


    /** Query the registry for web services that have the provided classification */
    public Registration[] query(ClassificationQuery query) throws RegistryException {
        ClassificationQuery[] classifications = new ClassificationQuery[1];
        classifications[0] = query;
        return query(classifications);
    }


    /** Query the registry for web services that have one of the provided classifications */
    public Registration[] query(ClassificationQuery[] queryArray) throws RegistryException {
        try {
            if(queryArray == null)
                throw new IllegalArgumentException("Classification query array is null");

            log.info("Querying registry for classifications");

            Collection classifications = new ArrayList();
            for(int i = 0; i < queryArray.length; i++) {
                log.info("Classification: " + queryArray[i].getClassificationScheme() + " " +
                         queryArray[i].getClassificationName() + " " +
                         queryArray[i].getClassificationValue());

                ClassificationScheme scheme = _bqm.findClassificationSchemeByName(null, queryArray[i].getClassificationScheme());
                if(scheme == null) {
                    log.error("Classification scheme " + queryArray[i].getClassificationScheme() + " not found");
                    return null;
                }

                Classification classification =
                    _blcm.createClassification(scheme,
                                               _blcm.createInternationalString(_locale, queryArray[i].getClassificationName()),
                                               queryArray[i].getClassificationValue());
                if(classification == null) {
                    log.error("Unable to create classification " + queryArray[i].getClassificationName() + " " + queryArray[i].getClassificationValue());
                    return null;
                }

                classifications.add(classification);
                log.trace("Classification added to query criteria");
            }

            Collection findQualifiers = new ArrayList();
            findQualifiers.add(FindQualifier.OR_ALL_KEYS);
            log.trace("Defined logical operation for query");

            log.info("Finding organizations with classifications");
            BulkResponse response = _bqm.findOrganizations(findQualifiers,
                                                           null,
                                                           classifications,
                                                           null,
                                                           null,
                                                           null);
            Collection orgs = response.getCollection();
            log.info("Found " + orgs.size() + " organizations");

            log.debug("Preparing results");
            Registration[] result = fillWSRegistrationArray(orgs);

            if(result != null)
                log.debug("Returning " + result.length + " results");
            else
                log.debug("Returning null results");

            return result;
        } catch(JAXRException e) {
            log.debug("caught JAXRException on classification query method");
            log.debug(e);
            log.trace("exception details", e);
            log.debug("throwing registry exception");
            throw new RegistryException(e);
        }
    }


    /** Query all web services. */
    public Registration[] queryAll() throws RegistryException {
        log.info("Querying all web services on registry");
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

        if(organizations == null) {
            throw new IllegalArgumentException("organizations collection is null");
        }

        if(organizations.size() == 0) {
            log.debug("organizations collection is empty");
            return null;
        }


        /* This method assumes that there are several organizations but that each organization only has one service,
           each service only has one service binding and one classification.
           If several exist, the result contains the values of the last one that is found. */

        Registration[] registrationArray = new Registration[organizations.size()];
        int i = -1;
        Iterator organizationIterator = organizations.iterator();
        while(organizationIterator.hasNext()) {
            Organization org = (Organization) organizationIterator.next();
            i++;
            log.debug("Organization #" + i);

            RegistrationKey registrationKey = new RegistrationKey(org.getKey());
            log.debug("Key: " + registrationKey.getKey());

            registrationArray[i] = new Registration(registrationKey);

            registrationArray[i].setOrganizationName(org.getName().getValue(_locale));
            if(_optionWarnAboutLocale && registrationArray[i].getOrganizationName() == null)
                throw new JAXRException("Organization name is null! Make sure the query locale is the same as the publish locale!");
            log.trace("Organization name: " + registrationArray[i].getOrganizationName());

            Collection services = org.getServices();
            Iterator serviceIterator = services.iterator();
            while (serviceIterator.hasNext()) {
                Service service = (Service) serviceIterator.next();
                registrationArray[i].setServiceName(service.getName().getValue(_locale));
                log.debug("Service name: " + registrationArray[i].getServiceName());

                Collection serviceBindings = service.getServiceBindings();
                Iterator serviceBindingsIterator = serviceBindings.iterator();
                while (serviceBindingsIterator.hasNext()) {
                    ServiceBinding serviceBinding = (ServiceBinding) serviceBindingsIterator.next();
                    registrationArray[i].setServiceBindingAccessURI(serviceBinding.getAccessURI());
                    log.debug("Service binding access URI: " + registrationArray[i].getServiceBindingAccessURI());
                }
            }

            Collection classifications = org.getClassifications();
            Iterator classificationIterator = classifications.iterator();
            while (classificationIterator.hasNext()) {
                Classification classification = (Classification) classificationIterator.next();

                ClassificationScheme classificationScheme = classification.getClassificationScheme();
                String scheme = classificationScheme.getName().getValue(_locale);
                log.debug("Classification scheme: " + scheme);

                String name = classification.getName().getValue(_locale);
                log.debug("Classification name: " + name);

                String value = classification.getValue();
                log.debug("Classification value: " + value);

                registrationArray[i].setClassification(scheme, name, value);
            }

        }

        return registrationArray;
    }


    /** Delete a web service from the registry server.
        Returns true if the service deletion was performed successfully, false otherwise */
    public boolean delete(RegistrationKey registrationKey) throws RegistryException {
        try {
            log.info("Deleting web service registration");

            String keyString = registrationKey.getKey();
            if(keyString == null) {
                log.info("Key is null, nothing to delete");
                return false;
            }

            Key key = _blcm.createKey(keyString);
            if(key == null) {
                log.info("Key not found, nothing to delete");
                return false;
            }

            String id = key.getId();
            Collection keys = new ArrayList();
            keys.add(key);
            log.info("Organization key: " + id);

            BulkResponse response = _blcm.deleteOrganizations(keys);
            Collection exceptions = response.getExceptions();
            if (exceptions == null) {
                log.info("Deleted organization");
                return true;
            }

            log.error("Organization not deleted");
            Iterator exceptionIterator = exceptions.iterator();
            Exception exception = null;
            while (exceptionIterator.hasNext()) {
                exception = (Exception) exceptionIterator.next();
                log.error("Deletion exception: " + exception.toString());
            }
            return false;

        } catch(JAXRException e) {
            log.debug("caught JAXRException on delete method");
            log.debug(e);
            log.trace("exception details", e);
            log.debug("throwing registry exception");
            throw new RegistryException(e);
        }
    }


    /** Delete all web services that satisfy the name pattern query.
        Returns true if all service deletions were performed successfully, false otherwise */
    public boolean delete(NamePatternQuery query) throws RegistryException {

        log.info("Deleting all web service registrations that satisfy the name pattern");

        NamePatternQuery[] queryArray = new NamePatternQuery[1];
        queryArray[0] = query;

        return delete(queryArray);
    }


    /** Delete all web services that satisfy one of the name pattern queries.
        Returns true if all service deletions were performed successfully, false otherwise */
    public boolean delete(NamePatternQuery[] queryArray) throws RegistryException {
        boolean result = true;

        log.info("Deleting all web service registrations that satisfy one of the name patterns");

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
        log.info("Deleting all web service registrations that satisfy the classification queries");

        ClassificationQuery[] queryArray = new ClassificationQuery[1];
        queryArray[0] = query;

        return delete(queryArray);
    }


    /** Delete all web services that satisfy one of the classification queries.
        Returns true if all service deletions were performed successfully, false otherwise */
    public boolean delete(ClassificationQuery[] queryArray) throws RegistryException {
        boolean result = true;

        log.info("Deleting all web service registrations that satisfy one of the classification queries");

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
        log.info("Deleting all web service registrations on registry");
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
