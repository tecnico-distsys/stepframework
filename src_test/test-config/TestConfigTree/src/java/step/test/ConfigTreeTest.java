package step.test;

import java.io.*;

import step.framework.config.tree.*;

import step.test.dot.*;


public class ConfigTreeTest {

    private static final boolean PRINT_STACK_TRACE = false;


    public static void main(String[] argv) throws Exception {
        try {
            System.out.println("Begin " + ConfigTreeTest.class.getSimpleName());

            testEmptyTree();
            testDefaultConfigTree();
            // default
            test1LevelConfigTree(true);
            test2LevelConfigTree(true);
            test3LevelConfigTree(true);
            testMultiLevelConfigTree(true);
            // no default
            test1LevelConfigTree(false);
            test2LevelConfigTree(false);
            test3LevelConfigTree(false);
            testMultiLevelConfigTree(false);


        } finally {
            System.out.println("End " + ConfigTreeTest.class.getSimpleName());
            System.out.println();
        }
    }


    private static void testEmptyTree() {
        try {
            System.out.println("testEmptyTree(...");
            // -----------------------------------------------------------------

            System.out.println("Create tree...");
            ConfigTree<DotConfig> tree = new ConfigTree<DotConfig>(new DotPathParser());

            System.out.println("Default config...");
            System.out.println(tree.getDefaultConfig());

            testPrintTree(tree);

            testFindMostSpecificConfig(tree);

            // -----------------------------------------------------------------
        } finally {
            System.out.println("...)");
            System.out.println();
        }
    }

    private static void testDefaultConfigTree() {
        try {
            System.out.println("testDefaultConfigTree(...");
            // -----------------------------------------------------------------

            System.out.println("Create tree...");
            ConfigTree<DotConfig> tree = new ConfigTree<DotConfig>(new DotPathParser());

            System.out.println("Set default config...");
            {
                DotConfig dc = new DotConfig("default config");
                tree.setDefaultConfig(dc);
            }
            System.out.println("Default config...");
            System.out.println(tree.getDefaultConfig());

            testPrintTree(tree);

            testFindMostSpecificConfig(tree);

            // -----------------------------------------------------------------
        } finally {
            System.out.println("...)");
            System.out.println();
        }
    }

    private static void test1LevelConfigTree(boolean useDefaultConfig) {
        try {
            System.out.println("test1LevelConfigTree(...");
            // -----------------------------------------------------------------

            System.out.println("Create tree...");
            ConfigTree<DotConfig> tree = new ConfigTree<DotConfig>(new DotPathParser());

            if(useDefaultConfig) {
                System.out.println("Set default config...");
                DotConfig dc = new DotConfig("default config");
                tree.setDefaultConfig(dc);
            }

            {
                String path = "a";
                System.out.println("Set config for path " + path + " ...");

                DotConfig config = new DotConfig("config for " + path);
                tree.setConfig(path, config);
            }
            {
                String path = "a";
                System.out.println("Get config for path " + path + " ...");

                DotConfig config = tree.getConfig(path);
                System.out.println(config);
            }

            testPrintTree(tree);

            testFindMostSpecificConfig(tree);

            // -----------------------------------------------------------------
        } finally {
            System.out.println("...)");
            System.out.println();
        }
    }

    private static void test2LevelConfigTree(boolean useDefaultConfig) {
        try {
            System.out.println("test2LevelConfigTree(...");
            // -----------------------------------------------------------------

            System.out.println("Create tree...");
            ConfigTree<DotConfig> tree = new ConfigTree<DotConfig>(new DotPathParser());

            if(useDefaultConfig) {
                System.out.println("Set default config...");
                DotConfig dc = new DotConfig("default config");
                tree.setDefaultConfig(dc);
            }
            {
                String path = "a.b";
                System.out.println("Set config for path " + path + " ...");

                DotConfig config = new DotConfig("config for " + path);
                tree.setConfig(path, config);
            }
            {
                String path = "a.b";
                System.out.println("Get config for path " + path + " ...");

                DotConfig config = tree.getConfig(path);
                System.out.println(config);
            }

            testPrintTree(tree);

            testFindMostSpecificConfig(tree);

            // -----------------------------------------------------------------
        } finally {
            System.out.println("...)");
            System.out.println();
        }
    }

