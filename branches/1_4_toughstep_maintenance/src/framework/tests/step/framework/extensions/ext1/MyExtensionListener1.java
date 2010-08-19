package step.framework.extensions.ext1;

import step.framework.extensions.*;

public class MyExtensionListener1 extends ExtensionListenerBase {

    @Override
    public void extensionInitialized(ExtensionListenerParameter param)
    throws ExtensionException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ extensionInitialized(" +
                           param +
                           ")");
        */
    }

    @Override
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
