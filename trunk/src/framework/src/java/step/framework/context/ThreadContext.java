package step.framework.context;

import java.util.HashMap;
import java.util.Map;

/**
 * ThreadContext has thread scope.
 * Factory methods provide access to current or a specific thread context.
 */
public class ThreadContext extends ContextImpl implements Context {

    /**
     * Private constructor prevents construction outside this class.
     */
    private ThreadContext() {
        super();
    }

    /**
     * Thread context collection.
     */
    private static final Map<String,ThreadContext> threadContextCollection = new HashMap<String,ThreadContext>();

    /**
     * Get context for current thread.
     */
    public static synchronized ThreadContext getInstance() {
        return getInstance(Thread.currentThread());
    }

    /**
     * Get context for specified thread.
     */
    public static synchronized ThreadContext getInstance(Thread thread) {
        String threadKey = getThreadKey(thread);
        ThreadContext threadContext = threadContextCollection.get(threadKey);
        if(threadContext == null) {
            threadContext = new ThreadContext();
            threadContextCollection.put(threadKey, threadContext);
        }
        return threadContext;
    }

    /**
     * Delete the current thread context.
     */
    public static synchronized ThreadContext deleteInstance() {
        return deleteInstance(Thread.currentThread());
    }

    /**
     * Delete context for specified thread.
     */
    public static synchronized ThreadContext deleteInstance(Thread thread) {
        String threadKey = getThreadKey(thread);
        return threadContextCollection.remove(threadKey);
    }

    /**
     * Generate a string representation of a thread id
     */
    private static String getThreadKey(Thread thread) {
        long id = thread.getId();
        return Long.toString(id);
    }

}
