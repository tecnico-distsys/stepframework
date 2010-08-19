package step.framework.extensions.ext1;

import javax.xml.ws.soap.SOAPFaultException;

import step.framework.extensions.*;

public class MyWebServiceInterceptor1 extends WebServiceInterceptorBase {

    @Override
    public boolean interceptMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ interceptMessage(" +
                           param +
                           ")");
        */
        return true;
    }

}
