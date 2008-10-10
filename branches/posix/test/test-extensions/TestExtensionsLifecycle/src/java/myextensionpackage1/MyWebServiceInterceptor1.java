package myextensionpackage1;

import javax.xml.ws.soap.SOAPFaultException;

import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.WebServiceInterceptorException;
import step.framework.extensions.WebServiceInterceptorParameter;


public class MyWebServiceInterceptor1 implements WebServiceInterceptor {

    public boolean interceptMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {
        System.out.println(this.getClass().getSimpleName() +
                           " @ interceptMessage(" +
                           param +
                           ")");
        return true;
    }

}
