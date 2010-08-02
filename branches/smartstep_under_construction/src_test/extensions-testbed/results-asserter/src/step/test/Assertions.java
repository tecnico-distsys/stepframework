package step.test;

import org.apache.commons.logging.*;


/**
 *  Assertion methods
 */
public class Assertions {

    /** Logging */
    private static Log log = LogFactory.getLog(Assertions.class);


    //
    //  Log assertions
    //

    public static void assertClientNoException(AssertContext ctx) throws Exception {
        log.info(getMethodName());
        String regex = "(?i).*Action message.*Exception.*";
        int lineNr = TextFileUtil.lineOfPattern(ctx.get("clientLog"), regex);
        if(lineNr != -1)
            throw new AssertException("Found unexpected exception in client log (line " + lineNr + ")");
    }

    public static void assertClientHelloFriend(AssertContext ctx) throws Exception {
        log.info(getMethodName());
        String regex = "(?i).*Action message.*Hello friend.*";
        int lineNr = TextFileUtil.lineOfPattern(ctx.get("clientLog"), regex);
        if(lineNr == -1)
            throw new AssertException("Expression " + regex + " should have matched a line in client log");
    }

    public static void assertClientServiceErrorException(AssertContext ctx) throws Exception {
        log.info(getMethodName());
        String regex = "(?i).*Action message.*ServiceError_Exception.*Name is missing.*";
        int lineNr = TextFileUtil.lineOfPattern(ctx.get("clientLog"), regex);
        if(lineNr == -1)
            throw new AssertException("Expression " + regex + " should have matched a line in client log");
    }

    public static void assertClientUnexpectedXML(AssertContext ctx) throws Exception {
        log.info(getMethodName());
        String regex = "(?i).*Action message.*XMLStreamReaderException.*unexpected XML tag.*";
        int lineNr = TextFileUtil.lineOfPattern(ctx.get("clientLog"), regex);
        if(lineNr == -1)
            throw new AssertException("Expression " + regex + " should have matched a line in client log");
    }

    public static void assertClientSOAPFaultException(AssertContext ctx) throws Exception {
        log.info(getMethodName());
        String regex = "(?i).*Action message.*SOAPFaultException.*generated.*";
        int lineNr = TextFileUtil.lineOfPattern(ctx.get("clientLog"), regex);
        if(lineNr == -1)
            throw new AssertException("Expression " + regex + " should have matched a line in client log");
    }

    public static void assertClientSOAPFaultExceptionFailedToDecipher(AssertContext ctx) throws Exception {
        log.info(getMethodName());
        String regex = "(?i).*Action message.*SOAPFaultException.*plain text message.*";
        int lineNr = TextFileUtil.lineOfPattern(ctx.get("clientLog"), regex);
        if(lineNr == -1)
            throw new AssertException("Expression " + regex + " should have matched a line in client log");
    }

    public static void assertClientNestedNullPointerException(AssertContext ctx) throws Exception {
        log.info(getMethodName());
        String regex = "(?i).*Action message.*WebServiceException.*NullPointerException.*generated.*";
        int lineNr = TextFileUtil.lineOfPattern(ctx.get("clientLog"), regex);
        if(lineNr == -1)
            throw new AssertException("Expression " + regex + " should have matched a line in client log");
    }


    //
    //  SOAP trace assertions
    //

    private static void assertCountSOAPEnvelopes(String file, int expectedCount, String fileDescription) throws Exception {
        String regex = ".*<[^/].*:Envelope.*";
        int lineCounter = TextFileUtil.lineCountPattern(file, regex);
        if(lineCounter != expectedCount) {
            String message = "Expecting " + expectedCount + " SOAP envelopes but found " + lineCounter + " in " + fileDescription;
            throw new AssertException(message);
        }
    }

    private static void assertCountSOAPFaults(String file, int expectedCount, String fileDescription) throws Exception {
        String regex = ".*<[^/].*:Fault.*";
        int lineCounter = TextFileUtil.lineCountPattern(file, regex);
        if(lineCounter != expectedCount) {
            String message = "Expecting " + expectedCount + " SOAP faults but found " + lineCounter + " in " + fileDescription;
            throw new AssertException(message);
        }
    }

