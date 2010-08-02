package step.extension.errors;

import step.framework.extensions.ExtensionException;
import step.framework.extensions.ExtensionListener;

/**
 *  This is the Errors extension listener.
 *  If properly configured, it is invoked on extension init and destroy.
 */
public class ErrorsExtensionListener extends ExtensionListener {

    public void extensionInitialized() throws ExtensionException
    {
        String exceptionClassName = (String) getExtension().getProperty("listener.initialized.throw");

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

    public void extensionDestroyed() throws ExtensionException
    {
        String exceptionClassName = (String) getExtension().getProperty("listener.destroyed.throw");

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
