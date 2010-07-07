package step.framework.extensions;

import java.util.List;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.config.tree.ConfigTree;
import step.framework.exception.ServiceException;


/**
 *  An instance of this class is responsible for
 *  interceping service execution (before and after it happens)
 *  invoking all configured extension service interceptors.
 */
public class ServiceInterceptorManager {

    //
    //  Members
    //
    /** Extension engine */
    ExtensionEngine engine;

    /** logging */
    private Log log = LogFactory.getLog(ServiceInterceptorManager.class);


    //
    //  Constructors
    //
    public ServiceInterceptorManager() {
        engine = ExtensionEngine.getInstance();
    }


    //
    //  Methods called by the Framework's Service
    //

    /**
     *  Intercept service before main action
     */
    public void interceptBeforeService(Object serviceInstance)
    throws ServiceException {
        try {
            log.trace("entering interceptBefore()");
            InterceptConfigData configData = prepareForIntercept(serviceInstance);
            if(configData != null) {
                interceptBefore(serviceInstance, configData);
            }
        } finally {
            log.trace("finally exiting interceptBefore()");
        }
    }

    /**
     *  Intercept service after main action
     */
    public void interceptAfterService(Object serviceInstance)
    throws ServiceException {
        try {
            log.trace("entering interceptAfter()");
            InterceptConfigData configData = prepareForIntercept(serviceInstance);
            if(configData != null) {
                interceptAfter(serviceInstance, configData);
            }
        } finally {
            log.trace("finally exiting interceptAfter()");
        }
    }


    //
    //  Service interception methods
    //

    // Helper method to prepare for interception
    // Returns appropriate intercept config data or null if invocation isn't necessary
    private InterceptConfigData prepareForIntercept(Object serviceInstance) {

        // check argument
        ExtensionsUtil.throwIllegalArgIfNull(serviceInstance,
                                             "service instance can't be null");

        // check if extension engine is enabled
        if(!this.engine.isEnabled()) {
            log.trace("extensions disabled; ignoring service execution");
            return null;
        }

        // extension engine enabled; proceed

        // obtain service instance class name
        String fullClassName = serviceInstance.getClass().getName();
        if(log.isTraceEnabled()) {
            log.trace("service instance full class name: " + fullClassName);
        }

        // get config tree
        ConfigTree<InterceptConfigData> configTree = engine.getInterceptServiceConfigTree();
        if(log.isTraceEnabled()) {
            log.trace("Using configuration tree: " + configTree.getName());
        }

        // the config path is the full class name
        String configPath = fullClassName;

        // find most specific config
        InterceptConfigData configData = configTree.findMostSpecificConfig(configPath);

        // return if no service interception config found
        if(configData == null) {
            log.trace("no service interception configuration found; ignoring service execution");
            return null;
        }

        // intercept config found; proceed

        // log detailed configuration data
        if(log.isTraceEnabled()) {
            log.trace("Service interception config data: " + configData);
        }

        // return
        return configData;
    }


    // Helper method to actually perform before service interception
    private void interceptBefore(Object serviceInstance, InterceptConfigData configData)
    throws ServiceException {

        // log service being intercepted
        if(log.isDebugEnabled()) {
            log.debug("Intercepting service " +
                      serviceInstance.getClass().getName() +
                      " BEFORE its execution");
            //          ------
        }

        // interception loop
        List<Extension> extList = configData.getExtensionList();
        ListIterator<Extension> extListIterator = extList.listIterator();

        while(extListIterator.hasNext()) {
            Extension ext = extListIterator.next();

            // skip disabled extensions
            if(!ext.isEnabled()) {
                if(log.isDebugEnabled()) {
                    log.debug("Skipping " + ext.getId() + " extension because it's disabled");
                }
                continue;
            }

            // invoke extension
            try {
                if(log.isDebugEnabled()) {
                    log.debug("Invoking " + ext.getId() + " extension");
                }

                log.trace("create service interceptor instance");
                ServiceInterceptor interceptor = ext.createServiceInterceptorInstance();

                log.trace("create service interceptor parameter instance");
                ServiceInterceptorParameter param =
                    new ServiceInterceptorParameter(this.engine, ext, serviceInstance);

                log.trace("call service interceptor");
                interceptor.interceptBefore(param);
                //          ---------------

            } catch(ServiceInterceptorException sie) {
                handleServiceInterceptorException(sie);
            } catch(ExtensionException ee) {
                handleExtensionException(ee);
            } catch(RuntimeException rte) {
                handleRuntimeException(rte);
            } // try-catch

        } // while

    } // interceptBefore


    // Helper method to actually perform after service interception
    private void interceptAfter(Object serviceInstance, InterceptConfigData configData)
    throws ServiceException {

        // log service being intercepted
        if(log.isDebugEnabled()) {
            log.debug("Intercepting service " +
                      serviceInstance.getClass().getName() +
                      " AFTER its execution");
            //          -----
        }

        //  interception loop
        List<Extension> extList = configData.getExtensionList();
        // place iterator at the end of list
        ListIterator<Extension> extListIterator = extList.listIterator(extList.size());

        // loop backwards
        while(extListIterator.hasPrevious()) {
            Extension ext = extListIterator.previous();

            // skip disabled extensions
            if(!ext.isEnabled()) {
                if(log.isDebugEnabled()) {
                    log.debug("Skipping " + ext.getId() + " extension because it's disabled");
                }
                continue;
            }

            // invoke extension
            try {
                if(log.isDebugEnabled()) {
                    log.debug("Invoking " + ext.getId() + " extension");
                }

                log.trace("create service interceptor instance");
                ServiceInterceptor interceptor = ext.createServiceInterceptorInstance();

                log.trace("create service interceptor parameter instance");
                ServiceInterceptorParameter param =
                    new ServiceInterceptorParameter(this.engine, ext, serviceInstance);

                log.trace("call service interceptor");
                interceptor.interceptAfter(param);
                //          --------------

            } catch(ServiceInterceptorException sie) {
                handleServiceInterceptorException(sie);
            } catch(ExtensionException ee) {
                handleExtensionException(ee);
            } catch(RuntimeException rte) {
                handleRuntimeException(rte);
            } // try-catch

        } // while

    } // interceptAfter


    // Helper method to handle an extension exception during interception
    private void handleServiceInterceptorException(ServiceInterceptorException sie)
    throws ServiceException {
        log.debug("service interceptor has thrown a service interceptor exception");
        log.debug(sie);
        log.trace("service interceptor exception details", sie);
        log.debug("throwing a service exception");
        throw new ServiceException(sie);
    }

    // Helper method to handle a extension exception during interception
    private void handleExtensionException(ExtensionException ee) {
        log.debug("caught extension exception (that is not a service interceptor exception)");
        log.debug(ee);
        log.trace("extension exception details", ee);
        log.trace("throwing a runtime exception with the extension exception nested inside");
        throw new RuntimeException(ee);
    }

    // Helper method to handle a runtime exception during interception
    private void handleRuntimeException(RuntimeException rte) {
        log.debug("service interceptor has thrown a run-time exception");
        log.debug(rte);
        log.trace("run-time exception details", rte);
        log.trace("rethrowing");
        throw rte;
    }

}
