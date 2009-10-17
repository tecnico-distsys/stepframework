package hello.ws;

import hello.wsdl.*;

@javax.jws.WebService (endpointInterface="hello.wsdl.HelloPortType")
public class HelloWebServiceImpl implements HelloPortType {

    public String sayHello(String name) {
        return("Hello " + name + "!");
    }

}
