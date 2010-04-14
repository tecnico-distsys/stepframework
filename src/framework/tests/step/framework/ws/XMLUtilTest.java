package step.framework.ws;

import java.io.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;


public class XMLUtilTest {

    static final private String BR = System.getProperty("line.separator");
    private PrintStream out = System.out;
    
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPrettyPrintString() throws Exception {
        final String XML = 
            "<grandfather name=\"Tony\"><father name=\"Tony\">" +   
            "<son name=\"Mike\"/><son name=\"John\"></son>" +   
            "</father></grandfather>" + BR;
        
        final String EXPECTED = 
            "<grandfather name=\"Tony\">" + BR + 
            "  <father name=\"Tony\">" + BR + 
            "    <son name=\"Mike\"/>" + BR + 
            "    <son name=\"John\"/>" + BR + 
            "  </father>" + BR + 
            "</grandfather>" + BR;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLUtil.prettyPrint(XML, baos);
        String actual = baos.toString();

        assertEquals(EXPECTED, actual);
    }
}
