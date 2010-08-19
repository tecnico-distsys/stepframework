package step.framework.context;

import java.util.HashMap;
import java.util.Map;

/**
 * ThreadContext has thread scope.
 * Factory methods provide access to current or a specific thread context.
 */
public class ThreadContext extends ContextImpl implements Context {

	private static final long serialVersionUID = 1L;

    // Private constructor prevents instantiation from other classes
    private ThreadContext() {
        super();
    }

    /**
    * SingletonHolder extends ThreadLocal so there is one context per thread
    */
    private static class SingletonHolder extends ThreadLocal<ThreadContext> {
        @Override protected ThreadContext initialValue() {
            return new ThreadContext();
        }
    }

    /** per thread singleton holder */
    private static SingletonHolder singletonHolder = new SingletonHolder();


    /**
     *  Get context for current thread.
     */
    public static ThreadContext getInstance() {
        return singletonHolder.get();
    }

    /**
     *  Delete the current thread context.
     */
    public static ThreadContext deleteInstance() {
        ThreadContext tc = singletonHolder.get();
        singletonHolder.remove();
        return tc;
    }

}
