package step.extension.errors;

import java.util.Properties;

import step.framework.domain.DomainException;
import step.framework.oldextensions.ServiceInterceptor;
import step.framework.oldextensions.ServiceInterceptorException;
import step.framework.oldextensions.ServiceInterceptorParameter;


/**
 *  This is the Errors extension's service interceptor.
 *  If properly configured, it is invoked before and after a service execution.
 */
public class ErrorsServiceInterceptor implements ServiceInterceptor {

    public void interceptBefore(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {

        Properties extConfig = param.getExtension().getConfig();
        String exceptionClassName = extConfig.getProperty("service-interceptor.before.throw");

        if(exceptionClassName != null && exceptionClassName.trim().length() > 0) {
            try {
                ThrowExceptionUtil.throwException(exceptionClassName, "interceptBefore");
            } catch(DomainException de) {
                throw de;
            } catch(ServiceInterceptorException sie) {
                throw sie;
            } catch(RuntimeException rte) {
                throw rte;
            } catch(Exception e) {
                System.out.println("exception type not declared in throws; wrapping exception in runtime exception");
                throw new RuntimeException(e);
            }
        }
    }

    public void interceptAfter(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {

        Properties extConfig = param.getExtension().getConfig();
        String exceptionClassName = extConfig.getProperty("service-interceptor.after.throw");

        if(exceptionClassName != null && exceptionClassName.trim().length() > 0) {
            try {
                ThrowExceptionUtil.throwException(exceptionClassName, "interceptAfter");
            } catch(DomainException de) {
                throw de;
            } catch(ServiceInterceptorException sie) {
                throw sie;
            } catch(RuntimeException rte) {
                throw rte;
            } catch(Exception e) {
                System.out.println("exception type not declared in throws; wrapping exception in runtime exception");
                throw new RuntimeException(e);
            }
        }
    }

}
