package proj;

import step.framework.context.*;


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

		{
    		System.out.println("Storing data in application context (global variables)");
    		ApplicationContext appCtx = ApplicationContext.getInstance();
    		appCtx.put("application-attribute-key", "application attribute value");
        }
        {
    		System.out.println("Storing data in session context (indexed by session id)");
    		String sessionId = "1";
    		SessionContext sessionCtx = SessionContext.getInstance(sessionId);
    		sessionCtx.put("session-attribute-key", "session attribute value");
        }
        {
    		System.out.println("Storing data in thread context (indexed by thread id)");
    		ThreadContext threadCtx = ThreadContext.getInstance();
    		threadCtx.put("thread-attribute-key", "thread attribute value");
        }


		{
    		System.out.println("Retrieving data from application context");
    		ApplicationContext appCtx = ApplicationContext.getInstance();
    		String key = "application-attribute-key";
    		Object value = appCtx.get(key);
    		System.out.println(key + ":" + value);
        }
		{
    		System.out.println("Retrieving data from session context");
            String sessionId = "1";
    		SessionContext sessionCtx = SessionContext.getInstance(sessionId);
    		String key = "session-attribute-key";
    		Object value = sessionCtx.get(key);
    		System.out.println(key + ":" + value);

    		System.out.println("Deleting session context");
    		SessionContext.deleteInstance(sessionId);

            sessionCtx = SessionContext.getInstance(sessionId);
    		value = sessionCtx.get(key);
    		System.out.println(key + ":" + value);
        }
		{
    		System.out.println("Retrieving data from thread context");
    		ThreadContext threadCtx = ThreadContext.getInstance();
    		String key = "thread-attribute-key";
    		Object value = threadCtx.get(key);
    		System.out.println(key + ":" + value);

    		System.out.println("Deleting thread context");
    		ThreadContext.deleteInstance();

            threadCtx = ThreadContext.getInstance();
    		value = threadCtx.get(key);
    		System.out.println(key + ":" + value);
        }

		System.out.println("Test application ending");
	}

}
