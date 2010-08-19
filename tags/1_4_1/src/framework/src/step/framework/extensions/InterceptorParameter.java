package step.framework.extensions;


/**
 *  An instance of a subclass of this class is provided as parameter
 *  to each interceptor and enables it to access configuration and context data.
 */
public abstract class InterceptorParameter {

    //
    //  Members
    //
    private ExtensionEngine engine;
    private Extension extension;

    //
    //  Constructors
    //
    InterceptorParameter(ExtensionEngine engine, Extension extension) {
        super();
        ExtensionsUtil.throwIllegalArgIfNull(engine,
            "engine for interceptor parameter can't be null");
        ExtensionsUtil.throwIllegalArgIfNull(extension,
            "extension for interceptor parameter can't be null");
        this.engine = engine;
        this.extension = extension;
    }


    //
    //  Context access methods
    //

    /**
     *  Access the extension engine object
     */
    public ExtensionEngine getExtensionEngine() {
        return this.engine;
    }

    /**
     *  Access the extension object
     */
    public Extension getExtension() {
        return this.extension;
    }

}
