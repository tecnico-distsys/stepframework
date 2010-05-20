package step.framework.oldextensions;


/**
 *  An instance of this class is provided as parameter to each service interceptor
 *  and enables it to access configuration and context data.
 */
public class ServiceInterceptorParameter extends InterceptorParameter {

    //
    //  Members
    //
    private Object serviceInstance;


    //
    //  Constructors
    //
    ServiceInterceptorParameter(ExtensionEngine engine,
                                Extension extension,
                                Object serviceInstance) {
        super(engine,extension);
        ExtensionsUtil.throwIllegalArgIfNull(serviceInstance,
            "instance for service interceptor parameter can't be null");
        this.serviceInstance = serviceInstance;
    }


    //
    //  Context access methods
    //

    /**
     *  Access the service instance that is being intercepted
     */
    public Object getServiceInstance() {
        return this.serviceInstance;
    }

}
