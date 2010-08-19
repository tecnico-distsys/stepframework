package step.framework.context;

import java.util.HashMap;
import java.util.Map;

/**
 * SessionContext has session scope.
 * Factory methods provide access to a session using an externally generated id.
 */
public class SessionContext extends ContextImpl implements Context {

	private static final long serialVersionUID = 1L;

	/**
     * Private constructor prevents construction outside this class.
     */
    private SessionContext() {
        super();
    }

    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance()
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class SingletonHolder {
        private static final Map<String,SessionContext> INSTANCE = new HashMap<String,SessionContext>();
    }

    /**
     * Get the session context for the session with the specified id.
     */
    public static synchronized SessionContext getInstance(String sessionId) {
        SessionContext sessionContext = SingletonHolder.INSTANCE.get(sessionId);
        if(sessionContext == null) {
            sessionContext = new SessionContext();
            SingletonHolder.INSTANCE.put(sessionId, sessionContext);
        }
        return sessionContext;
    }

    /**
     * Delete the session context for the session with the specified id.
     */
    public static synchronized SessionContext deleteInstance(String sessionId) {
        return SingletonHolder.INSTANCE.remove(sessionId);
    }

}
