CalcWS client

Client of CalcWS.
If differs from HelloWS client because it shows how the Web Service address can be specified at run-time and how to properly handle exceptions.

Interest points:
- build.xml - web service client definitions
- calc.CalcServiceClient - Java implementation of the Web Service client - see how the fault was mapped to a Java Exception

After the first build:
- check build/jax-ws-client directory to look at generated code
- look again into build.xml; a JAX-WS handler section can be uncommented to enable a client-side SOAP message printer

--
Miguel Pardal
2008-03-11