    private static void assertCountSOAPBodyCipher(String file, int expectedCount, String fileDescription) throws Exception {
        String regex = ".*<[^/]?CipherBody.*";
        int lineCounter = TextFileUtil.lineCountPattern(file, regex);
        if(lineCounter != expectedCount) {
            String message = "Expecting " + expectedCount + " SOAP body ciphers but found " + lineCounter + " in " + fileDescription;
            throw new AssertException(message);
        }
    }

    private static void assertCountForcedBack(String file, int expectedCount, String fileDescription) throws Exception {
        String regex = ".*bound.*SOAP Message.*forced back to client.*";
        int lineCounter = TextFileUtil.lineCountPattern(file, regex);
        if(lineCounter != expectedCount) {
            String message = "Expecting " + expectedCount + " forced backs but found " + lineCounter + " in " + fileDescription;
            throw new AssertException(message);
        }
    }


    public static void assertCountClientSOAPEnvelopes(AssertContext ctx, int expected) throws Exception {
        log.info(getMethodName());
        assertCountSOAPEnvelopes(ctx.get("clientSoap"), expected, "client soap trace");
    }

    public static void assertCountServerSOAPEnvelopes(AssertContext ctx, int expected) throws Exception {
        log.info(getMethodName());
        assertCountSOAPEnvelopes(ctx.get("serverSoap"), expected, "server soap trace");
    }

    public static void assertCountClientSOAPFaults(AssertContext ctx, int expected) throws Exception {
        log.info(getMethodName());
        assertCountSOAPFaults(ctx.get("clientSoap"), expected, "client soap trace");
    }

    public static void assertCountServerSOAPFaults(AssertContext ctx, int expected) throws Exception {
        log.info(getMethodName());
        assertCountSOAPFaults(ctx.get("serverSoap"), expected, "server soap trace");
    }

    public static void assertCountClientSOAPBodyCipher(AssertContext ctx, int expected) throws Exception {
        log.info(getMethodName());
        assertCountSOAPBodyCipher(ctx.get("clientSoap"), expected, "client soap trace");
    }

    public static void assertCountServerSOAPBodyCipher(AssertContext ctx, int expected) throws Exception {
        log.info(getMethodName());
        assertCountSOAPBodyCipher(ctx.get("serverSoap"), expected, "server soap trace");
    }

    public static void assertCountClientForcedBack(AssertContext ctx, int expected) throws Exception {
        log.info(getMethodName());
        assertCountForcedBack(ctx.get("clientSoap"), expected, "client soap trace");
    }

    public static void assertCountServerForcedBack(AssertContext ctx, int expected) throws Exception {
        log.info(getMethodName());
        assertCountForcedBack(ctx.get("serverSoap"), expected, "server soap trace");
    }


    public static void assertSOAPCounts(AssertContext ctx,
        int clientSoapEnvelopeCount, int serverSoapEnvelopeCount,
        int clientSoapFaultCount, int serverSoapFaultCount) throws Exception {

        log.info(getMethodName());
        assertCountClientSOAPEnvelopes(ctx, clientSoapEnvelopeCount);
        assertCountServerSOAPEnvelopes(ctx, serverSoapEnvelopeCount);
        assertCountClientSOAPFaults(ctx, clientSoapFaultCount);
        assertCountServerSOAPFaults(ctx, serverSoapFaultCount);
    }

    public static void assertSOAPBodyCipherCounts(AssertContext ctx,
        int clientSoapBodyCipherCount, int serverSoapBodyCipherCount) throws Exception {

        log.info(getMethodName());
        assertCountClientSOAPBodyCipher(ctx, clientSoapBodyCipherCount);
        assertCountServerSOAPBodyCipher(ctx, serverSoapBodyCipherCount);
    }

    public static void assertSOAPForcedBackCounts(AssertContext ctx,
        int clientCount, int serverCount) throws Exception {

        log.info(getMethodName());
        assertCountClientForcedBack(ctx, clientCount);
        assertCountServerForcedBack(ctx, serverCount);
    }



    //
    //  Helpers
    //

    private static String getMethodName()
    {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        // ste[0] is getStackTrace
        // ste[1] is getMethodName
        // ste[2] is calling method
        return ste[2].getMethodName();
    }

}
