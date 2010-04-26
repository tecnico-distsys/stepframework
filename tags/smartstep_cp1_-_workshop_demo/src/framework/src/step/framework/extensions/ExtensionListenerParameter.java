package step.framework.extensions;



/**
 *  An instance of this class is provided as parameter
 *  to each extension listener
 *  and enables it to access configuration and context data.
 */
public class ExtensionListenerParameter {

    //
    //  Members
    //
    private ExtensionEngine engine;
    private Extension extension;

    //
    //  Constructors
    //
    ExtensionListenerParameter(ExtensionEngine engine, Extension extension) {
        super();
        ExtensionsUtil.throwIllegalArgIfNull(engine,
            "engine for extension listener parameter can't be null");
        ExtensionsUtil.throwIllegalArgIfNull(extension,
            "extension for extension listener parameter can't be null");
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
