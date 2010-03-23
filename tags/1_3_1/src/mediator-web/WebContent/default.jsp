<%@ page contentType="text/html;charset=ISO-8859-15" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c"       uri="http://java.sun.com/jsp/jstl/core" %>

<stripes:layout-render name="/layout/Mediator.jsp">
    <stripes:layout-component name="title">Home</stripes:layout-component>
    <stripes:layout-component name="content">
        <center><h3>Mediator</h3></center>
        <center><h5> ! ! ! ! !</h5></center><br />

	<stripes:messages />

	<c:if test="${(actionBean != null) 
		    && (empty actionBean.context.validationErrors) 
		    && (empty actionBean.context.messages)}">
	    <p> Flight booked. </p>
	    <p> Reservation code: ${actionBean.result.reservationCode}</p>
	    <p> Client id: ${actionBean.result.clientId}</p>
	</c:if>


    </stripes:layout-component>
</stripes:layout-render>
