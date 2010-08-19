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
public class PerfHibernateListenerDetail
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
        monitor.enter("hibernate.insert");
        // do not veto operation
        return false;
    }

    public void onPreLoad(PreLoadEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.load");
    }

    public boolean onPreUpdate(PreUpdateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.update");
        // do not veto operation
        return false;
    }

    public boolean onPreDelete(PreDeleteEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.delete");
        // do not veto operation
        return false;
    }

    public void onPreRecreateCollection(PreCollectionRecreateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.collection-recreate");
    }

	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.collection-update");
    }

	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.enter("hibernate.collection-remove");
    }


    public void onPostInsert(PostInsertEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate.insert");
    }

    public void onPostLoad(PostLoadEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate.load");
    }

    public void onPostUpdate(PostUpdateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate.update");
    }

    public void onPostDelete(PostDeleteEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate.delete");
    }


	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate.collection-recreate");
    }

	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate.collection-update");
    }

	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
        PerfLayerMonitor monitor = MonitorHelper.get();
        monitor.exit("hibernate.collection-remove");
    }

}
