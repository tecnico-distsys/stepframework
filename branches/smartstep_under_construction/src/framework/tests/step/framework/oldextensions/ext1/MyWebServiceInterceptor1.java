package step.framework.oldextensions.ext1;

import javax.xml.ws.soap.SOAPFaultException;

import step.framework.oldextensions.WebServiceInterceptor;
import step.framework.oldextensions.WebServiceInterceptorException;
import step.framework.oldextensions.WebServiceInterceptorParameter;

public class MyWebServiceInterceptor1 implements WebServiceInterceptor {

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
