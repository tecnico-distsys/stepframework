package step.framework.extensions;

import java.util.EventListener;

/**
 *  When properly configured, an extension listener
 *  can be used to perform additional resource
 *  initialization and destruction related to its extension.
 */
public interface ExtensionListener extends EventListener {

    /**
     *  Executed just before the extension is ready for use.
     *
     *  @param param object that provides access to configuration and context data
     *  @throws ExtensionException to report a problem
     */
    public void extensionInitialized(ExtensionListenerParameter param)
      throws ExtensionException;

    /**
     *  Executed just before the extension is destroyed.
     *
     *  @param param object that provides access to configuration and context data
     *  @throws ExtensionException to report a problem
     */
    public void extensionDestroyed(ExtensionListenerParameter param)
      throws ExtensionException;

}
