package step.framework.extensions;

import step.framework.domain.DomainException;


/**
 *  This abstract class provides a default implementation for
 *  Extension listener methods.
 */
public abstract class ExtensionListenerBase implements ExtensionListener {

    /**
     *  Empty implementation of extension initialized.<br />
     *  <br />
     */
    public void extensionInitialized(ExtensionListenerParameter param)
      throws ExtensionException {
    }

    /**
     *  Empty implementation of extension destroyed.<br />
     *  <br />
     */
    public void extensionDestroyed(ExtensionListenerParameter param)
      throws ExtensionException {
    }

}
