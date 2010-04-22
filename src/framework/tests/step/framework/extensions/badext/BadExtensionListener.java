package step.framework.extensions.badext;

import step.framework.extensions.ExtensionListenerParameter;

public class BadExtensionListener /*implements ExtensionListener*/ {

    public void extensionInitialized(ExtensionListenerParameter param) {
        System.out.println(this.getClass().getSimpleName() +
                           " @ extensionInitialized(" +
                           param +
                           ")");
    }

    public void extensionDestroyed(ExtensionListenerParameter param) {
        System.out.println(this.getClass().getSimpleName() +
                           " @ extensionDestroyed(" +
                           param +
                           ")");
    }

}
