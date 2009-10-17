package myextension;

import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.WebServiceInterceptorParameter;

public class BadWebServiceInterceptor /*implements WebServiceInterceptor*/ {

    public void inbound(WebServiceInterceptorParameter param) throws Exception {
        System.out.println(this.getClass().getSimpleName() +
                           " @ inbound(" +
                           param +
                           ")");
        return;
    }

    public void outbound(WebServiceInterceptorParameter param) throws Exception {
        System.out.println(this.getClass().getSimpleName() +
                           " @ outbound(" +
                           param +
                           ")");
        return;
    }

}
