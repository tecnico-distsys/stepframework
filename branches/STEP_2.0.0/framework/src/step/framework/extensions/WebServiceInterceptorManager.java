package step.framework.extensions;

import java.util.List;
import java.util.ListIterator;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.config.tree.ConfigTree;
import step.framework.ws.SOAPUtil;


/**
 *  An instance of this class is responsible for
 *  interceping web service messages (inbound and outbound)
 *  invoking all configured extension web service interceptors.
 */
public class WebServiceInterceptorManager {

    //
    //  Members
    //
    /** Extension engine */
    ExtensionEngine engine;

    /** logging */
    private Log log = LogFactory.getLog(WebServiceInterceptorManager.class);


    //
    // Constructors
    //
    public WebServiceInterceptorManager() {
        engine = ExtensionEngine.getInstance();
    }


    //
    //  Methods called by the Framework's JAX-WS Handler
    //

    /**
     *  Intercept Web Service handler normal message
     */
    public boolean interceptHandleMessageWebServiceHandler(SOAPMessageContext smc) {
        try {
            log.trace("interceptHandleMessageWebService()");
            InterceptConfigData configData = prepareForIntercept(smc);
            if(configData != null) {
                return interceptHandleMessage(smc, configData);
            } else {
                // the handler's handleMessage return controls if
                // message processing should continue (true) or cease (false)
                return true;
            }
        } finally {
            log.trace("finally exiting interceptHandleMessageWebService()");
        }
    }

    /**
     *  Intercept Web Service handler fault message
     */
    public boolean interceptHandleFaultWebServiceHandler(SOAPMessageContext smc) {
        try {
            log.trace("interceptHandleFaultWebService()");
            InterceptConfigData configData = prepareForIntercept(smc);
            if(configData != null) {
                return interceptHandleFault(smc, configData);
            } else {
                // the handler's handleMessage return controls if
                // message processing should continue (true) or cease (false)
                return true;
            }
        } finally {
            log.trace("finally exiting interceptHandleFaultWebService()");
        }
    }

    /**
     *  Intercept Web Service handler close
     */
    public void interceptCloseWebServiceHandler(MessageContext mc) {
        try {
            log.trace("interceptCloseWebServiceHandler()");

            // do nothing

        } finally {
            log.trace("finally exiting interceptCloseWebServiceHandler()");
        }
    }


    //
    //  Web Service configuration path methods
    //

    // Helper method to build a Web Service interception configuration path
    // from a SOAP message context
    //
    // example SOAP message context contents:
    //  (handler,javax.xml.ws.wsdl.service,{http://calc}CalcService)
    //  (handler,javax.xml.ws.wsdl.port,{http://calc}CalcPort)
    //  (handler,javax.xml.ws.wsdl.operation,{http://calc}sum)
    //
    //  In practice, wsdl.operation is not available in server-side inbound
    //  messages, so it's not used.
    private String buildWebServiceConfigPath(SOAPMessageContext smc) {
        ExtensionsUtil.throwIllegalArgIfNull(smc,
                                             "soap message context cannot be null");

        // read SOAP message name properties
        QName qualifiedServiceName = (QName) smc.get(MessageContext.WSDL_SERVICE);
        QName qualifiedPortName = (QName) smc.get(MessageContext.WSDL_PORT);

        ExtensionsUtil.throwIllegalStateIfNull(qualifiedServiceName,
                                               "could not find qualified service name in soap message context");
        ExtensionsUtil.throwIllegalStateIfNull(qualifiedPortName,
                                               "could not find qualified port name in soap message context");

        String qualifiedService = qualifiedServiceName.toString();
        String qualifiedPort = qualifiedPortName.toString();

        if(log.isTraceEnabled()) {
            log.trace("SOAP message names follow");
            log.trace(MessageContext.WSDL_SERVICE + ": '" + qualifiedService + "'");
            log.trace(MessageContext.WSDL_PORT + ": '" + qualifiedPort + "'");
        }

        // extract namespace and service name
        String[] dequalifiedService = dequalifyName(qualifiedService);
        String[] dequalifiedPort = dequalifyName(qualifiedPort);

        String namespace = dequalifiedService[DEQUALIFY_NAMESPACE_IDX];
        String service = dequalifiedService[DEQUALIFY_LOCAL_NAME_IDX];
        String port = dequalifiedPort[DEQUALIFY_LOCAL_NAME_IDX];

        // warn if the namespaces in port and operation are different from
        // the namespace in service (the one that is being used)
        if(log.isWarnEnabled()) {
            String portNamespace = dequalifiedPort[DEQUALIFY_NAMESPACE_IDX];
            if(!(namespace.equals(portNamespace))) {
                log.warn("The port namespace is different from the service namespace.");
                log.warn("service ns: '" + namespace + "', port ns: '" + portNamespace + "'");
                log.warn("Extension engine is assuming they are the same for configuration purposes.");
            }
        }

        // create config path
        String configPath = WebServiceConfigPathParser.composeConfigPath(namespace,
                                                                         service,
                                                                         port);
        if(log.isTraceEnabled()) {
            log.trace("Web Service config path source items");
            log.trace("namespace: '" + namespace + "'");
            log.trace("service: '" + service + "'");
            log.trace("port: '" + port + "'");
        }

        return configPath;
    }


