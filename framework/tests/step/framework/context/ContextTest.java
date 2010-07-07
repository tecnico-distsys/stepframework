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
    public void testThreadContext() {
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

}
