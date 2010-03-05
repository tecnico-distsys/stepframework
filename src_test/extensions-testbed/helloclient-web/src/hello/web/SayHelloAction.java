package hello.web;

import hello.ws.client.service.SayHelloService;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

public class SayHelloAction extends HelloAction
{
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
			setMessage(e.getMessage());
		}

		return new ForwardResolution("/default.jsp");
	}


    /* For console testing */
    public static void main(String[] args) throws Exception {
        SayHelloAction action = new SayHelloAction();
        action.setName(args[0]);
        action.sayHello();
        System.out.println(action.getMessage());
    }
    
}
