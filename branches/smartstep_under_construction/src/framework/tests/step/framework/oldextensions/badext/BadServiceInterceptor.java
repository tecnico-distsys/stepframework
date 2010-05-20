package step.framework.oldextensions.badext;

import step.framework.oldextensions.ServiceInterceptorParameter;

public class BadServiceInterceptor /*implements ServiceInterceptor*/ {

    public Object before(ServiceInterceptorParameter param) throws Exception {
        System.out.println(this.getClass().getSimpleName() +
                           " @ before(" +
                           param +
                           ")");
        return null;
    }

    public Object after(ServiceInterceptorParameter param) throws Exception {
        System.out.println(this.getClass().getSimpleName() +
                           " @ after(" +
                           param +
                           ")");
        return null;
    }

}
