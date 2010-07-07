package step.framework.extensions.ext3;

import javax.xml.ws.soap.SOAPFaultException;

import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.WebServiceInterceptorParameter;
import step.framework.extensions.WebServiceInterceptorException;

public class MyWebServiceInterceptor3 implements WebServiceInterceptor {

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
