package hello.ws;

import hello.exception.ws.HelloWSException;
import hello.exception.ws.MissingNameException;

import hello.wsdl.HelloPortType;
import hello.wsdl.ServiceError;
import hello.wsdl.ServiceError_Exception;

@javax.jws.WebService (endpointInterface="hello.wsdl.HelloPortType")
public class HelloWebServiceImpl implements HelloPortType {

    public String sayHello(String name) throws ServiceError_Exception {
        try	{
            if(name == null || name.trim().length() == 0)
                throw new MissingNameException("Name is missing", null);

            return "Hello " + name;

        } catch (Exception e) {
            throw new ServiceError_Exception(e.getMessage(), new ServiceError(), e);
        }

    }

}
