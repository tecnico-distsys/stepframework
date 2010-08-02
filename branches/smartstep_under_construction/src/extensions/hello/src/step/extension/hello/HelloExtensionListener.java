package step.extension.hello;

import step.framework.extensions.ExtensionException;
import step.framework.extensions.ExtensionListener;

/**
 *  This is the Hello extension listener.
 *  If properly configured, it is invoked on extension init and destroy.
 */
public class HelloExtensionListener extends ExtensionListener {

    /** Greet when the extension is initialized */
    public void extensionInitialized() throws ExtensionException
    {
        String greeting = (String) getExtension().getProperty("greeting");
        System.out.println(greeting + " (initializing extension)");
    }

    /** Greet when the extension is destroyed */
    public void extensionDestroyed() throws ExtensionException
    {
        String greeting = (String) getExtension().getProperty("greeting");
        System.out.println(greeting + " (destroying extension)");
    }

}
