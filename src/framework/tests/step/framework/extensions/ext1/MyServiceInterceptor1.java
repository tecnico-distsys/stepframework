package step.framework.extensions.ext1;

import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.ServiceInterceptorException;
import step.framework.extensions.ServiceInterceptorParameter;

public class MyServiceInterceptor1 implements ServiceInterceptor {

    public void interceptBefore(ServiceInterceptorParameter param)
    throws ServiceInterceptorException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ interceptBefore(" +
                           param +
                           ")");
        */
    }

    public void interceptAfter(ServiceInterceptorParameter param)
    throws ServiceInterceptorException {
        /*
        System.out.println(this.getClass().getSimpleName() +
                           " @ interceptAfter(" +
                           param +
                           ")");
        */
    }

}
