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
        MonitorHelper.get().enter("hibernate");
        // do not veto operation
        return false;
    }

    public void onPreLoad(PreLoadEvent event) {
        MonitorHelper.get().enter("hibernate");
    }

    public boolean onPreUpdate(PreUpdateEvent event) {
        MonitorHelper.get().enter("hibernate");
        // do not veto operation
        return false;
    }

    public boolean onPreDelete(PreDeleteEvent event) {
        MonitorHelper.get().enter("hibernate");
        // do not veto operation
        return false;
    }

    public void onPreRecreateCollection(PreCollectionRecreateEvent event) {
        MonitorHelper.get().enter("hibernate");
    }

	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
        MonitorHelper.get().enter("hibernate");
    }

	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
        MonitorHelper.get().enter("hibernate");
    }


    public void onPostInsert(PostInsertEvent event) {
        MonitorHelper.get().exit("hibernate");
    }

    public void onPostLoad(PostLoadEvent event) {
        MonitorHelper.get().exit("hibernate");
    }

    public void onPostUpdate(PostUpdateEvent event) {
        MonitorHelper.get().exit("hibernate");
    }

    public void onPostDelete(PostDeleteEvent event) {
        MonitorHelper.get().exit("hibernate");
    }


	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
        MonitorHelper.get().exit("hibernate");
    }

	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        MonitorHelper.get().exit("hibernate");
    }

	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
        MonitorHelper.get().exit("hibernate");
    }

}
