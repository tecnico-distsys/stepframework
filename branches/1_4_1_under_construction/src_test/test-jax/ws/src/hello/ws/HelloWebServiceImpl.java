package hello.ws;

import hello.wsdl.*;

@javax.jws.WebService (endpointInterface="hello.wsdl.HelloPortType")
public class HelloWebServiceImpl implements HelloPortType {

    public String sayHello(String name) throws EmptyNameFault{
        if(name == null || name.trim().length() == 0) {
            throw new EmptyNameFault("Name parameter is empty", new EmptyName());
        }
        return("Hello " + name + "!");
    }

}
