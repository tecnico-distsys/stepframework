package hello;

import hello.ws.stubs.*;


public class HelloServiceClient {

    public static void main(String[] args) {
        try {
            HelloPortType port = new HelloService().getHelloPort();
            String sayHelloResult = port.sayHello("friend");
            System.out.println(sayHelloResult);
        } catch(Exception e) {
            System.out.println("Caught exception " + e);
        }
    }

}
