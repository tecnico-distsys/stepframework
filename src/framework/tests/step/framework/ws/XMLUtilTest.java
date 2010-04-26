package step.framework.ws;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class XMLUtilTest {

    static final private String LINE_SEPARATOR = System.getProperty("line.separator");
    
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
            "</father></grandfather>" + LINE_SEPARATOR;
        
        final String EXPECTED = 
            "<grandfather name=\"Tony\">" + LINE_SEPARATOR + 
            "  <father name=\"Tony\">" + LINE_SEPARATOR + 
            "    <son name=\"Mike\"/>" + LINE_SEPARATOR + 
            "    <son name=\"John\"/>" + LINE_SEPARATOR + 
            "  </father>" + LINE_SEPARATOR + 
            "</grandfather>" + LINE_SEPARATOR;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLUtil.prettyPrint(XML, baos);
        String actual = baos.toString();

        assertEquals(EXPECTED, actual);
    }
}
