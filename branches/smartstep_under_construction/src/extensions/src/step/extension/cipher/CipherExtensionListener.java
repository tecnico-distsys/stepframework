package step.extension.cipher;

import step.framework.oldextensions.ExtensionException;
import step.framework.oldextensions.ExtensionListener;
import step.framework.oldextensions.ExtensionListenerParameter;

public class CipherExtensionListener implements ExtensionListener {

    public void extensionInitialized(ExtensionListenerParameter param) throws ExtensionException
    {
        System.out.println("Cipher extension init");
    }

    public void extensionDestroyed(ExtensionListenerParameter param) throws ExtensionException
    {
        System.out.println("Cipher extension destroy");
    }
}
