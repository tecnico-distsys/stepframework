package hello;

import javax.xml.ws.BindingProvider;

import hello.wsdl.*;
import java.util.*;

public class HelloWebServiceClient {

    public static void main(String[] args) {

        System.out.println("Web Service client starting...");

        System.out.println("Creating stub and configuring it...");

        // create stub
        HelloService service = new HelloService();
        HelloPortType port = service.getHelloPort();
        BindingProvider bindingProvider = (BindingProvider) port;

        // set endpoint address
        String endpointAddress = "http://localhost:8080/ws/endpoint";
        System.out.println("Setting endpoint address to: " + endpointAddress);
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

        // invoke web service and print result
        System.out.println("Invoking Web Service...");
        String sayHelloResult = port.sayHello("friend");
        System.out.println("The result is: " + sayHelloResult);

    }

}
