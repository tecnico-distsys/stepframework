package step.extension.trace;

import java.io.PrintStream;
import java.lang.reflect.Field;

import step.framework.domain.DomainException;
import step.framework.extensions.Extension;
import step.framework.extensions.InterceptorException;
import step.framework.extensions.ServiceInterceptor;
import step.framework.service.Service;

/**
 *  This is the Trace extension's service interceptor.
 *  If properly configured, it is invoked before and after a service execution.
 */
public class TraceServiceInterceptor extends ServiceInterceptor {

    /**
     *   Prints the values of the service atributes before the service's execution.
     */
    @SuppressWarnings("unchecked")
	public void interceptBefore(Service service) throws DomainException, InterceptorException
	{
        trace(service);
    }

    /**
     *   Prints the values of the service atributes after the service's execution.
     */
    @SuppressWarnings("unchecked")
	public void interceptAfter(Service service) throws DomainException, InterceptorException
    {
        trace(service);
    }

    @SuppressWarnings("unchecked")
	private void trace(Service service) throws DomainException, InterceptorException
    {
        // where to write the information
        PrintStream out = getPrintStream();

        // ok, lets print the service's field
        Class<?> serviceClass = service.getClass();
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
    private PrintStream getPrintStream()
    {
        Extension ext = getExtension();
        synchronized(ext) {
            return (PrintStream) ext.getProperty("output.stream");
        }
    }

}
