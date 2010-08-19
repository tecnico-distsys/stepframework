package step.framework.extensions.ext3;

import java.util.*;

import step.framework.extensions.*;

public class MyExtensionListener3 extends ExtensionListenerBase {

    @Override
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

    @Override
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
