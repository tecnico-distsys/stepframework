package step.extension.errors;

import step.framework.domain.DomainException;
import step.framework.extensions.InterceptorException;
import step.framework.extensions.ServiceInterceptor;
import step.framework.service.Service;

/**
 *  This is the Errors extension's service interceptor.
 *  If properly configured, it is invoked before and after a service execution.
 */
public class ErrorsServiceInterceptor extends ServiceInterceptor {

    @SuppressWarnings("unchecked")
	public void interceptBefore(Service service) throws DomainException, InterceptorException
    {
        String exceptionClassName = (String) getExtension().getProperty("service-interceptor.before.throw");

        if(exceptionClassName != null && exceptionClassName.trim().length() > 0) {
            try {
                ThrowExceptionUtil.throwException(exceptionClassName, "interceptBefore");
            } catch(DomainException de) {
                throw de;
            } catch(InterceptorException sie) {
                throw sie;
            } catch(RuntimeException rte) {
                throw rte;
            } catch(Exception e) {
                System.out.println("exception type not declared in throws; wrapping exception in runtime exception");
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
	public void interceptAfter(Service service) throws DomainException, InterceptorException
    {
        String exceptionClassName = (String) getExtension().getProperty("service-interceptor.after.throw");

        if(exceptionClassName != null && exceptionClassName.trim().length() > 0) {
            try {
                ThrowExceptionUtil.throwException(exceptionClassName, "interceptAfter");
            } catch(DomainException de) {
                throw de;
            } catch(InterceptorException sie) {
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