    // Helper method to extract namespace and local name from a qualified name
    // String[0] - namespace
    // String[1] - local name
    private final int DEQUALIFY_RESULT_ARRAY_SIZE = 2;

    private final int DEQUALIFY_NAMESPACE_IDX = 0;
    private final int DEQUALIFY_LOCAL_NAME_IDX = 1;

    private String[] dequalifyName(String qualifiedName) {
        final String BEGIN_NS = "{";
        final String END_NS = "}";
        final int idxBegin = qualifiedName.indexOf(BEGIN_NS);
        final int idxEnd = qualifiedName.indexOf(END_NS);
        if(idxBegin == -1 || idxEnd == -1 || idxBegin > idxEnd) {
            throw new IllegalArgumentException("expecting web service name with namespace " +
                                               " delimited by " + BEGIN_NS +
                                               " and " + END_NS);
        }

        String[] result = new String[DEQUALIFY_RESULT_ARRAY_SIZE];
        result[DEQUALIFY_NAMESPACE_IDX] = qualifiedName.substring(idxBegin+1, idxEnd);
        result[DEQUALIFY_LOCAL_NAME_IDX] = qualifiedName.substring(idxEnd+1, qualifiedName.length());
        return result;
    }


    //
    //  Web Service interception methods
    //

    // Helper method to prepare for interception
    // Returns appropriate intercept config data or null if invocation isn't necessary
    private InterceptConfigData prepareForIntercept(SOAPMessageContext smc) {

        // check argument
        ExtensionsUtil.throwIllegalArgIfNull(smc,
                                             "SOAP message context cannot be null");

        // check if extension engine is enabled
        if(!this.engine.isEnabled()) {
            log.trace("extensions disabled; ignoring web service execution");
            return null;
        }

        // extension engine enabled; proceed

        // use SOAP message context to extract Web Service config path
        String configPath = buildWebServiceConfigPath(smc);
        if(log.isTraceEnabled()) {
            log.trace("Extracted config path: '" + configPath + "'");
        }

        // get config tree
        ConfigTree<InterceptConfigData> configTree = engine.getInterceptWebServiceConfigTree();
        if(log.isTraceEnabled()) {
            log.trace("Using configuration tree: " + configTree.getName());
        }

        // find most specific config
        InterceptConfigData configData = configTree.findMostSpecificConfig(configPath);

        // return if no service interception config found
        if(configData == null) {
            log.trace("no web service interception configuration found; ignoring web service execution");
            return null;
        }

        // intercept config found; proceed

        // log detailed configuration data
        if(log.isTraceEnabled()) {
            log.trace("Web Service interception config data: " + configData);
        }

        // return
        return configData;
    }


    // Helper method to handle normal messages that have to be intercepted
    private boolean interceptHandleMessage(SOAPMessageContext smc, InterceptConfigData configData) {
        // log service being intercepted
        if(log.isDebugEnabled()) {
            log.debug("Intercepting web service " +
                      buildWebServiceConfigPath(smc));
        }
        // intercept!
        return interceptMessage(smc, configData);
    }

    // Helper method to handle fault messages that have to be intercepted
    private boolean interceptHandleFault(SOAPMessageContext smc, InterceptConfigData configData) {
        // fault messages are processed the same way as normal messages
        return interceptHandleMessage(smc, configData);
    } // interceptHandleFault


