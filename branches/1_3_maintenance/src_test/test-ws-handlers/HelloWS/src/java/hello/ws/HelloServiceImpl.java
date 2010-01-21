package hello.ws;

import hello.ws.ties.*;

@javax.jws.WebService (endpointInterface="hello.ws.ties.HelloPortType")
public class HelloServiceImpl implements HelloPortType {

    public String sayHello(String name)
        throws ServiceError, NameMissing {

        try {
            if(name == null || name.trim().length() == 0)
                throw new NameMissing("Name is missing", null);

            return "Hello " + name;

        } catch(NameMissing e) {
            // catch and rethrow
            throw e;

        } catch(Exception e) {
            // log exception information
            System.out.println("Caught exception: ");
            System.out.println(e.getClass().toString());
            System.out.println(e.getMessage());

            throw new ServiceError("Service unavailable, please try again later.",
                                   null,
                                   e);
        }

    }

}
