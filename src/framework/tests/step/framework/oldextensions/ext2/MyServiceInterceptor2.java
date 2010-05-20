package step.framework.oldextensions.ext2;

import step.framework.domain.DomainException;

import step.framework.oldextensions.ServiceInterceptor;
import step.framework.oldextensions.ServiceInterceptorException;
import step.framework.oldextensions.ServiceInterceptorParameter;

public class MyServiceInterceptor2 implements ServiceInterceptor {

    public void interceptBefore(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ interceptBefore(" +
                           param +
                           ")");
        */
    }

    public void interceptAfter(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ interceptAfter(" +
                           param +
                           ")");
        */
    }

}
