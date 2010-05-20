package step.extension.errors;

import java.util.Properties;

import step.framework.oldextensions.ExtensionException;
import step.framework.oldextensions.ExtensionListener;
import step.framework.oldextensions.ExtensionListenerParameter;


/**
 *  This is the Errors extension listener.
 *  If properly configured, it is invoked on extension init and destroy.
 */
public class ErrorsExtensionListener implements ExtensionListener {

    public void extensionInitialized(ExtensionListenerParameter param) throws ExtensionException {
        Properties extConfig = param.getExtension().getConfig();
        String exceptionClassName = extConfig.getProperty("listener.initialized.throw");

        if(exceptionClassName != null && exceptionClassName.trim().length() > 0) {
            try {
                ThrowExceptionUtil.throwException(exceptionClassName, "extensionInitialized");
            } catch(ExtensionException ee) {
                throw ee;
            } catch(RuntimeException rte) {
                throw rte;
            } catch(Exception e) {
                System.out.println("exception type not declared in throws; wrapping exception in runtime exception");
                throw new RuntimeException(e);
            }
        }
    }

    public void extensionDestroyed(ExtensionListenerParameter param) throws ExtensionException {
        Properties extConfig = param.getExtension().getConfig();
        String exceptionClassName = extConfig.getProperty("listener.destroyed.throw");

        if(exceptionClassName != null && exceptionClassName.trim().length() > 0) {
            try {
                ThrowExceptionUtil.throwException(exceptionClassName, "extensionDestroyed");
            } catch(ExtensionException ee) {
                throw ee;
            } catch(RuntimeException rte) {
                throw rte;
            } catch(Exception e) {
                System.out.println("exception type not declared in throws; wrapping exception in runtime exception");
                throw new RuntimeException(e);
            }
        }
    }

}
