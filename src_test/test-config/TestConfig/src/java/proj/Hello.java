package proj;

import step.framework.config.*;


/**
 * This class is <b>important</b> as it contains the main program method.
 */
public class Hello {

	public Hello() { }

    /**
     * Executes the test program
     *
     * @param  args an array of strings provided as input for the program
     */
	public static void main(String args[]) throws Exception {

		System.out.println("Test application beginning");

		System.out.println("Parameters specified in command line");
		for(int i=0; i<args.length; i++) {
		    System.out.println("args[" + i + "]=" + args[i]);
		}
		System.out.println("Loading configuration resource file");
		Config config = Config.getInstance();
		config.load(); // default is "/config.properties"
		config.load("/db.properties");

		config.loadWithPrefix("global."); // default is "/config.properties"
		config.loadWithPrefix("/db.properties", "database.");


		System.out.println("Accessing initialization parameter values");

		System.out.println("paramDir=" + config.getInitParameter("paramDir"));
		System.out.println("p2=" + config.getInitParameter("p2"));

		System.out.println("db.name=" + config.getInitParameter("db.name"));
		System.out.println("db.user=" + config.getInitParameter("db.user"));
		System.out.println("db.pass=" + config.getInitParameter("db.pass"));

		System.out.println("Resetting configuration");
		config.reset();

		System.out.println("Using configuration auto-load");
		System.out.println("paramDir=" + config.getInitParameter("paramDir"));
		System.out.println("p2=" + config.getInitParameter("p2"));


		System.out.println("Test application ending");
	}

}
