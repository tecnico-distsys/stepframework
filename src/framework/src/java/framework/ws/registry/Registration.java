package step.framework.ws.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  Simplified JAX-R registration data model for a web service.
 *
 *  Assumes that each web service registration has:
 *  - 1 organization
 *  - with 1 service
 *  - with 1 service binding
 *  - with 0 or 1 classification
 *
 */
public class Registration {

    /* Key */
    private RegistrationKey _key;

    /* Organization */
    private String _organizationName;
    public static final String ORGANIZATION_NAME_PROPERTY = "organizationName";

    /* Service */
    private String _serviceName;
    public static final String SERVICE_NAME_PROPERTY = "serviceName";

    /* Service binding */
    private String _serviceBindingAccessURI;
    public static final String SERVICE_BINDING_ACCESS_URI_PROPERTY = "serviceBindingAccessURI";

    /* Classification */
    private String _classificationScheme;
    public static final String CLASSIFICATION_SCHEME_PROPERTY = "classificationScheme";
    private String _classificationName;
    public static final String CLASSIFICATION_NAME_PROPERTY = "classificationName";
    private String _classificationValue;
    public static final String CLASSIFICATION_VALUE_PROPERTY = "classificationValue";


    //
    // Constructors
    //

    /** Create a registration with the provided key. */
    public Registration(RegistrationKey key) {
        setRegistrationKey(key);
    }

    /** Create a registration with the provided arguments. */
    public Registration(String organizationName, String serviceName, String serviceBindingAccessURI) {
        setOrganizationName(organizationName);
        setServiceName(serviceName);
        setServiceBindingAccessURI(serviceBindingAccessURI);
    }

    /** Create a registration with the provided arguments. */
    public Registration(RegistrationKey key, String organizationName, String serviceName, String serviceBindingAccessURI) {
        this(organizationName, serviceName, serviceBindingAccessURI);
        setRegistrationKey(key);
    }

    /** Create a registration with the provided arguments. */
    public Registration(String organizationName, String serviceName, String serviceBindingAccessURI, String classificationScheme, String classificationName, String classificationValue) {
        this(organizationName, serviceName, serviceBindingAccessURI);
        setClassification(classificationScheme, classificationName, classificationValue);
    }

    /** Create a registration with the provided arguments. */
    public Registration(RegistrationKey key, String organizationName, String serviceName, String serviceBindingAccessURI, String classificationScheme, String classificationName, String classificationValue) {
        this(organizationName, serviceName, serviceBindingAccessURI, classificationScheme, classificationName, classificationValue);
        setRegistrationKey(key);
    }

    /** Use a property file to load the registration information */
    public Registration(File propertyFile) throws RegistryException {
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            loadFromInputStream(fis);
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Use a property input stream to load the registration information */
    public Registration(InputStream is) throws RegistryException {
        loadFromInputStream(is);
    }

    /** Use a resource path to create an input stream and load registration configuration. */
    public Registration(String resourcePath) throws RegistryException {
        InputStream is = Registration.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IllegalArgumentException("Resource path " + resourcePath + " not found");
        }
        loadFromInputStream(is);
    }

    /** Use a property set to load the registration information */
    public Registration(Properties properties) {
        loadFromProperties(properties);
    }


    //
    // Private auxiliary methods
    //

    /** Use an input stream to load the registration configuration. */
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

    /** Use a property set to load the registration information */
    private void loadFromProperties(Properties properties) {

        String propertyName, property;

        /* required properties */

        propertyName = ORGANIZATION_NAME_PROPERTY;
        property = properties.getProperty(propertyName);
        if(property == null)
            throw new IllegalArgumentException(propertyName + " property is missing from file!");
        setOrganizationName(property);

        propertyName = SERVICE_NAME_PROPERTY;
        property = properties.getProperty(propertyName);
        if(property == null)
            throw new IllegalArgumentException(propertyName + " property is missing from file!");
        setServiceName(property);

        propertyName = SERVICE_BINDING_ACCESS_URI_PROPERTY;
        property = properties.getProperty(propertyName);
        if(property == null)
            throw new IllegalArgumentException(propertyName + " property is missing from file!");
        setServiceBindingAccessURI(property);


        /* optional properties */

        String scheme = properties.getProperty(CLASSIFICATION_SCHEME_PROPERTY);
        String name = properties.getProperty(CLASSIFICATION_NAME_PROPERTY);
        String value = properties.getProperty(CLASSIFICATION_VALUE_PROPERTY);
        if(scheme!= null && name!= null && value != null)
            setClassification(scheme, name, value);

        /* read registration key property */
        setRegistrationKey(new RegistrationKey(properties));

    }


    //
    // Property methods (getters and setters)
    //

    public boolean hasClassification() {
        return _classificationScheme != null && _classificationName != null && _classificationValue != null;
    }

    public String getClassificationScheme() {
        return _classificationScheme;
    }

    public String getClassificationName() {
        return _classificationName;
    }

    public String getClassificationValue() {
        return _classificationValue;
    }

    public void setClassification(String scheme, String name, String value) {
        _classificationScheme = scheme;
        _classificationName = name;
        _classificationValue = value;
    }

    public RegistrationKey getRegistrationKey() {
        return _key;
    }

    public void setRegistrationKey(RegistrationKey key) {
        this._key = key;
    }

    public String getOrganizationName() {
        return _organizationName;
    }

    public void setOrganizationName(String name) {
        _organizationName = name;
    }

    public String getServiceBindingAccessURI() {
        return _serviceBindingAccessURI;
    }

    public void setServiceBindingAccessURI(String bindingAccessURI) {
        _serviceBindingAccessURI = bindingAccessURI;
    }

    public String getServiceName() {
        return _serviceName;
    }

    public void setServiceName(String name) {
        _serviceName = name;
    }

}
