package step.framework.perf.monitor.event;

import java.io.*;
import java.util.*;

import step.framework.context.*;
import step.framework.domain.*;
import step.framework.extensions.*;
import step.framework.service.*;


/**
 *  This class implements the STEP Extensions Service Interceptor interface.
 *  Its purpose is intercept the service (business layer) invocations.
 */
public class PerfServiceInterceptor extends ServiceInterceptorBase {

    //
    //  ServiceInterceptor
    //

    @Override
    public void interceptBefore(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException {

        PerfEventMonitor monitor = MonitorHelper.get();

        Object service = param.getServiceInstance();
        Class serviceClass = service.getClass();
        monitor.context("className", serviceClass.getName());
        monitor.event("enter-si");
    }

    @Override
    public void interceptAfter(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException {

        // do nothing. Exit is made in interceptFinallyAfter
    }

    @Override
    public void interceptFinallyAfter(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException {

        MonitorHelper.get().event("exit-si");
    }

}
