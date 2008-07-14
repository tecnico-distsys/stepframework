package step.test;

import java.io.*;

import step.framework.extensions.*;


public class ExtensionsLifecycleTest {

    private static final boolean PRINT_STACK_TRACE = true;
    private static PrintStream out = System.out;


    public static void main(String[] argv) throws Exception {
        try {
            out.println("Begin " + ExtensionsLifecycleTest.class.getSimpleName());

            testInit();
            testDestroy();

        } catch(Exception e) {
            out.print("Caught " + e.getClass().getName());
            if(e.getMessage() != null) {
                out.print(" ");
                out.print(e.getMessage());
            }
            out.println();
            if(PRINT_STACK_TRACE) {
                e.printStackTrace();
            }
        } finally {
            out.println("End " + ExtensionsLifecycleTest.class.getSimpleName());
            out.println();
        }
    }


    private static void testInit() throws Exception {
        try {
            out.println("testInit(...");
            // -----------------------------------------------------------------

            out.println("Get engine instance...");
            ExtensionEngine engine = ExtensionEngine.getInstance();

            out.println("Engine to string...");
            out.println(engine);

            out.println("Engine init...");
            engine.init();

            // -----------------------------------------------------------------
        } finally {
            out.println("...)");
            out.println();
        }
    }

    private static void testDestroy() throws Exception  {
        try {
            out.println("testDestroy(...");
            // -----------------------------------------------------------------

            out.println("Get engine instance...");
            ExtensionEngine engine = ExtensionEngine.getInstance();

            out.println("Engine to string...");
            out.println(engine);

            out.println("Engine destroy...");
            engine.destroy();

            // -----------------------------------------------------------------
        } finally {
            out.println("...)");
            out.println();
        }
    }

}