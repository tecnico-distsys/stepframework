package step.framework.perf.monitor.hibernate;

import java.io.*;
import java.util.*;

import org.hibernate.*;
import org.hibernate.event.*;
import org.hibernate.type.*;

import org.apache.commons.logging.*;

import org.perf4j.*;

import step.framework.perf.monitor.*;


/**
 *  This class implements Hibernate Event Listener interfaces.
 *  Its purpose is to monitor the data layer of
 *  a STEP application.<br />
 *  <br />
 *  It is an alternative to an Hibernate Interceptor.<br />
 *  <br />
 *  It must be configured in the event listener section of
 *  the hibernate-configuration file.<br />
 *  <br />
 */
public class PerfHibernateListener
    implements PreInsertEventListener, PreLoadEventListener, PreUpdateEventListener, PreDeleteEventListener,
        PreCollectionRecreateEventListener, PreCollectionUpdateEventListener, PreCollectionRemoveEventListener,
        PostInsertEventListener, PostLoadEventListener, PostUpdateEventListener, PostDeleteEventListener,
        PostCollectionRecreateEventListener, PostCollectionUpdateEventListener, PostCollectionRemoveEventListener {

    /** Logging */
    private static Log log = LogFactory.getLog(PerfHibernateListener.class);


    //
    //  Listeners implementation
    //

    public boolean onPreInsert(PreInsertEvent event) {
        log.trace("onPreInsert");
        StopWatchHelper.getThreadStopWatch("hibernate").start("hibernate.insert");
        // do not veto operation
        return false;
    }

    public void onPreLoad(PreLoadEvent event) {
        log.trace("onPreLoad");
        StopWatchHelper.getThreadStopWatch("hibernate").start("hibernate.load");
    }

    public boolean onPreUpdate(PreUpdateEvent event) {
        log.trace("onPreUpdate");
        StopWatchHelper.getThreadStopWatch("hibernate").start("hibernate.update");
        // do not veto operation
        return false;
    }

    public boolean onPreDelete(PreDeleteEvent event) {
        log.trace("onPreDelete");
        StopWatchHelper.getThreadStopWatch("hibernate").start("hibernate.delete");
        // do not veto operation
        return false;
    }

    public void onPreRecreateCollection(PreCollectionRecreateEvent event) {
        log.trace("onPreRecreateCollection");
        StopWatchHelper.getThreadStopWatch("hibernate").start("hibernate.collection-recreate");
    }

	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
        log.trace("onPreUpdateCollection");
        StopWatchHelper.getThreadStopWatch("hibernate").start("hibernate.collection-update");
    }

	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
        log.trace("onPreRemoveCollection");
        StopWatchHelper.getThreadStopWatch("hibernate").start("hibernate.collection-remove");
    }


    public void onPostInsert(PostInsertEvent event) {
        log.trace("onPostInsert");
        StopWatchHelper.getThreadStopWatch("hibernate").stop("hibernate.insert");
    }

    public void onPostLoad(PostLoadEvent event) {
        log.trace("onPostLoad");
        StopWatchHelper.getThreadStopWatch("hibernate").stop("hibernate.load");
    }

    public void onPostUpdate(PostUpdateEvent event) {
        log.trace("onPostUpdate");
        StopWatchHelper.getThreadStopWatch("hibernate").stop("hibernate.update");
    }

    public void onPostDelete(PostDeleteEvent event) {
        log.trace("onPostDelete");
        StopWatchHelper.getThreadStopWatch("hibernate").stop("hibernate.delete");
    }


	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
        log.trace("onPostRecreateCollection");
        StopWatchHelper.getThreadStopWatch("hibernate").stop("hibernate.collection-recreate");
    }

	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        log.trace("onPostUpdateCollection");
        StopWatchHelper.getThreadStopWatch("hibernate").stop("hibernate.collection-update");
    }

	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
        log.trace("onPostRemoveCollection");
        StopWatchHelper.getThreadStopWatch("hibernate").stop("hibernate.collection-remove");
    }

}
