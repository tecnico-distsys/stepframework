package calc;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import step.framework.config.*;
import step.framework.ws.registry.*;

import calc.ws.stubs.*;


public class CalcServiceClient {

    public static void main(String[] args) {
        try {
            System.out.println("begin");

            //
            // check and read command line arguments
            //
            if(args.length < 4) {
                System.out.println("expected parameters: endpointURL (sum|sub) opArg1 opArg2");
                return;
            }

            // web service endpoint address
            String endpointURL = args[0];

            // operation
            String operation = args[1];

            // arguments
            String opArg1String = args[2];
            String opArg2String = args[3];
            int opArg1 = Integer.parseInt(opArg1String);
            int opArg2 = Integer.parseInt(opArg2String);

            System.out.println("Web Service endpoint URL: " + endpointURL);
            System.out.println(operation + " " + opArg1String + " " + opArg2String);

            //
            // create Web Service stub by direct instantiation
            //
            {
              System.out.println("Using direct stub creation...");

              CalcService service = new CalcService();
              CalcPortType port = service.getCalcPort();
              BindingProvider bindingProvider = (BindingProvider) port;

              // set endpoint address
              bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

              //
              // invoke Web Service operation
              //
              int result;
              if(operation.equalsIgnoreCase("sum")) {
                  result = port.sum(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("sub")) {
                  result = port.sub(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("mult")) {
                  result = port.mult(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("intdiv")) {
                  result = port.intdiv(opArg1, opArg2);
              } else {
                  System.out.println("operation " + operation + " not recognized!");
                  return;
              }

              System.out.println("= " + result);
          }


            //
            // create Web Service stub using factory
            //
            {
              System.out.println("Using factory stub creation...");

              //CalcService service = new CalcStubFactory().getService();
              //CalcPortType port = service.getCalcPort();
              CalcPortType port = new CalcStubFactory().getPort();

              BindingProvider bindingProvider = (BindingProvider) port;

              // set endpoint address
              bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);


              //
              // invoke Web Service operation
              //
              int result;
              if(operation.equalsIgnoreCase("sum")) {
                  result = port.sum(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("sub")) {
                  result = port.sub(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("mult")) {
                  result = port.mult(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("intdiv")) {
                  result = port.intdiv(opArg1, opArg2);
              } else {
                  System.out.println("operation " + operation + " not recognized!");
                  return;
              }

              System.out.println("= " + result);
          }

            //
            // create Web Service stub using factory and specifying URL
            //
            {
              System.out.println("Using factory stub creation with custom-URL...");

              //CalcService service = new CalcStubFactory().getService();
              //CalcPortType port = service.getCalcPort();
              //BindingProvider bindingProvider = (BindingProvider) port;
              //bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
              CalcPortType port = new CalcStubFactory().getPort(endpointURL);

              //
              // invoke Web Service operation
              //
              int result;
              if(operation.equalsIgnoreCase("sum")) {
                  result = port.sum(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("sub")) {
                  result = port.sub(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("mult")) {
                  result = port.mult(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("intdiv")) {
                  result = port.intdiv(opArg1, opArg2);
              } else {
                  System.out.println("operation " + operation + " not recognized!");
                  return;
              }

              System.out.println("= " + result);
          }

            //
            // create Web Service stub using factory and configuration
            //
            {
              System.out.println("Using factory stub creation with configuration-specified URL...");

              //CalcService service = new CalcStubFactory().getService();
              //CalcPortType port = service.getCalcPort();
              //BindingProvider bindingProvider = (BindingProvider) port;
              //bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
              CalcPortType port = new CalcStubFactory().getPortUsingConfig();

              //
              // invoke Web Service operation
              //
              int result;
              if(operation.equalsIgnoreCase("sum")) {
                  result = port.sum(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("sub")) {
                  result = port.sub(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("mult")) {
                  result = port.mult(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("intdiv")) {
                  result = port.intdiv(opArg1, opArg2);
              } else {
                  System.out.println("operation " + operation + " not recognized!");
                  return;
              }

              System.out.println("= " + result);
          }

            //
            // create Web Service stub using factory and registry
            //
            {
              System.out.println("Using factory stub creation with registry...");

              //CalcService service = new CalcStubFactory().getService();
              //CalcPortType port = service.getCalcPort();
              //BindingProvider bindingProvider = (BindingProvider) port;
              //bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
              CalcPortType port = new CalcStubFactory().getPortUsingRegistry();

              //
              // invoke Web Service operation
              //
              int result;
              if(operation.equalsIgnoreCase("sum")) {
                  result = port.sum(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("sub")) {
                  result = port.sub(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("mult")) {
                  result = port.mult(opArg1, opArg2);
              } else if(operation.equalsIgnoreCase("intdiv")) {
                  result = port.intdiv(opArg1, opArg2);
              } else {
                  System.out.println("operation " + operation + " not recognized!");
                  return;
              }

              System.out.println("= " + result);
          }

        } catch(WebServiceException  e) {
            // handle Web Service exception
            System.out.println("Caught web service exception: ");
            System.out.println(e.getClass().toString());
            System.out.println(e.getMessage());

        } catch(Exception  e) {
            // handle general exception
            System.out.println("Caught exception: ");
            System.out.println(e.getClass().toString());
            System.out.println(e.getMessage());

        } finally {
            System.out.println("end");
        }
    }

}
