****************************************
*** Java APIs for XML (JAX) Examples ***
****************************************

These examples demonstrate the essential features of the Java APIs for XML,
namely:
- Java API for XML Data-Binding (JAX-B) 2.0
- Java API for XML Web Services (JAX-WS) 2.1

Because JAX-WS can be deployed as a web application, a Servlet-JSP example
is also included.


1. Test environment

The examples were tested on the following environment:
- Windows Vista Service Pack 2
- Java Developer Kit (JDK) 1.6.0_16
- Java Runtime Environment (JRE) 6 update 16
- Apache Ant 1.7.1
- Apache Tomcat 6.0.20

JAX-B and JAX-WS are part of the JDK and JRE:
- JAX-B 2.1.10
- JAX-WS 2.1.6

The Apache Ant tasks xjc and wsimport used the libraries included in
JAX-WS 2.1.7 because I couldn't find the libraries for version 2.1.6.

https://jaxb.dev.java.net/
https://jax-ws.dev.java.net/

The lib folder contains the minimum subset of JAX-WS libraries,
and Apache Ant shared files.


2. Installation summary

2.1 Install JDK and JRE
2.2 Unzip Apache Ant
2.3 Unzip Apache Tomcat
2.4 Define environment variables: JAVA_HOME, ANT_HOME, CATALINA_HOME
e.g. C:\Java\jdk\1.6.0_16, C:\Java\apache-ant\1.7.1, C:\Java\apache-tomcat\6.0.20
2.5 Extend PATH environment variable:
%JAVA_HOME%\bin;%ANT_HOME%\bin;%CATALINA_HOME%\bin;
2.6 Add a manager to %CATALINA_HOME%\conf\tomcat-users.xml:
  <role rolename="manager"/>
  <user username="admin" password="adminadmin" roles="manager"/>
If you use a different user or password, you will have to edit build.properties
accordingly.


3. Examples

Each example has a build.xml file that defines how to construct and
run the program. To list the main targets:
$ ant -p


3.1 web

This example is a very simple Java Web Application containing
a simple Servlet and a simple JSP.
See web.xml for web application configuration,
including servlet declaration and mapping.

To build:
$ ant build

To deploy:
$ catalina start
$ ant deploy

To test:
http://localhost:8080/web/


3.2 b

This example demonstrates Java code generation from XML Schema using JAX-B.
After the code is generated, JAX-B supports marshal (xml to java data conversion)
and unmarshal (java to xml data conversion).

The generated code is saved in the build folder for reference.

To build:
$ ant build

To run:
$ ant run

To know about other run targets:
$ ant -p


3.3 ws

This example demonstrates a simple JAX-WS web service,
including SOAP message interceptors (called handlers).

The Web Service's interface is specified in the WSDL file.
The WSDL file sections are described in numbered comments.
There is a single operation, sayHello, that
receives a name string and answers Hello!

The Web Service's implementation is specified in a Java class:
hello.ws.HelloServiceImpl.

Configuration files contain settings that depend of the WSDL and Java class.
(see sun-jaxws.xml and custom binding files, for starters)

The generated code is saved in the build folder for reference.

To build:
$ ant build

To deploy:
$ catalina start
$ ant deploy

To confirm deployment:
http://localhost:8080/ws/endpoint

Errors are reported in Tomcat's output.


3.4 - ws-cli

This is a Web Service client. In this case, it is a console program that
invokes the Web Service once.

Configuration files contain settings that depend of the WSDL and Java class.
(see custom binding file for handler configuration)

The generated code is saved in the build folder for reference.

To build:
$ ant build

To run:
$ ant run

The expected output is "Hello friend!" along with the inbound and outbound
SOAP messages. The messages are printed by the handler classes
defined in the custom binding files (one for the client, one for the server).


4. Acknowledgements

The JAX-WS samples were an important reference.



--
Miguel Pardal (miguel.pardal@ist.utl.pt)
2009-10-16
