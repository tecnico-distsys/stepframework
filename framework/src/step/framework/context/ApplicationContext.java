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
     * Single application context.
     */
    private static final ApplicationContext applicationContext = new ApplicationContext();

    /**
     * Obtain application context.
     */
    public static synchronized ApplicationContext getInstance() {
        return applicationContext;
    }

}
