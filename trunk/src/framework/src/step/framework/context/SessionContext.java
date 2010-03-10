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
     * Session context collection.
     */
    private static final Map<String,SessionContext> sessionContextCollection = new HashMap<String,SessionContext>();

    /**
     * Get the session context for the session with the specified id.
     */
    public static synchronized SessionContext getInstance(String sessionId) {
        SessionContext sessionContext = sessionContextCollection.get(sessionId);
        if(sessionContext == null) {
            sessionContext = new SessionContext();
            sessionContextCollection.put(sessionId, sessionContext);
        }
        return sessionContext;
    }

    /**
     * Delete the session context for the session with the specified id.
     */
    public static synchronized SessionContext deleteInstance(String sessionId) {
        return sessionContextCollection.remove(sessionId);
    }

}
