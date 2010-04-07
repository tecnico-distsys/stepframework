package step.framework.perf.monitor.extension;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.*;

import step.framework.extensions.*;


/**
 *  This class implements the STEP Extensions Listener interface.
 *  Its purpose is intercept the initialization and destruction
 *  of the STEP extension.
 */
public class PerfExtensionListener extends ExtensionListenerBase {

    /** Logging */
    private static Log log = LogFactory.getLog(PerfExtensionListener.class);


    //
    //  ExtensionListener
    //

    @Override
    public void extensionInitialized(ExtensionListenerParameter param)
        throws ExtensionException {

        log.trace("extensionInitialized");

    }

    @Override
    public void extensionDestroyed(ExtensionListenerParameter param)
        throws ExtensionException {

        log.trace("extensionDestroyed");
    }

}
