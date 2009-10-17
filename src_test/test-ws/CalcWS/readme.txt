CalcWS

This is another simple example of a Web Service written in Java.
However, it is bigger than HelloWS (four operations instead of just on) and demonstrates a fault and prints the SOAP messages to the server log.

As all Web Services, it is specified in the WSDL file.
There are four operations: sum, sub, mult, intdiv.

Interest points:
- WSDL file - sections are explained in sequenced comments #... - see fault definition
- calc.ws.CalcServiceImpl - Java implementation of the Web Service - see how the fault was mapped to a Java Exception
- build.xml - web service definitions - including a JAX-WS handler that will output the inbound and outbound messages to the server log

After build:
- check build/jax-ws-server directory to look at generated code

--
Miguel Pardal
2008-03-11