    private static void test3LevelConfigTree(boolean useDefaultConfig) {
        try {
            System.out.println("test3LevelConfigTree(...");
            // -----------------------------------------------------------------

            System.out.println("Create tree...");
            ConfigTree<DotConfig> tree = new ConfigTree<DotConfig>(new DotPathParser());

            if(useDefaultConfig) {
                System.out.println("Set default config...");
                DotConfig dc = new DotConfig("default config");
                tree.setDefaultConfig(dc);
            }
            {
                String path = "a.b.c";
                System.out.println("Set config for path " + path + " ...");

                DotConfig config = new DotConfig("config for " + path);
                tree.setConfig(path, config);
            }
            {
                String path = "a.b.c";
                System.out.println("Get config for path " + path + " ...");

                DotConfig config = tree.getConfig(path);
                System.out.println(config);
            }

            testPrintTree(tree);

            testFindMostSpecificConfig(tree);

            // -----------------------------------------------------------------
        } finally {
            System.out.println("...)");
            System.out.println();
        }
    }

    private static void testMultiLevelConfigTree(boolean useDefaultConfig) {
        try {
            System.out.println("testMultiLevelConfigTree(...");
            // -----------------------------------------------------------------

            System.out.println("Create tree...");
            ConfigTree<DotConfig> tree = new ConfigTree<DotConfig>(new DotPathParser());

            if(useDefaultConfig) {
                System.out.println("Set default config...");
                DotConfig dc = new DotConfig("default config");
                tree.setDefaultConfig(dc);
            }
            {
                String path = "a";
                System.out.println("Set config for path " + path + " ...");

                DotConfig config = new DotConfig("config for " + path);
                tree.setConfig(path, config);
            }
            {
                String path = "a.b.c";
                System.out.println("Set config for path " + path + " ...");

                DotConfig config = new DotConfig("config for " + path);
                tree.setConfig(path, config);
            }
            {
                String path = "a.e.i.o.u";
                System.out.println("Set config for path " + path + " ...");

                DotConfig config = new DotConfig("config for " + path);
                tree.setConfig(path, config);
            }

            {
                String path = "a";
                System.out.println("Get config for path " + path + " ...");

                DotConfig config = tree.getConfig(path);
                System.out.println(config);
            }
            {
                String path = "a.b";
                System.out.println("Get config for path " + path + " ...");

                DotConfig config = tree.getConfig(path);
                System.out.println(config);
            }
            {
                String path = "a.b.c";
                System.out.println("Get config for path " + path + " ...");

                DotConfig config = tree.getConfig(path);
                System.out.println(config);
            }
            {
                String path = "a.e.i";
                System.out.println("Get config for path " + path + " ...");

                DotConfig config = tree.getConfig(path);
                System.out.println(config);
            }
            {
                String path = "a.e.i.o.u";
                System.out.println("Get config for path " + path + " ...");

                DotConfig config = tree.getConfig(path);
                System.out.println(config);
            }

            testPrintTree(tree);

            testFindMostSpecificConfig(tree);

            // -----------------------------------------------------------------
        } finally {
            System.out.println("...)");
            System.out.println();
        }
    }


    private static void testPrintTree(ConfigTree tree) {
        tree.setName("Dot-paths");
        tree.print(System.out);
        System.out.println();
    }


    private static void testFindMostSpecificConfig(ConfigTree tree) {
        System.out.println("Expecting exceptions:");
        tryFindMostSpecificConfig(tree, null);
        tryFindMostSpecificConfig(tree, ".");
        tryFindMostSpecificConfig(tree, "..");
        tryFindMostSpecificConfig(tree, "...");
        System.out.println("Expecting config:");
        tryFindMostSpecificConfig(tree, "");
        tryFindMostSpecificConfig(tree, "x");
        tryFindMostSpecificConfig(tree, "a");
        tryFindMostSpecificConfig(tree, "a.b");
        tryFindMostSpecificConfig(tree, "a.b.c");
        tryFindMostSpecificConfig(tree, "a.b.c.x");
        tryFindMostSpecificConfig(tree, "a.e");
        tryFindMostSpecificConfig(tree, "a.e.i");
        tryFindMostSpecificConfig(tree, "a.e.i.o");
        tryFindMostSpecificConfig(tree, "a.e.i.o.u");
        tryFindMostSpecificConfig(tree, "a.e.i.o.u.x");
    }

    private static void tryFindMostSpecificConfig(ConfigTree tree, String configPath) {
        try {
            System.out.print("Most specific config for " + configPath + " is ");
            System.out.println(tree.findMostSpecificConfig(configPath));
        } catch(Exception e) {
            System.out.print("~ " + e.getClass().getName());
            if(e.getMessage() != null) {
                System.out.print(" ");
                System.out.print(e.getMessage());
            }
            System.out.println();
            if(PRINT_STACK_TRACE) {
                e.printStackTrace();
            }
        }
    }

}