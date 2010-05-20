package step.framework.oldextensions.ext3;

import javax.xml.ws.soap.SOAPFaultException;

import step.framework.oldextensions.WebServiceInterceptor;
import step.framework.oldextensions.WebServiceInterceptorParameter;
import step.framework.oldextensions.WebServiceInterceptorException;

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
