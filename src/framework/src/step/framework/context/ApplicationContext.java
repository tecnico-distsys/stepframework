package step.framework.context;

/**
 * ApplicationContext has application scope.
 * Factory methods provide access to the single context with application scope.
 */
public class ApplicationContext extends ContextImpl implements Context {

	private static final long serialVersionUID = 1L;

	/**
     * Private constructor prevents construction outside this class.
     */
    private ApplicationContext() {
        super();
    }

    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance()
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class SingletonHolder {
        private static final ApplicationContext INSTANCE = new ApplicationContext();
    }

    /**
     *  Obtain application context.
     */
    public static ApplicationContext getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
