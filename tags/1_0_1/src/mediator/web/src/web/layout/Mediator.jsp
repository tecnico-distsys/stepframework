<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

<stripes:layout-definition>
    <html>
        <head>
            <title>Mediator [${title}]</title>
            <style>
                body, p, td, table, tr {
                    font-family: Verdana, arial, sans-serif;
                    font-size: 12px;
                    line-height: 16px;
                    color: #023264;
                    font-weight: normal;
                }

                body {
                    margin: 0px;
                    padding-bottom: 0px;
                    padding-left: 0px;
                    padding-top: 0px;
                    padding-right: 0px;
                }
                
                hr {
                    border: none;
                    background-color: #023264;
                    height: 1px;
                    width: 60%;
                    margin-top: 10px;
                    margin-bottom: 10px;
                }
                
                td.border {
                    background-color: #B1BBD6;
                }

                td.menu {
                    width: 150;
                    padding: 0 5 0 5;
                }
                
                td.header {
                    height: 50;
                }

                td.body {
                    padding: 25 25 25 25
                }
                
                table.layout {
                    width: 100%;
                    height: 100%;
                    border-spacing: 0;
                    padding: 0;
                }
                
                table.list {
                    width: 85%;
                    border-spacing: 0;
                }
                
                td.list {
                    vertical-align: text-top
                }
                
                tr.blue {
                    background-color: #E4E8F1;
                }
                
                tr.green {
                    background-color: #DDFFDD;
                }

                tr.red {
                    background-color: #FFCCCC;
                }
                
                .footer {
                    font-size: smaller;
                    vertical-align: bottom;
                }
                
                .errorSection {
                    border-style: solid;
                    border-width: 1px;
                    border-color: #F0C000;
                    background-color: #FFFFCE;
                    margin-top: 5;
                    margin-bottom: 5;
                    padding: 10 20 10 20;
                    text-align:left; 
                    width: 85%;
                }
                
                .errorMessage, .errorItem {
                    color: #b72222;
                }

                .errorMessage {
                    font-weight: bold;
                }
            </style>
	    </head>
	    <body>
            <table class="layout">
                <tr>
                    <td class="border menu" rowspan="2" valign="top">
                        <center>
                            <a href="http://www.ist.utl.pt">
                                <img src="${pageContext.request.contextPath}/layout/images/logo.gif" width="95" height="198" alt="Instituto Superior Técnico" border="0"/>
                            </a>
                        </center>
                        <br/>
                        <stripes:layout-component name="menu">
                            <jsp:include page="/menu.jsp"/>
                        </stripes:layout-component>                        
                    </td>
                    <td class="border header">
                        <stripes:layout-component name="header">
                            <jsp:include page="/layout/header.jsp"/>
                        </stripes:layout-component>
                    </td>
                    <td class="border header" align="center" valign="middle" width="120">
                        <stripes:layout-component name="login" />
                    </td>
                </tr>
                <tr>
                    <td class="body" valign="top" colspan="2">
                        <stripes:layout-component name="body">
                            <center>
                                <a href="http://www.ist.utl.pt">
                                    <img src="${pageContext.request.contextPath}/layout/images/banner.gif" alt="Instituto Superior Técnico" border="0"/>
                                </a>
                            </center>
                            <br/>
                            <stripes:layout-component name="errors">
                                <jsp:include page="/layout/errors.jsp"/>
                            </stripes:layout-component>
                            <stripes:layout-component name="content" />
                        </stripes:layout-component>                        
                    </td>
                </tr>
                <tr>
                    <td class="border menu" valign="bottom">
                        <stripes:layout-component name="footer">
                            <jsp:include page="/layout/footer.jsp"/>
                        </stripes:layout-component>
                    </td>
                </tr>
            </table>
	    </body>
	</html>
</stripes:layout-definition>