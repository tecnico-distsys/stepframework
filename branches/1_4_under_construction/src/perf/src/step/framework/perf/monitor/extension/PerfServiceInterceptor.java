package step.framework.perf.monitor.extension;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.*;

import org.perf4j.*;

import step.framework.perf.monitor.*;

import step.framework.context.*;
import step.framework.domain.*;
import step.framework.extensions.*;
import step.framework.service.*;


/**
 *  This class implements the STEP Extensions Service Interceptor interface.
 *  Its purpose is intercept the service (business layer) invocations.
 */
public class PerfServiceInterceptor extends ServiceInterceptorBase {

    /** Logging */
    private static Log log = LogFactory.getLog(PerfServiceInterceptor.class);


    //
    //  ServiceInterceptor
    //

    @Override
    public void interceptBefore(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException {

        log.trace("interceptBefore");
        StopWatchHelper.getThreadStopWatch("si").start("si");
    }

    @Override
    public void interceptAfter(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException {

        log.trace("interceptAfter");
    }

    @Override
    public void interceptFinallyAfter(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException {

        log.trace("interceptFinallyAfter");
        StopWatchHelper.getThreadStopWatch("si").stop("si");
    }

}
