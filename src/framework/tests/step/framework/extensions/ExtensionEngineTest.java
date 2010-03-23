package step.framework.extensions;

import java.io.PrintStream;
import java.util.Map;

import org.junit.*;
import static org.junit.Assert.*;


public class ExtensionEngineTest {

    ExtensionEngine engine;
    private PrintStream out = System.out;
    
    @Before
    public void setUp() {
        engine = ExtensionEngine.getInstance();
    }

    @After
    public void tearDown() throws ExtensionEngineException {
        engine.destroy();
        engine = null;
    }

    private void assertEngineState() {
        // check engine
        assertTrue(engine.isEnabled());
        assertEquals("123", engine.getConfig().getProperty("custom2"));
        assertEquals(3, engine.getExtensionList().size());

        // check extension ids
        Extension ext1 = engine.getExtensionList().get(0);
        Extension ext2 = engine.getExtensionList().get(1);
        Extension ext3 = engine.getExtensionList().get(2);
        
        assertEquals("ext1", ext1.getId());
        assertEquals("ext2", ext2.getId());
        assertEquals("ext3", ext3.getId());
        
        // check if parsed data in one of the extensions matches the data in the configuration file
        assertTrue(ext2.isEnabled());
        assertEquals("my.value", ext2.getConfig().getProperty("my.field"));
        
        // check if extension listener executed as expected
        assertTrue(ext3.isEnabled());
        Map<String,Object> ext3ctx = ext3.getContext();
        assertNotNull(ext3ctx);
        assertEquals("3", ext3ctx.get("ext3ctx.var"));
    }
    
    @Test
    public void testEngineDefaultInit() throws ExtensionEngineException {
        engine.init();
        assertEngineState();
    }

    @Test
    public void testEngineInit() throws ExtensionEngineException {
        engine.init("/extensions.properties");
        assertEngineState();
    }

    @Test
    public void testEngineAutoInit() throws ExtensionEngineException {
        boolean enabled = engine.isEnabled();   // triggers auto-initialization
        assertEngineState();
    }

    @Test(expected=ExtensionEngineException.class)
    public void testEngineWithBadExtension() throws ExtensionEngineException {
        try{ 
            engine.init("/extensions-bad.properties");
        } catch(ExtensionEngineException eee) {
            // verify error reporting
            assertFalse(engine.isEnabled());
            
            assertNotNull(eee.getMessage());
            assertTrue(eee.getMessage().indexOf("Failed to initialize extensions") != -1);
        
            Throwable cause = eee.getCause();
            assertNotNull(cause);
            assertNotNull(cause.getMessage());
            assertTrue(cause.getMessage().indexOf("Failed to cast") != -1);
            
            // rethrow exception
            throw eee;
        }
    }

}
