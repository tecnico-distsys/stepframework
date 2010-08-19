<%@ page contentType="text/html;charset=ISO-8859-15" language="java" import="java.io.PrintWriter" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

<stripes:layout-render name="/layout/Hello.jsp">
    <stripes:layout-component name="title">Unexpected exception</stripes:layout-component>
    <stripes:layout-component name="content">
	Here is one exception that is not yet well handled in the interface!

    	<font size="1">
			<pre>
<% 
	Throwable t = (Throwable)request.getAttribute("exception");
	t.printStackTrace(new PrintWriter(out));
%>
			</pre>
<hr /><center>Cause: (if available)</center>
			<pre>
<% 
	Throwable cause = t.getCause();
        if (cause != null) { cause.printStackTrace(new PrintWriter(out)); }
%>
			</pre>
		</font>
    </stripes:layout-component>
</stripes:layout-render>
