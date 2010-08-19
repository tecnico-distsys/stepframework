package hello;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  Query for web service registration using a classification.
 */
public class ClassificationQuery extends Query {

    public static final String CLASSIFICATION_SCHEME_PROPERTY = "queryClassificationScheme";
    private static final String CLASSIFICATION_SCHEME_PROPERTY_ALIAS[] =
        { "classificationScheme", Registration.CLASSIFICATION_SCHEME_PROPERTY };

    private String _queryClassificationName;
    public static final String CLASSIFICATION_NAME_PROPERTY = "queryClassificationName";
    private static final String CLASSIFICATION_NAME_PROPERTY_ALIAS[] =
        { "classificationName", Registration.CLASSIFICATION_NAME_PROPERTY };

    private String _queryClassificationValue;
    public static final String CLASSIFICATION_VALUE_PROPERTY = "queryClassificationValue";
    private static final String CLASSIFICATION_VALUE_PROPERTY_ALIAS[] =
        { "classificationValue", Registration.CLASSIFICATION_VALUE_PROPERTY };
    private String _queryClassificationScheme;


    //
    // Constructors
    //

    /** Create a classification query from the provided arguments */
    public ClassificationQuery(String scheme, String name, String value) {
        setClassification(scheme, name, value);
    }

    /** Use a property file to load the classification query */
    public ClassificationQuery(File propertyFile) throws RegistryException {
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            loadFromInputStream(fis);
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Use a property input stream to load the classification query */
    public ClassificationQuery(InputStream is) throws RegistryException {
        try {
            loadFromInputStream(is);
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Use a resource path to create an input stream and load classification query. */
    public ClassificationQuery(String resourcePath) throws RegistryException {
        try {
            InputStream is = ClassificationQuery.class.getResourceAsStream(resourcePath);
            if (is == null) {
                throw new IllegalArgumentException("Resource path " + resourcePath + " not found");
            }
            loadFromInputStream(is);
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Use a property set to load the classification query */
    public ClassificationQuery(Properties properties) {
        loadFromProperties(properties);
    }


    //
    // Private auxiliary methods
    //

    /** Use an input stream to load the classification query. */
    private void loadFromInputStream(InputStream is) throws IOException {
        try {
            Properties properties = new Properties();
            properties.load(is);
            loadFromProperties(properties);
        } finally {
            if(is != null)
                is.close();
        }
    }

    /** Uses a property set to load the classification query */
    private void loadFromProperties(Properties properties) {

        /* required properties */

        String schemePropertyName = CLASSIFICATION_SCHEME_PROPERTY;
        String schemeProperty = properties.getProperty(schemePropertyName);
        if(schemeProperty == null) {
            // try alias
            for(int i = 0; i < CLASSIFICATION_SCHEME_PROPERTY_ALIAS.length; i++) {
                schemePropertyName = CLASSIFICATION_SCHEME_PROPERTY_ALIAS[i];
                schemeProperty = properties.getProperty(schemePropertyName);
                if(schemeProperty != null)
                    break;
            }
        }
        if(schemeProperty == null)
            throw new IllegalArgumentException(CLASSIFICATION_SCHEME_PROPERTY + " property is missing from file!");


        String namePropertyName = CLASSIFICATION_NAME_PROPERTY;
        String nameProperty = properties.getProperty(namePropertyName);
        if(nameProperty == null) {
            // try alias
            for(int i = 0; i < CLASSIFICATION_NAME_PROPERTY_ALIAS.length; i++) {
                namePropertyName = CLASSIFICATION_NAME_PROPERTY_ALIAS[i];
                nameProperty = properties.getProperty(namePropertyName);
                if(nameProperty != null)
                    break;
            }
        }
        if(nameProperty == null)
            throw new IllegalArgumentException(CLASSIFICATION_NAME_PROPERTY + " property is missing from file!");


        String valuePropertyName = CLASSIFICATION_VALUE_PROPERTY;
        String valueProperty = properties.getProperty(valuePropertyName);
        if(valueProperty == null) {
            // try alias
            for(int i = 0; i < CLASSIFICATION_VALUE_PROPERTY_ALIAS.length; i++) {
                valuePropertyName = CLASSIFICATION_VALUE_PROPERTY_ALIAS[i];
                valueProperty = properties.getProperty(valuePropertyName);
                if(valueProperty != null)
                    break;
            }
        }
        if(valueProperty == null)
            throw new IllegalArgumentException(CLASSIFICATION_VALUE_PROPERTY + " property is missing from file!");

        setClassification(schemeProperty, nameProperty, valueProperty);

    }


    //
    // Property methods (getters and setters)
    //

    public String getClassificationName() {
        return _queryClassificationName;
    }

    public String getClassificationScheme() {
        return _queryClassificationScheme;
    }

    public String getClassificationValue() {
        return _queryClassificationValue;
    }

    public void setClassification(String scheme, String name, String value) {
        _queryClassificationScheme = scheme;
        _queryClassificationName = name;
        _queryClassificationValue = value;
    }

}
