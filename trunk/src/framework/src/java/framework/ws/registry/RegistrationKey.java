package step.framework.ws.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Key;

/**
 *  JAX-R web service registration key
 */
public class RegistrationKey {

    private String _key;
    public static final String KEY_PROPERTY = "registrationKey";

    //
    // Constructors
    //

    /** Create an empty registration key */
    public RegistrationKey() {
    }

    /** Create a registration key from string representation */
    /*
    public RegistrationKey(String key) {
        setKey(key);
    }
    */

    /** Create a registration key from JAX-R Key object */
    public RegistrationKey(Key key) throws RegistryException {
        try {
            setKey(key.getId());
        } catch(JAXRException e) {
            throw new RegistryException(e);
        }
    }

    /** Use a property file to load the registration key.
        The value can be read from an individual file or from a registration properties file. */
    public RegistrationKey(File propertyFile) throws RegistryException {
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            loadKey(fis);
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Use a property input stream to load the registration key.
        The value can be read from a registration properties input stream. */
    public RegistrationKey(InputStream is) throws RegistryException {
        loadKey(is);
    }

    /** Use a property set to load the registration key. */
    public RegistrationKey(Properties properties) {
        loadKey(properties);
    }

    /** Use a resource path to create an input stream and load registration key. */
    public RegistrationKey(String resourcePath) throws RegistryException {
        InputStream is = RegistrationKey.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IllegalArgumentException("Resource path " + resourcePath + " not found");
        }
        loadKey(is);
    }


    //
    // Key load and store
    //

    /** Loads the key from a property file */
    public void loadKey(File propertyFile) throws RegistryException {
        try {
            loadKey(new FileInputStream(propertyFile));
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Loads the key from a property input stream */
    public void loadKey(InputStream is) throws RegistryException {
        try {
            try {
                Properties properties = new Properties();
                properties.load(is);

                loadKey(properties);

            } finally {
                if(is != null)
                    is.close();
            }
        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Loads the key from a property set */
    public void loadKey(Properties properties) {

        /* optional properties */

        String key = properties.getProperty(KEY_PROPERTY);
        if(key!= null)
            setKey(key);

    }

    /** Stores the key in a property file, maintaining other existing properties.
        The file is created if it doesn't exist. */
    public void storeKey(File propertyFile) throws RegistryException {
        try {
            Properties properties = null;

            if(!propertyFile.exists()) {
                // create new file
                propertyFile.createNewFile();
                properties = new Properties();
            } else {
                // load existing file
                FileInputStream fis = new FileInputStream(propertyFile);
                try {
                    properties = new Properties();
                    properties.load(fis);
                } finally {
                    if(fis != null)
                        fis.close();
                }
            }

            // add/update property
            storeKey(properties);

            // store file with new contents
            FileOutputStream fos = new FileOutputStream(propertyFile);
            try {
                properties.store(fos, null);
            } finally {
                if(fos != null)
                    fos.close();
            }

        } catch(IOException e) {
            throw new RegistryException(e);
        }
    }

    /** Sets the key property in a property set. */
    public void storeKey(Properties properties) {

        // add/update property
        properties.setProperty(KEY_PROPERTY, _key);

    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }

}
