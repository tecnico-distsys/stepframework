package hello.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.config.Config;
import step.framework.config.ConfigUtil;

import hello.exception.ws.HelloWSException;
import hello.exception.ws.MissingNameException;

import hello.wsdl.HelloPortType;
import hello.wsdl.ServiceError;
import hello.wsdl.ServiceError_Exception;

@javax.jws.WebService (endpointInterface="hello.wsdl.HelloPortType")
public class HelloWebServiceImpl implements HelloPortType {

    /** logging */
    private static Log log = LogFactory.getLog(HelloWebServiceImpl.class);
    
    public String sayHello(String name) throws ServiceError_Exception {
        try	{
            log.trace("Starting sayHello on server");
            log.trace("name = " + name);
            
            boolean throwError = ConfigUtil.recognizeAsTrue(Config.getInstance().getInitParameter("hello.ws.ThrowServiceError"));
            if(throwError)
                throw new MissingNameException("Name is missing (forced by hello.ws.ThrowServiceError property for testing)", null);
            
            if(name == null || name.trim().length() == 0)
                throw new MissingNameException("Name is missing", null);

            String returnValue = "Hello " + name;
            log.trace("returnValue = " + returnValue);
            return returnValue;

        } catch (Exception e) {
            log.trace("Caught exception");
            log.error(e);
            log.trace("Rethrowing inside a ServiceError_Exception");
            throw new ServiceError_Exception(e.getMessage(), new ServiceError(), e);

        } finally {
            log.trace("Finally exiting sayHello on server");
        }

    }

}
