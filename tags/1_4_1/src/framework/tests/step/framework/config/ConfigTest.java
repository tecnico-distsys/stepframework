package step.framework.config;

import org.junit.*;
import static org.junit.Assert.*;


public class ConfigTest {

    Config config;

    @Before
    public void setUp() {
        config = Config.getInstance();
    }

    @After
    public void tearDown() {
        config.reset();
        config = null;
    }

    @Test
    public void testConfigAutoLoad() throws ConfigException {
        assertEquals("c:/temp", config.getInitParameter("paramDir"));
        assertEquals("xpto", config.getInitParameter("p2"));
    }

    @Test
    public void testConfigDefaultLoad() throws ConfigException {
        config.load(); // default is "/config.properties"
        assertEquals("c:/temp", config.getInitParameter("paramDir"));
        assertEquals("xpto", config.getInitParameter("p2"));
    }

    @Test
    public void testConfigLoad() throws ConfigException {
        config.load("/db.properties");
        assertEquals("db1", config.getInitParameter("db.name"));
        assertEquals("user1", config.getInitParameter("db.user"));
        assertEquals("pass1", config.getInitParameter("db.pass"));
    }

    @Test
    public void testConfigDefaultLoadPrefix() throws ConfigException {
        config.loadWithPrefix("global."); // default is "/config.properties"
        assertEquals("c:/temp", config.getInitParameter("global.paramDir"));
        assertEquals("xpto", config.getInitParameter("global.p2"));
    }

    @Test
    public void testConfigLoadPrefix() throws ConfigException {
        config.loadWithPrefix("/db.properties", "database.");
        assertEquals("db1", config.getInitParameter("database.db.name"));
        assertEquals("user1", config.getInitParameter("database.db.user"));
        assertEquals("pass1", config.getInitParameter("database.db.pass"));
    }

    @Test
    public void testConfigMultiLoad() throws ConfigException {
        config.load(); // default is "/config.properties"
        config.load("/db.properties");

        assertEquals("c:/temp", config.getInitParameter("paramDir"));

        assertEquals("user1", config.getInitParameter("db.user"));
    }

}
