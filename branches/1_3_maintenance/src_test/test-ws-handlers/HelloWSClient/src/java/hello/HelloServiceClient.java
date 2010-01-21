package hello;

import hello.ws.stubs.*;


public class HelloServiceClient {

    public static void main(String[] args) throws Exception {
        HelloPortType port = new HelloService().getHelloPort();
        String sayHelloResult = port.sayHello("friend");
        System.out.println(sayHelloResult);
    }

}
