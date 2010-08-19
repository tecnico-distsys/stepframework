package step.framework.perf.monitor.layer;

import java.io.*;
import java.util.*;

import org.hibernate.*;
import org.hibernate.event.*;
import org.hibernate.type.*;


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
    //
    //  Listeners implementation
    //

    public boolean onPreInsert(PreInsertEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        // do not veto operation
        return false;
    }

    public void onPreLoad(PreLoadEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
    }

    public boolean onPreUpdate(PreUpdateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        // do not veto operation
        return false;
    }

    public boolean onPreDelete(PreDeleteEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
        // do not veto operation
        return false;
    }

    public void onPreRecreateCollection(PreCollectionRecreateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
    }

	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
    }

	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate");
    }


    public void onPostInsert(PostInsertEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate");
    }

    public void onPostLoad(PostLoadEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate");
    }

    public void onPostUpdate(PostUpdateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate");
    }

    public void onPostDelete(PostDeleteEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate");
    }


	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate");
    }

	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate");
    }

	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate");
    }

}
