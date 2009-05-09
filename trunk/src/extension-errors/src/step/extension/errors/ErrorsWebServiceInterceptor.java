package step.extension.errors;

import java.io.IOException;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.WebServiceInterceptorException;
import step.framework.extensions.WebServiceInterceptorParameter;
import step.framework.ws.SOAPUtil;


/**
 *  This is the Errors extension's web service interceptor.
 *  If properly configured, it captures all inbound and outbound SOAP messages.
 */
public class ErrorsWebServiceInterceptor implements WebServiceInterceptor {

    public boolean interceptMessage(WebServiceInterceptorParameter param)
    throws SOAPFaultException, WebServiceInterceptorException {

        Properties extConfig = param.getExtension().getConfig();

        // when to cause the error
        final String WHEN_PROP_NAME = "web-service-interceptor.error-when";
        String errorWhen = extConfig.getProperty(WHEN_PROP_NAME);
        if(errorWhen == null) {
            System.out.println("no web service interceptor error when specified (property " +
                               WHEN_PROP_NAME + "); skipping");
            return true;
        }

        String situation = "interceptMessage-" + errorWhen.toLowerCase();

        boolean isServerSide = param.isServerSide();
        boolean isOutbound = param.isOutboundSOAPMessage();
        boolean isFault = param.isFaultSOAPMessage();

        // return
        final String RETURN_PROP_NAME = "web-service-interceptor.return";
        String returnPropertyValue = extConfig.getProperty(RETURN_PROP_NAME);

        if(returnPropertyValue != null && returnPropertyValue.equalsIgnoreCase("false")) {
            if(errorWhen.equalsIgnoreCase("client-inbound") && !isServerSide && !isOutbound) {
                System.out.println("Returning false on " + situation);
                return false;
            } else if(errorWhen.equalsIgnoreCase("client-outbound") && !isServerSide && isOutbound) {
                System.out.println("Returning false on " + situation);
                return false;
            } else if(errorWhen.equalsIgnoreCase("server-inbound") && isServerSide && !isOutbound) {
                System.out.println("Returning false on " + situation);
                return false;
            } else if(errorWhen.equalsIgnoreCase("server-outbound")  && isServerSide && isOutbound) {
                System.out.println("Returning false on " + situation);
                return false;
            }
        }

        // throw
        final String EXCEPTION_PROP_NAME = "web-service-interceptor.throw";
        String exceptionClassName = extConfig.getProperty(EXCEPTION_PROP_NAME);
        if(exceptionClassName == null || exceptionClassName.trim().length() == 0) {
            System.out.println("no web service interceptor throw exception specified (property " +
                               EXCEPTION_PROP_NAME + "); skipping");
            return true;
        }

        try {
            if(exceptionClassName.equals("javax.xml.ws.soap.SOAPFaultException")) {
                // special case: SOAPFaultException
                if(errorWhen.equalsIgnoreCase("client-inbound") && !isServerSide && !isOutbound) {
                    throwSOAPFaultException(situation, param);
                } else if(errorWhen.equalsIgnoreCase("client-outbound") && !isServerSide && isOutbound) {
                    throwSOAPFaultException(situation, param);
                } else if(errorWhen.equalsIgnoreCase("server-inbound") && isServerSide && !isOutbound) {
                    throwSOAPFaultException(situation, param);
                } else if(errorWhen.equalsIgnoreCase("server-outbound")  && isServerSide && isOutbound) {
                    throwSOAPFaultException(situation, param);
                }
            } else {
                // normal case
                if(errorWhen.equalsIgnoreCase("client-inbound") && !isServerSide && !isOutbound) {
                    ThrowExceptionUtil.throwException(exceptionClassName, situation);
                } else if(errorWhen.equalsIgnoreCase("client-outbound") && !isServerSide && isOutbound) {
                    ThrowExceptionUtil.throwException(exceptionClassName, situation);
                } else if(errorWhen.equalsIgnoreCase("server-inbound") && isServerSide && !isOutbound) {
                    ThrowExceptionUtil.throwException(exceptionClassName, situation);
                } else if(errorWhen.equalsIgnoreCase("server-outbound")  && isServerSide && isOutbound) {
                    ThrowExceptionUtil.throwException(exceptionClassName, situation);
                }
            }

        } catch(WebServiceInterceptorException wsie) {
            throw wsie;
        } catch(RuntimeException rte) {
            throw rte;
        } catch(Exception e) {
            System.out.println("exception type not declared in throws; wrapping exception in runtime exception");
            throw new RuntimeException(e);
        }

        return true;

    }

    private void throwSOAPFaultException(String message, WebServiceInterceptorParameter param)
    throws SOAPFaultException, SOAPException, IOException {

        // access envelope
        SOAPMessageContext smc = param.getSOAPMessageContext();

        // create SOAP message
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage soapMessage = mf.createMessage();

        // access SOAP body
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        SOAPBody soapBody = soapEnvelope.getBody();

        // create SOAP Fault
        SOAPFault soapFault = soapBody.addFault();

        // set message
        String faultMessage = this.getClass().getSimpleName() +
                              " generated exception in method " +
                              message;
        soapFault.setFaultString(faultMessage);

        // set code according to context
        QName faultCode = SOAPUtil.suggestFaultCode(smc);
        soapFault.setFaultCode(faultCode);

        // throw SOAP fault exception
        throw new SOAPFaultException(soapFault);
    }

}
