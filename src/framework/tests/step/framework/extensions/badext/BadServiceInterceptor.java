package step.framework.extensions.badext;

/*import step.framework.extensions.ServiceInterceptor;*/
import step.framework.extensions.ServiceInterceptorParameter;

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
