package hello;

import hello.wsdl.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.ws.BindingProvider;


public class HelloServlet extends HttpServlet {

	public HelloServlet() {

	}

    public void init(ServletConfig config) throws ServletException {
		super.init(config);
    }

    public void destroy() {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Hello World Servlet</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Hello World Servlet</h1>");
		out.println("<p>");
		out.println(new java.util.Date());
		out.println("</p>");
		out.println("<p>");
        out.println("Web Service invocation output:<br /><pre>");
        try {
             // create stub
            HelloService service = new HelloService();
            HelloPortType port = service.getHelloPort();
            BindingProvider bindingProvider = (BindingProvider) port;

            // set endpoint address
            String endpointAddress = "http://localhost:8080/ws/endpoint";
            out.println("Setting endpoint address to: " + endpointAddress);
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

            // invoke web service and print result
            out.println("Invoking Web Service...");
            String sayHelloResult = port.sayHello("friend");
            out.println("The result is: " + sayHelloResult);
        } catch(Exception e) {
            out.println("Caught exception " + e);
        }
		out.println("</pre></p>");
		out.println("</body>");
		out.println("</html>");

		out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
	    // call doGet to print the same page
		doGet(request, response);
    }

}
