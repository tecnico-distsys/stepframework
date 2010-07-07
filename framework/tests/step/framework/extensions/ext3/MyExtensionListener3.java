package step.framework.extensions.ext3;

import java.util.*;

import step.framework.extensions.ExtensionListener;
import step.framework.extensions.ExtensionListenerParameter;
import step.framework.extensions.ExtensionException;

public class MyExtensionListener3 implements ExtensionListener {

    public void extensionInitialized(ExtensionListenerParameter param)
    throws ExtensionException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ extensionInitialized(" +
                           param +
                           ")");
        */
        
        Map<String,Object> ctx = param.getExtension().getContext();
        ctx.put("ext3ctx.var", "3");
    }

    public void extensionDestroyed(ExtensionListenerParameter param)
    throws ExtensionException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ extensionDestroyed(" +
                           param +
                           ")");
        */

        Map<String,Object> ctx = param.getExtension().getContext();
        ctx.remove("ext3ctx.var");
    }

}
