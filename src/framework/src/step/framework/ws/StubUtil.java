package step.framework.ws;

import javax.xml.ws.BindingProvider;

public class StubUtil {

    //
    // Utility methods
    //
    public static void setPortEndpointAddress(Object port, String url) {
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
    }

    public static String getPortEndpointAddress(Object port) {
        BindingProvider bindingProvider = (BindingProvider) port;
        return (String) bindingProvider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
    }

}
