<%@ page contentType="text/html;charset=ISO-8859-15" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c"       uri="http://java.sun.com/jsp/jstl/core" %>

<stripes:layout-render name="/layout/Hello.jsp">
    <stripes:layout-component name="title">SayHello</stripes:layout-component>
    <stripes:layout-component name="content">

	<c:if test="${actionBean != null && actionBean.message != null}">
	        <h3>Reply</h3>
		<c:out value="${actionBean.message}" />
	</c:if>
	
	<h3>SayHello</h3>
        <stripes:form action="/SayHello.action" focus="">
           <table>
               <tr>
                   <td>Name</td>
                   <td><stripes:text name="name" value=""/></td>
               </tr>
               <tr colspan="2">
                   <td><stripes:submit name="sayHello" value="SayHello"/></td>
               </tr>
           </table>
       </stripes:form>

    </stripes:layout-component>
</stripes:layout-render>
