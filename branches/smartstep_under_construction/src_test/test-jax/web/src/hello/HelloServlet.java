package hello;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
