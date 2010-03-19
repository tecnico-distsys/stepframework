package hello.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import hello.ws.client.service.SayHelloService;


public class SayHelloAction extends HelloAction {

    /** logging */
    private static Log log = LogFactory.getLog(SayHelloAction.class);

    /* members */
	private String name;
	private String message;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@DefaultHandler
	public Resolution sayHello() {
		try {
			SayHelloService	svc = new SayHelloService(name);
			String greeting = svc.execute();

			setMessage(greeting);
		} catch(Exception e) {
            log.trace("sayHello caught exception");
            log.error(e);
			setMessage(e.getMessage());
		}

		return new ForwardResolution("/default.jsp");
	}


    /* For console testing */
    public static void main(String[] args) throws Exception {
        try {
            log.trace("Starting main");
            log.trace(args);

            SayHelloAction action = new SayHelloAction();
            action.setName(args[0]);

            log.trace("Invoking sayHello()");
            action.sayHello();

            log.trace("Action message: " + action.getMessage());
            System.out.println(action.getMessage());

        } catch(Throwable t) {
            log.trace("Caught throwable");
            log.error(t);

        } finally {
            log.trace("Finally exiting main");
        }
    }
    
}
