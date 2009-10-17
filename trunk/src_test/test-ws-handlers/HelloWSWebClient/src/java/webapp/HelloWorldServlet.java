package webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HelloWorldServlet extends HttpServlet {

	public HelloWorldServlet() {

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

		out.println("<p>Invoking web service...</h1>");

        hello.HelloServiceClient.main(new String[0]);

		out.println("<p>Done! Check server log for output</h1>");

		out.println("</body>");
		out.println("</html>");

		out.close();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

		doGet(request, response);

    }

}
