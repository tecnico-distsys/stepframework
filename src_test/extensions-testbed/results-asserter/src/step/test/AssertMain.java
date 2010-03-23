package step.test;

import java.io.*;

import org.apache.commons.logging.*;

/**
 */
public class AssertMain {

    /** Logging */
    private static Log log = LogFactory.getLog(AssertMain.class);

    /** Main */
    public static void main(String[] args) {
        try {
            log.info("Results asserter starting");

            if(log.isDebugEnabled()) {
                log.debug("Program arguments:");
                for(int i = 0; i < args.length; i++) {
                    log.debug(String.format("args[%d] '%s'", i, args[i]));
                }
            }

            if(args.length != 2) {
                log.fatal("Required arguments are missing!");
                log.info("Usage: <configuration> <resultDir>");
                return;
            }

            AssertMain assertMain = new AssertMain();
            AssertContext ctx = assertMain.buildContext(args);
            assertMain.dispatch(ctx);

        } finally {
            log.info("End of results asserter.");
        }
    }

    /** Define values for properties used by asserters */
    public AssertContext buildContext(String[] args) {
        final String SEP = System.getProperty("file.separator");

        log.debug("Building context");
        AssertContext ctx = new AssertContext();

        //
        //  arguments
        //
        ctx.put("config", args[0]);
        ctx.put("resultDir", args[1]);


        //
        //  result files
        //
        ctx.put("clientLog", ctx.get("resultDir") + SEP + "log-client.log");
        ctx.put("serverLog", ctx.get("resultDir") + SEP + "log-server.log");

        ctx.put("clientSoap", ctx.get("resultDir") + SEP + "soap-trace-client.log");
        ctx.put("serverSoap", ctx.get("resultDir") + SEP + "soap-trace-server.log");


        if(log.isTraceEnabled()) {
            log.trace("Context:");
            for(String key : ctx.keySet())
                log.trace(String.format("%s: %s", key, ctx.get(key)));
        }

        return ctx;
    }