    // Helper method to handle messages that have to be intercepted
    private boolean interceptMessage(SOAPMessageContext smc, InterceptConfigData configData) {
        // check argument
        ExtensionsUtil.throwIllegalArgIfNull(smc,
                                             "SOAP message context cannot be null");
        try {
            // intercept message return value
            boolean interceptMessageReturnValue = true;

            // inspect SOAP message context
            // to find out in which situation is the message being intercepted
            final boolean IS_SERVER_SIDE = SOAPUtil.isServerSideMessage(smc);
            final boolean WAS_OUTBOUND = SOAPUtil.isOutboundMessage(smc);
            final boolean WAS_FAULT = SOAPUtil.isFaultMessage(smc);

            // current message status
            boolean isOutbound = WAS_OUTBOUND;
            boolean isFault = WAS_FAULT;

            if(log.isDebugEnabled()) {
                log.debug("SOAP Message situation: " +
                          (IS_SERVER_SIDE ? "server-side" : "client-side") + ", " +
                          (isOutbound ? "outbound" : "inbound") + ", " +
                          (isFault ? "fault" : "non-fault"));
            }


            // interception loop
            List<Extension> extList = configData.getExtensionList();

            // Message forced back to client flag. A forced message cannot change direction.
            boolean isForcedBackToClient = false;
            // A message can be forced by a return false,
            // or by a thrown WebServiceInterceptorException,
            // or by a thrown SOAPFaultException.

            // create iterator in position according to message direction
            ListIterator<Extension> extListIterator =
                directionAwareInitListIterator(extList, isOutbound);

            // loop according to message direction
            while(directionAwareHasNext(extListIterator, isOutbound)) {

                // get next extension according to message direction
                Extension ext = directionAwareNext(extListIterator, isOutbound);

                // skip disabled extensions
                if(!ext.isEnabled()) {
                    if(log.isDebugEnabled()) {
                        log.debug("Skipping " + ext.getId() + " extension because it's disabled");
                    }
                    continue;
                }

                //
                // invoke extension
                //
                try {
                    if(log.isDebugEnabled()) {
                        log.debug("Invoking " + ext.getId() + " extension");
                    }

                    log.trace("create web service interceptor instance");
                    WebServiceInterceptor interceptor = ext.createWebServiceInterceptorInstance();

                    log.trace("create web service interceptor parameter instance");
                    WebServiceInterceptorParameter param =
                        new WebServiceInterceptorParameter(this.engine, ext, smc);

                    log.trace("call web service interceptor");
                    boolean interceptorReturnValue = interceptor.interceptMessage(param);
                    if(log.isTraceEnabled()) {
                        log.trace("interceptor return value: '" + interceptorReturnValue + "'");
                    }

                    // process interceptor false return value in exception handler
                    if(interceptorReturnValue == false)
                        throw new ReturnFalseException();


                } catch(Exception e) {

                    if(e instanceof ReturnFalseException ||
                       e instanceof SOAPFaultException ||
                       e instanceof WebServiceInterceptorException) {

                        if(e instanceof ReturnFalseException) {
                            log.trace("web service interceptor returned false");
                            log.trace("will return false in the end of loop");
                            interceptMessageReturnValue = false;

                        } else if(e instanceof SOAPFaultException) {
                            log.debug("web service interceptor has thrown a SOAP fault exception");
                            log.debug(e);
                            log.trace("soap fault exception details", e);
                            log.debug("replacing existing message's body with SOAP Fault contained in exception");
                            SOAPUtil.replaceBodyWithFault(smc, ((SOAPFaultException)e).getFault());

                        } else if(e instanceof WebServiceInterceptorException) {
                            log.debug("web service interceptor has thrown a web service interceptor exception");
                            log.debug(e);
                            log.trace("web service interceptor exception details", e);
                            log.debug("replacing existing message's body with a SOAP Fault containing the exception's message");
                            SOAPUtil.replaceBodyWithNewFault(smc, e.getMessage());

                        }

                        if(!isForcedBackToClient) {
                            log.debug("Message is now forced to go to client");
                            log.trace("from now on, the loop can no longer change direction");

                            isForcedBackToClient = true;
                            SOAPUtil.setForcedBackToClient(smc, isForcedBackToClient);

                            if((!IS_SERVER_SIDE && isOutbound) || (IS_SERVER_SIDE && !isOutbound)) {
                                log.trace("reverse loop direction");

                                isOutbound = !isOutbound;
                                SOAPUtil.setOutboundMessage(smc, isOutbound);

                                log.trace("advance list iterator to skip current extension");
                                directionAwareNext(extListIterator, isOutbound);
                            }
                        }

                    } else if(e instanceof ExtensionException) {
                        log.debug("caught extension exception (that is not a web service interceptor exception)");
                        log.debug(e);
                        log.trace("extension exception details", e);

                        // No changes to flags are necessary in this case,
                        // because processing will be aborted by the upward propagation of the runtime exception

                        log.trace("throwing runtime exception with extension exception nested inside");
                        throw new RuntimeException(e);

                    } else if(e instanceof RuntimeException) {
                        log.debug("web service interceptor has thrown a run-time exception");
                        log.debug(e);
                        log.trace("run-time exception details", e);

                        // No changes to flags are necessary in this case,
                        // because processing will be aborted by the upward propagation of the runtime exception

                        log.trace("rethrowing");
                        throw (RuntimeException) e;

                    } else if(e instanceof SOAPException) {
                        log.trace("rethrowing SOAPException");
                        throw (SOAPException) e;
                    }

                } // catch(Exception)

                // update isFault
                isFault = SOAPUtil.isFaultMessage(smc);
                if(log.isTraceEnabled()) {
                    log.trace("message " + (isFault ? "contains" : "does not contain") + " a fault");
                }
                log.trace("proceed to next step in loop");

            } // while


            log.debug("Proceeding after web service interceptor loop");

            if(isForcedBackToClient) {
                // either return false or SOAPFaultException or WebServiceInterceptorException
                log.trace("Something happened to normal message flow");
                log.debug("Message is forced to go to client");

                // return false
                if(!interceptMessageReturnValue) {
                    log.trace("returning false from intercept message");
                    return false;
                }

                // ! either a SOAPFaultException or a WebServiceInterceptorException was thrown

                // check fault status
                if(isFault) {
                    // check initial fault status
                    if(WAS_FAULT) {
                        log.trace("Relying on JAX-WS engine to handle the fault");
                        log.trace("returning true from intercept message.");
                        return true;
                    } else {
                        // message is a newly created SOAP fault, throw a SOAPFaultException
                        SOAPFault sf = SOAPUtil.getFault(smc.getMessage());
                        if(sf == null) {
                            String warning = "Message should contain a fault. SOAPFault should not be null.";
                            log.warn(warning);
                            throw new IllegalStateException(warning);
                        }
                        log.trace("SOAP message contains a newly added fault; throwing SOAPFaultException with it");
                        throw new SOAPFaultException(sf);
                    }
                } else {
                    // !isFault
                    log.trace("Message is not a fault but it is being forced to go back to client");
                    log.trace("Probably a fault is hidden somehow (e.g. inside a ciphered body). Return message to client as is.");

                    log.trace("returning false from intercept message.");
                    return false;
                }

            }


            // return value
            if(!interceptMessageReturnValue) {
                String warning = "Return value is false, but all return false cases should have been handled elsewhere.";
                log.warn(warning);
                throw new IllegalStateException(warning);
            }

            log.trace("returning true from intercept message");
            return true;

        } catch(SOAPException se) {
            // throw an unchecked exception if the SOAP message is in an illegal state
            // it can be an extensions programming error or a bad change made by some web service interceptor
            String msg = "The Web Service interceptor manager found the SOAP message in an illegal state; " +
                         "please check nested SOAPException for further details";
            throw new IllegalStateException(msg, se);
        }

    } // interceptMessage


    // Helper method to initialize list iterator according to message direction
    private ListIterator<Extension> directionAwareInitListIterator(List<Extension> extList, boolean isOutbound) {
        if(isOutbound) {
            return extList.listIterator();
        } else {
            return extList.listIterator(extList.size());
        }
    }

    // Helper method to check initialize list iterator according to message direction
    private boolean directionAwareHasNext(ListIterator<Extension> extListIterator, boolean isOutbound) {
        if(isOutbound) {
            return extListIterator.hasNext();
        } else {
            return extListIterator.hasPrevious();
        }
    }

    // Helper method to check initialize list iterator according to message direction
    private Extension directionAwareNext(ListIterator<Extension> extListIterator, boolean isOutbound) {
        if(isOutbound) {
            return extListIterator.next();
        } else {
            return extListIterator.previous();
        }
    }

    // Helper exception class to allow similar treatment of return false as SOAPFaultException and WebServiceInterceptorException
    private class ReturnFalseException extends Exception {
		private static final long serialVersionUID = 1L;
        // empty
    }

}
