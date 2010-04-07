package hello;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  Query for web service registration using a name pattern.
 *  Name pattern syntax follows SQL-92 LIKE specification.
 *  For instance, "%" is a wildcard.
 */
public class NamePatternQuery extends Query {

    private String _queryNamePattern;
    public static final String QUERY_NAME_PATTERN_PROPERTY = "queryNamePattern";
    private static final String QUERY_NAME_PATTERN_PROPERTY_ALIAS[] =
        { "namePattern", Registration.ORGANIZATION_NAME_PROPERTY };


    //
    // Constructors
    //

    /** Creates an empty name pattern query. */
    public NamePatternQuery() {
        setNamePattern("");
    }

    /** Uses a property file to load the name pattern query */
    public NamePatternQuery(File propertyFile) throws RegistryException {
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            loadFromInputStream(fis);
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Uses a property input stream to load the name pattern query */
    public NamePatternQuery(InputStream is) throws RegistryException {
        try {
            loadFromInputStream(is);
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Uses a resource path to create an input stream and load the name pattern query. */
    public NamePatternQuery(String resourcePath) throws RegistryException {
        try {
            InputStream is = NamePatternQuery.class.getResourceAsStream(resourcePath);
            if (is == null) {
                throw new IllegalArgumentException("Resource path " + resourcePath + " not found");
            }
            loadFromInputStream(is);
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Uses a property set to load the name pattern query */
    public NamePatternQuery(Properties properties) {
        loadFromProperties(properties);
    }


    //
    // Auxiliary private methods
    //

    /** Uses an input stream to load the name pattern query. */
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

    /** Uses a property set to load the name pattern query */
    private void loadFromProperties(Properties properties) {

        String propertyName, property;

        /* required properties */

        propertyName = QUERY_NAME_PATTERN_PROPERTY;
        property = properties.getProperty(propertyName);
        if(property == null) {
            // try alias
            for(int i = 0; i < QUERY_NAME_PATTERN_PROPERTY_ALIAS.length; i++) {
                propertyName = QUERY_NAME_PATTERN_PROPERTY_ALIAS[i];
                property = properties.getProperty(propertyName);
                if(property != null)
                    break;
            }
        }
        if(property == null)
            throw new IllegalArgumentException(QUERY_NAME_PATTERN_PROPERTY + " property is missing from file!");
        setNamePattern(property);

    }

    //
    // Property methods (getters and setters)
    //

    public String getNamePattern() {
        return _queryNamePattern;
    }

    public void setNamePattern(String namePattern) {
        _queryNamePattern = namePattern;
    }

}
