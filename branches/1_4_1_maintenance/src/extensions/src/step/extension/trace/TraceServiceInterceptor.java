package step.extension.trace;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Map;

import step.framework.domain.DomainException;
import step.framework.extensions.*;
import step.framework.service.Service;

/**
 *  This is the Trace extension's service interceptor.
 *  If properly configured, it is invoked before and after a service execution.
 */
public class TraceServiceInterceptor extends ServiceInterceptorBase {

    /**
     *   Prints the values of the service atributes before the service's execution.
     */
    @Override
    public void interceptBefore(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {
        trace(param);
    }

    /**
     *   Prints the values of the service atributes after the service's execution.
     */
    @Override
    public void interceptAfter(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {
        trace(param);
    }

    private void trace(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {
        // where to write the information
        PrintStream out = getPrintStream(param);

        // the service to intercept
        Object service = param.getServiceInstance();

        // ok, lets print the service's field
        Class serviceClass = service.getClass();
        out.println("Tracing service: " + serviceClass);

        Field[] declaredFields = serviceClass.getDeclaredFields();
        for (Field f : declaredFields) {
            f.setAccessible(true);
            out.print("Field: " + f.getName() + " = ");
            try {
                out.println(f.get(service));
            } catch (IllegalAccessException e) {
                out.println("<cannot access value>");
            }
        }

        try {
            Field returnValueField = Service.class.getDeclaredField("returnValue");
            returnValueField.setAccessible(true);
            out.println("Return value: " + returnValueField.get(service));
        } catch (NoSuchFieldException e) {
            out.println("Services are expected to have a field named \"returnValue\"!!!");
        } catch (IllegalAccessException e) {
            out.println("<cannot access return value>");
        }
    }

    /** Get the configured output print stream */
    private PrintStream getPrintStream(ServiceInterceptorParameter param) {
        Map<String,Object> extContext = param.getExtension().getContext();
        synchronized(extContext) {
            return (PrintStream) extContext.get("output");
        }
    }

}
