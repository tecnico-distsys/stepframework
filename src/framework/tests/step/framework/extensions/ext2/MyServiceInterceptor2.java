package step.framework.extensions.ext2;

import step.framework.domain.DomainException;

import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.ServiceInterceptorException;
import step.framework.extensions.ServiceInterceptorParameter;

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
