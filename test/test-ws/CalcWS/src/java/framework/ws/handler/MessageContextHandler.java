package framework.ws.handler;


import java.io.PrintStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/*
 * This simple SOAPHandler will output the contents of the message handler in
 * incoming and outgoing messages.
 */
public class MessageContextHandler implements SOAPHandler<SOAPMessageContext> {
    
    // change this to redirect output if desired
    private static PrintStream out = System.out;
    
    public Set<QName> getHeaders() {
        return null;
    }
    
    public boolean handleMessage(SOAPMessageContext smc) {
        printMessageContext(smc);
        return true;
    }
    
    public boolean handleFault(SOAPMessageContext smc) {
        printMessageContext(smc);
        return true;
    }
    
    // nothing to clean up
    public void close(MessageContext messageContext) {
    }
    
    private void printMessageContext(MessageContext map) {
        out.println("Message context: (scope,key,value)");
        try {
            java.util.Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                Object value = map.get(key);
                
                String keyString;
                if (key == null)
                    keyString = "null";
                else
                    keyString = key.toString();

                String valueString;
                if (value == null)
                    valueString = "null";
                else
                    valueString = value.toString();

                Object scope = map.getScope(keyString);
                String scopeString;
                if (scope == null)
                    scopeString = "null";
                else
                    scopeString = scope.toString();
                scopeString = scopeString.toLowerCase();
                
                System.out.println("(" + scopeString + "," +
                                   keyString + "," +
                                   valueString + ")");
            }
        } catch(Exception e) {
            out.println("Exception while printing message context: " + e);   
        }
    }

    
}
