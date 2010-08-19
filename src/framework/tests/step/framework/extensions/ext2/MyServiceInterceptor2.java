package step.framework.extensions.ext2;

import step.framework.domain.DomainException;

import step.framework.extensions.*;

public class MyServiceInterceptor2 extends ServiceInterceptorBase {

    @Override
    public void interceptBefore(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ interceptBefore(" +
                           param +
                           ")");
        */
    }

    @Override
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
