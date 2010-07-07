package step.framework.extensions.ext1;

import step.framework.extensions.ExtensionListener;
import step.framework.extensions.ExtensionListenerParameter;
import step.framework.extensions.ExtensionException;

public class MyExtensionListener1 implements ExtensionListener {

    public void extensionInitialized(ExtensionListenerParameter param)
    throws ExtensionException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ extensionInitialized(" +
                           param +
                           ")");
        */
    }

    public void extensionDestroyed(ExtensionListenerParameter param)
    throws ExtensionException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ extensionDestroyed(" +
                           param +
                           ")");
        */
    }

}
