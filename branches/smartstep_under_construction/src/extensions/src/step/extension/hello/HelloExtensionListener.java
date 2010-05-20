package step.extension.hello;

import java.util.Properties;

import step.framework.oldextensions.ExtensionException;
import step.framework.oldextensions.ExtensionListener;
import step.framework.oldextensions.ExtensionListenerParameter;


/**
 *  This is the Hello extension listener.
 *  If properly configured, it is invoked on extension init and destroy.
 */
public class HelloExtensionListener implements ExtensionListener {

    /** Greet when the extension is initialized */
    public void extensionInitialized(ExtensionListenerParameter param)
    throws ExtensionException {
        Properties extConfig = param.getExtension().getConfig();
        String greeting = extConfig.getProperty("greeting");
        System.out.println(greeting + " (initializing extension)");
    }

    /** Greet when the extension is destroyed */
    public void extensionDestroyed(ExtensionListenerParameter param) throws ExtensionException {
        Properties extConfig = param.getExtension().getConfig();
        String greeting = extConfig.getProperty("greeting");
        System.out.println(greeting + " (destroying extension)");
    }

}
