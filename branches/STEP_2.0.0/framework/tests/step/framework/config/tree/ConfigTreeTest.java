package step.framework.config.tree;

import step.framework.config.tree.dot.*;

import org.junit.*;
import static org.junit.Assert.*;


public class ConfigTreeTest {

    ConfigTree<DotConfig> tree;

    @Before
    public void setUp() {
        tree = new ConfigTree<DotConfig>(new DotPathParser());
        tree.setName("Dot-paths");
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testEmpty() {

        DotConfig config = tree.getDefaultConfig();
        assertNull(config);

    }

    @Test
    public void testDefault() {

        DotConfig defaultConfig = new DotConfig("default config");
        tree.setDefaultConfig(defaultConfig);
        assertEquals("default config", tree.getDefaultConfig().toString());

    }

    @Test
    public void testDepth1() {

        DotConfig defaultConfig = new DotConfig("default config");
        tree.setDefaultConfig(defaultConfig);

        {
            String path = "a";
            DotConfig config = new DotConfig("config for " + path);
            tree.setConfig(path, config);
        }
        {
            String path = "a";
            DotConfig config = tree.getConfig(path);
            assertEquals("config for " + path, config.toString());
        }

    }

    @Test
    public void testDepth2() {

        DotConfig defaultConfig = new DotConfig("default config");
        tree.setDefaultConfig(defaultConfig);

        {
            String path = "a.b";
            DotConfig config = new DotConfig("config for " + path);
            tree.setConfig(path, config);
        }
        {
            String path = "a.b";
            DotConfig config = tree.getConfig(path);
            assertEquals("config for " + path, config.toString());
        }

    }

    @Test
    public void testDepth3() {

        DotConfig defaultConfig = new DotConfig("default config");
        tree.setDefaultConfig(defaultConfig);

        {
            String path = "a.b.c";
            DotConfig config = new DotConfig("config for " + path);
            tree.setConfig(path, config);
        }
        {
            String path = "a.b.c";
            DotConfig config = tree.getConfig(path);
            assertEquals("config for " + path, config.toString());
        }

    }


    private void addNodes(boolean addDefault) {
        if(addDefault) {
            DotConfig defaultConfig = new DotConfig("default config");
            tree.setDefaultConfig(defaultConfig);
        }

        {
            String path = "a";
            DotConfig config = new DotConfig("config for " + path);
            tree.setConfig(path, config);
        }
        {
            String path = "a.b.c";
            DotConfig config = new DotConfig("config for " + path);
            tree.setConfig(path, config);
        }
        {
            String path = "a.e.i.o.u";
            DotConfig config = new DotConfig("config for " + path);
            tree.setConfig(path, config);
        }
    }

    /*
         Configuration tree Dot-paths
         root [ConfigNode: config=null/default config, #childNodes=1]
         a [ConfigNode: config=config for a, #childNodes=2]
             e [ConfigNode: config=null, #childNodes=1]
                 i [ConfigNode: config=null, #childNodes=1]
                     o [ConfigNode: config=null, #childNodes=1]
                         u [ConfigNode: config=config for a.e.i.o.u, #childNodes=0]
             b [ConfigNode: config=null, #childNodes=1]
                 c [ConfigNode: config=config for a.b.c, #childNodes=0]
    */

    @Test
    public void testGetConfig() {

        addNodes(true);

        {
            String path = "a";
            DotConfig config = tree.getConfig(path);
            assertEquals("config for " + path, config.toString());
        }
        {
            String path = "a.b";
            DotConfig config = tree.getConfig(path);
            assertNull(config);
        }
        {
            String path = "a.b.c";
            DotConfig config = tree.getConfig(path);
            assertEquals("config for " + path, config.toString());
        }
        {
            String path = "a.e.i";
            DotConfig config = tree.getConfig(path);
            assertNull(config);
        }
        {
            String path = "a.e.i.o.u";
            DotConfig config = tree.getConfig(path);
            assertEquals("config for " + path, config.toString());
        }

    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindNull() {
        addNodes(true);
        tree.findMostSpecificConfig(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFind1Dot() {
        addNodes(true);
        tree.findMostSpecificConfig(".");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFind2Dot() {
        addNodes(false);
        tree.findMostSpecificConfig("..");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFind3Dot() {
        addNodes(true);
        tree.findMostSpecificConfig("...");
    }

    @Test
    public void testFindWithDefault() {
        addNodes(true);
        assertEquals("default config", tree.findMostSpecificConfig("").toString());
        assertEquals("config for a", tree.findMostSpecificConfig("a").toString());
        assertEquals("config for a", tree.findMostSpecificConfig("a.b").toString());
        assertEquals("config for a.b.c", tree.findMostSpecificConfig("a.b.c").toString());
        assertEquals("config for a.b.c", tree.findMostSpecificConfig("a.b.c.x").toString());
        assertEquals("config for a", tree.findMostSpecificConfig("a.e").toString());
        assertEquals("config for a", tree.findMostSpecificConfig("a.e.i").toString());
        assertEquals("config for a", tree.findMostSpecificConfig("a.e.i.o").toString());
        assertEquals("config for a.e.i.o.u", tree.findMostSpecificConfig("a.e.i.o.u").toString());
        assertEquals("config for a.e.i.o.u", tree.findMostSpecificConfig("a.e.i.o.u.x").toString());
        assertEquals("default config", tree.findMostSpecificConfig("x").toString());
    }

    @Test
    public void testFindWithoutDefault() {
        addNodes(false);
        assertNull(tree.findMostSpecificConfig(""));
        assertEquals("config for a", tree.findMostSpecificConfig("a").toString());
        assertEquals("config for a", tree.findMostSpecificConfig("a.b").toString());
        assertEquals("config for a.b.c", tree.findMostSpecificConfig("a.b.c").toString());
        assertEquals("config for a.b.c", tree.findMostSpecificConfig("a.b.c.x").toString());
        assertEquals("config for a", tree.findMostSpecificConfig("a.e").toString());
        assertEquals("config for a", tree.findMostSpecificConfig("a.e.i").toString());
        assertEquals("config for a", tree.findMostSpecificConfig("a.e.i.o").toString());
        assertEquals("config for a.e.i.o.u", tree.findMostSpecificConfig("a.e.i.o.u").toString());
        assertEquals("config for a.e.i.o.u", tree.findMostSpecificConfig("a.e.i.o.u.x").toString());
        assertNull(tree.findMostSpecificConfig("x"));
    }

}
