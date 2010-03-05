package hello.ws;

import step.framework.config.Config;
import step.framework.config.ConfigUtil;

import hello.exception.ws.HelloWSException;
import hello.exception.ws.MissingNameException;

import hello.wsdl.HelloPortType;
import hello.wsdl.ServiceError;
import hello.wsdl.ServiceError_Exception;

@javax.jws.WebService (endpointInterface="hello.wsdl.HelloPortType")
public class HelloWebServiceImpl implements HelloPortType {

    public String sayHello(String name) throws ServiceError_Exception {
        try	{
            boolean throwError = ConfigUtil.recognizeAsTrue(Config.getInstance().getInitParameter("hello.ws.ThrowServiceError"));
            if(throwError)
                throw new MissingNameException("Name is missing (forced by hello.ws.ThrowServiceError property for testing)", null);
            
            if(name == null || name.trim().length() == 0)
                throw new MissingNameException("Name is missing", null);

            return "Hello " + name;

        } catch (Exception e) {
            throw new ServiceError_Exception(e.getMessage(), new ServiceError(), e);
        }

    }

}
