package step.framework.context;

import org.junit.*;
import static org.junit.Assert.*;


public class ContextTest {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testApplicationContext() {
        {
            ApplicationContext appCtx = ApplicationContext.getInstance();
            appCtx.put("application-attribute-key", "application attribute value");
        }

        {
            ApplicationContext appCtx = ApplicationContext.getInstance();
            String key = "application-attribute-key";
            Object value = appCtx.get(key);
            assertEquals("application attribute value", value);

            appCtx.remove(key);
            value = appCtx.get(key);
            assertNull(value);
        }
    }

    @Test
    public void testSessionContext() {
        {
            String sessionId = "1";
            SessionContext sessionCtx = SessionContext.getInstance(sessionId);
            sessionCtx.put("session-attribute-key", "session attribute value");
        }

        {
            String sessionId = "1";
            SessionContext sessionCtx = SessionContext.getInstance(sessionId);
            String key = "session-attribute-key";
            Object value = sessionCtx.get(key);
            assertEquals("session attribute value", value);

            SessionContext.deleteInstance(sessionId);

            sessionCtx = SessionContext.getInstance(sessionId);
            value = sessionCtx.get(key);
            assertNull(value);
        }
    }

    @Test
    public void testSingleThreadContext() {
        {
            ThreadContext threadCtx = ThreadContext.getInstance();
            threadCtx.put("thread-attribute-key", "thread attribute value");
        }

        {
            ThreadContext threadCtx = ThreadContext.getInstance();
            String key = "thread-attribute-key";
            Object value = threadCtx.get(key);
            assertEquals("thread attribute value", value);

            ThreadContext.deleteInstance();

            threadCtx = ThreadContext.getInstance();
            value = threadCtx.get(key);
            assertNull(value);
        }
    }

    @Test
    public void testMultiThreadContext() {
        {
            // place value in current thread
            ThreadContext threadCtx = ThreadContext.getInstance();
            threadCtx.put("thread-attribute-key", "thread attribute value");
        }

        {
            ThreadContext threadCtx = ThreadContext.getInstance();
            String key = "thread-attribute-key";
            Object value = threadCtx.get(key);
            // attribute is there for current thread
            assertEquals("thread attribute value", value);
        }

        try {
            // start a different thread
            TestThread thread = new TestThread();
            thread.start();
            
            // wait for thread to complete
            thread.join();
            
            Object result = thread.getResult();
            Exception exception = thread.getException();

            assertNull("Thread should not have caught an exception", exception);
            assertNull("Thread context should have been different from current thread's", result);

        } catch(InterruptedException ie) {
            fail("Thread was interrupted");
        } finally {
            ThreadContext.deleteInstance();
        }
    }

    private static class TestThread extends Thread {
        Object argument;
        Object result;
    
        public TestThread() {
        }
    
        public TestThread(Object argument) {
            this.argument = argument;
        }
    
        public Object getResult() {
            return this.result;
        }
    
        public Exception getException() {
            if(this.result instanceof Exception)
                return (Exception) this.result;
            else
                return null;
        }
    
        public void run() {
            try {
                ThreadContext threadCtx = ThreadContext.getInstance();
                String key = "thread-attribute-key";
                Object value = threadCtx.get(key);
                this.result = value;
            } catch (Exception e) {
                this.result = e;
            }
        }
    }



}
