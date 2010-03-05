======STEP Extensions/JAX-WS Handlers testbed Project======

=====Structure=====

====hello-ws====

Web service implementation of a simple Hello service.

It takes an argument and returns a greeting with the name in the argument (return "Hello " + arg).

====hello-wsclient====

Web service client implementation.

Sole purpose is to invoke remote service with the given argument.

====helloclient-web====

Web application based on a simple form with a text area for the argument.

====extensions====
		
===trace===

Prints an inbound or outbound SOAP message.

===errors===

Throws/returns exceptions/booleans as defined by the the configuration file.

===cipher===

Encrypts all outbound SOAP messages and decrypts all inbound SOAP messages.

=====Tests=====

====Cases====

  * Normal execution with cipher extension enabled

  * Web Service throws ServiceError_Exception with cipher extension enabled

  * Web Service throws ServiceError_Exception with cipher extension disabled

  * Server Errors Extension throws SOAPFaultException on inbound SOAP messages with cipher extension enabled

  * Server Errors Extension throws SOAPFaultException on inbound SOAP messages with cipher extension disabled

  * Server Errors Extension throws SOAPFaultException on outbound SOAP messages with cipher extension enabled

  * Server Errors Extension returns false on inbound SOAP messages with cipher extension enabled

  * Server Errors Extension returns false on inbound SOAP messages with cipher extension disabled

  * Server Errors Extension returns false on outbound SOAP messages with cipher extension enabled

  * Server Errors Extension returns false on outbound SOAP messages with cipher extension disabled

====Logs====

Output logs were saved on the logs folder located on the root directory of the project.

They are divided into two sub-folders: **step-0** and **step-1**.

Folder **step-0** contains the logs of the test cases executed before the modifications to the framework.

Folder **step-1** contains the logs of the test cases executed after the successful modifications to the framework.

=====Bugs=====

====Documented====

The documented bug was detected by a group of students executing the ES-SD course project.

When an encryption extension is being used, it might mask a fault in the SOAP message, and, upon reception and subsequent decryption, the fault was transformed into another type.

This was resolved by checking if the SOAP message contains a fault after each web service interceptor executes.

If a fault was uncovered by one of the interceptors, it would not throw a SOAPFaultException, forcing the program to parse the fault to its natural type.

====Undocumented====

An undocumented fault was discovered when executing the initial tests.

When an exception is thrown by an interceptor, forcing the execution to change direction, the encryption extension masks this fault, causing the jax-ws handler to ignore the hidden fault and proceeding with the normal execution of the web service, even if the WebServiceInterceptorManager already executed the outbound processing.

This causes the web service to throw an error when trying to read the encrypted message wich was supposed to be sent back to the client.

This was resolved using a boolean flag that turns **true** whenever a non-fatal exception is thrown by an interceptor.

After the entire execution, if the flag is **true**, the WebServiceInterceptorManager returns **false**, forcing the jax-ws handler to abort its execution and returning the outbound prepared SOAP message to the client.
