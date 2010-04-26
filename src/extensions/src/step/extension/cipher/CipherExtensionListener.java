package step.extension.cipher;

import step.framework.extensions.ExtensionException;
import step.framework.extensions.ExtensionListener;
import step.framework.extensions.ExtensionListenerParameter;

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
