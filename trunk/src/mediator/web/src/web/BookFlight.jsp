<%@ page contentType="text/html;charset=ISO-8859-15" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c"       uri="http://java.sun.com/jsp/jstl/core" %>

<stripes:layout-render name="/layout/Mediator.jsp">
    <stripes:layout-component name="title">Book Flight</stripes:layout-component>
    <stripes:layout-component name="content">
        <h3>Book a flight</h3>
        <stripes:form action="/Booking.action" focus="">
            <table>
                <tr>
                    <td>From</td>
                    <td><stripes:text name="origin"/></td>
                    <td><stripes:errors field="origin"/></td>
                </tr>
                <tr>
                    <td>To</td>
                    <td><stripes:text name="destination"/></td>
                    <td><stripes:errors field="destination"/></td>
                </tr>
                <tr>
                    <td>Name</td>
                    <td><stripes:text name="name"/></td>
                    <td><stripes:errors field="name"/></td>
                </tr>
                <tr>
                    <td>Identification</td>
                    <td><stripes:text name="id"/></td>
                    <td><stripes:errors field="id"/></td>
                </tr>
                
                <tr colspan="3">
                    <td><stripes:submit name="flight" value="Submit"/></td>
                </tr>
            </table>
        </stripes:form>
    </stripes:layout-component>
</stripes:layout-render>
