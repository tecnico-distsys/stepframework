<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c"       uri="http://java.sun.com/jsp/jstl/core" %>

<stripes:layout-definition>
    <html>
        <head>
            <title>Hello - ${title}</title>            
            <style type="text/css">
		@import "./layout/style.css";
	    </style>
	</head>
	<body>
            <table class="layout">
                <tr>
                    <jsp:include page="/layout/header.jsp"/>
                </tr>
                <tr>
                    <td class="body" valign="top" colspan="3">
                        <stripes:layout-component name="body">
                            <stripes:layout-component name="errors">
                                <jsp:include page="/layout/errors.jsp"/>
                            </stripes:layout-component>
                            <stripes:layout-component name="content" />
                        </stripes:layout-component>                        
                    </td>
                </tr>
            </table>
	</body>
    </html>
</stripes:layout-definition>