    /** Recognize tested configuration and apply assertions */
    public void dispatch(AssertContext ctx) {
        log.debug("Entering assert dispatch");
        final String CONFIG = ctx.get("config");
        if(log.isDebugEnabled()) {
            log.debug("Asserting configuration '" + CONFIG + "'");
        }

        try {

            if("ok".equals(CONFIG)) {
                Assertions.assertClientNoException(ctx);
                Assertions.assertClientHelloFriend(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 0, 0); // client envelopes, server envelopes,
                                                              //    client faults, server faults

            } else if("throw-serviceerrorexception".equals(CONFIG)) {
                Assertions.assertClientServiceErrorException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 2, 2);

            } else if("return-false-cout".equals(CONFIG)) {
                Assertions.assertClientUnexpectedXML(ctx);
                Assertions.assertSOAPCounts(ctx, 2, 0, 0, 0);
            } else if("return-false-sin".equals(CONFIG)) {
                Assertions.assertClientUnexpectedXML(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 2, 0, 0);
            } else if("return-false-sout".equals(CONFIG)) {
                Assertions.assertClientNoException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 0, 0);
            } else if("return-false-cin".equals(CONFIG)) {
                Assertions.assertClientNoException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 0, 0);

            } else if("throw-wsinterceptorexception-cout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 2, 0, 1, 0);
            } else if("throw-wsinterceptorexception-sin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 2, 2, 1);
            } else if("throw-wsinterceptorexception-sout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 2, 1);
            } else if("throw-wsinterceptorexception-cin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 1, 0);

            } else if("throw-soapfaultexception-cout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 2, 0, 1, 0);
            } else if("throw-soapfaultexception-sin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 2, 2, 1);
            } else if("throw-soapfaultexception-sout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 2, 1);
            } else if("throw-soapfaultexception-cin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 1, 0);

            } else if("throw-nullpointerexception-cout".equals(CONFIG)) {
                Assertions.assertClientNestedNullPointerException(ctx);
                Assertions.assertSOAPCounts(ctx, 1, 0, 0, 0);
            } else if("throw-nullpointerexception-sin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 1, 2, 0);
            } else if("throw-nullpointerexception-sout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 3, 2, 0);
            } else if("throw-nullpointerexception-cin".equals(CONFIG)) {
                Assertions.assertClientNestedNullPointerException(ctx);
                Assertions.assertSOAPCounts(ctx, 3, 4, 0, 0);

            //
            //  cipher cases
            //

            } else if("cipher-ok".equals(CONFIG)) {
                Assertions.assertClientNoException(ctx);
                Assertions.assertClientHelloFriend(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 0, 0); // client envelopes, server envelopes,
                                                              //    client faults, server faults
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2); // client, server

            } else if("cipher-throw-serviceerrorexception".equals(CONFIG)) {
                Assertions.assertClientServiceErrorException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 1, 1);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);

            } else if("cipher-return-false-cout".equals(CONFIG)) {
                Assertions.assertClientUnexpectedXML(ctx);
                Assertions.assertSOAPCounts(ctx, 2, 0, 0, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 0, 0);
            } else if("cipher-return-false-sin".equals(CONFIG)) {
                Assertions.assertClientUnexpectedXML(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 2, 0, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);
            } else if("cipher-return-false-sout".equals(CONFIG)) {
                Assertions.assertClientNoException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 0, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);
            } else if("cipher-return-false-cin".equals(CONFIG)) {
                Assertions.assertClientNoException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 0, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);

            } else if("cipher-throw-wsinterceptorexception-cout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultExceptionFailedToDecipher(ctx);
                Assertions.assertSOAPCounts(ctx, 2, 0, 1, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 0, 0);
            } else if("cipher-throw-wsinterceptorexception-sin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 2, 1, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);
            } else if("cipher-throw-wsinterceptorexception-sout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 1, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);
            } else if("cipher-throw-wsinterceptorexception-cin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultExceptionFailedToDecipher(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 1, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);

            } else if("cipher-throw-soapfaultexception-cout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultExceptionFailedToDecipher(ctx);
                Assertions.assertSOAPCounts(ctx, 2, 0, 1, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 0, 0);
            } else if("cipher-throw-soapfaultexception-sin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 2, 1, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);
            } else if("cipher-throw-soapfaultexception-sout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultException(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 1, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);
            } else if("cipher-throw-soapfaultexception-cin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultExceptionFailedToDecipher(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 4, 1, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);

            } else if("cipher-throw-nullpointerexception-cout".equals(CONFIG)) {
                Assertions.assertClientNestedNullPointerException(ctx);
                Assertions.assertSOAPCounts(ctx, 1, 0, 0, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 0, 0);
            } else if("cipher-throw-nullpointerexception-sin".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultExceptionFailedToDecipher(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 1, 2, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 1, 1);
            } else if("cipher-throw-nullpointerexception-sout".equals(CONFIG)) {
                Assertions.assertClientSOAPFaultExceptionFailedToDecipher(ctx);
                Assertions.assertSOAPCounts(ctx, 4, 3, 2, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 1, 1);
            } else if("cipher-throw-nullpointerexception-cin".equals(CONFIG)) {
                Assertions.assertClientNestedNullPointerException(ctx);
                Assertions.assertSOAPCounts(ctx, 3, 4, 0, 0);
                Assertions.assertSOAPBodyCipherCounts(ctx, 2, 2);

            } else {
                throw new AssertException("Unknown configuration " + CONFIG);
            }

            log.info("SUCCESS! All assertions executed successfully for configuration '" + CONFIG + "'");

        } catch(AssertException ae) {
            log.info("FAILURE! Assertion failed for configuration '" + CONFIG + "'");
            log.info("Details", ae);

        } catch(Exception e) {
            log.error("FAILURE! Caught unexpected exception " + e.getClass().getName() + " while testing configuration '" + CONFIG + "'");
            log.error("Details", e);
        }

    }

}
