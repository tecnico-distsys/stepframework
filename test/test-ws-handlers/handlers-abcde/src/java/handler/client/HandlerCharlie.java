package handler.client;

import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.soap.*;
import javax.xml.ws.handler.soap.SOAPMessageContext;



/*
 * This basic JAX-WS handler prints a message for each
 * Handler interface method.
 * It also prints if the message is inbound or outbound.
 */
public class HandlerCharlie implements SOAPHandler<SOAPMessageContext> {

    public HandlerCharlie() {
        System.out.println(this + "> (constructor)");
    }

    public Set<QName> getHeaders() {
        System.out.println(this + "> getHeaders()");
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        System.out.print(this + "> handleMessage()");

        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty.booleanValue()) {
            System.out.println(" - Outbound SOAP message");

            if(false) {
                System.out.println(this + " throwing runtime exception");
                throw new RuntimeException("Client Outbound - Runtime Exception!");
            }
            if(false) {
                System.out.println(this + " throwing soap fault exception");
                throw new SOAPFaultException(handler.CreateSOAPFaultUtil.createFault("Client Outbound - SOAP Fault!"));
            }
            if(false) {
                System.out.println(this + " returning false");
                return false;
            } else {
                return true;
            }

        } else {
            System.out.println(" - Inbound SOAP message");

            if(false) {
                System.out.println(this + " throwing runtime exception");
                throw new RuntimeException("Client Inbound - Runtime Exception!");
            }
            if(false) {
                System.out.println(this + " throwing soap fault exception");
                throw new SOAPFaultException(handler.CreateSOAPFaultUtil.createFault("Client Inbound - SOAP Fault!"));
            }
            if(false) {
                System.out.println(this + " returning false");
                return false;
            } else {
                return true;
            }

        }

    }

    public boolean handleFault(SOAPMessageContext smc) {
        System.out.print(this + "> handleFault()");

        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty.booleanValue()) {
            System.out.println(" - Outbound SOAP message");
        } else {
            System.out.println(" - Inbound SOAP message");
        }

        return true;
    }

    public void close(MessageContext messageContext) {
        System.out.println(this + "> close()");
        return;
    }

}
