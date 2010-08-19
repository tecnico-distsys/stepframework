package step.extension.hello;

import java.util.Properties;

import step.framework.extensions.*;


/**
 *  This is the Hello extension listener.
 *  If properly configured, it is invoked on extension init and destroy.
 */
public class HelloExtensionListener extends ExtensionListenerBase {

    /** Greet when the extension is initialized */
    @Override
    public void extensionInitialized(ExtensionListenerParameter param)
    throws ExtensionException {
        Properties extConfig = param.getExtension().getConfig();
        String greeting = extConfig.getProperty("greeting");
        System.out.println(greeting + " (initializing extension)");
    }

    /** Greet when the extension is destroyed */
    @Override
    public void extensionDestroyed(ExtensionListenerParameter param) throws ExtensionException {
        Properties extConfig = param.getExtension().getConfig();
        String greeting = extConfig.getProperty("greeting");
        System.out.println(greeting + " (destroying extension)");
    }

}
