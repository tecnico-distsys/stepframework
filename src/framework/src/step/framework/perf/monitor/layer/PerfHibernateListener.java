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
        MonitorHelper.get().enter("hibernate.insert");
        // do not veto operation
        return false;
    }

    public void onPreLoad(PreLoadEvent event) {
        MonitorHelper.get().enter("hibernate.load");
    }

    public boolean onPreUpdate(PreUpdateEvent event) {
        MonitorHelper.get().enter("hibernate.update");
        // do not veto operation
        return false;
    }

    public boolean onPreDelete(PreDeleteEvent event) {
        MonitorHelper.get().enter("hibernate.delete");
        // do not veto operation
        return false;
    }

    public void onPreRecreateCollection(PreCollectionRecreateEvent event) {
        MonitorHelper.get().enter("hibernate.collection-recreate");
    }

	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
        MonitorHelper.get().enter("hibernate.collection-update");
    }

	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
        MonitorHelper.get().enter("hibernate.collection-remove");
    }


    public void onPostInsert(PostInsertEvent event) {
        MonitorHelper.get().exit("hibernate.insert");
    }

    public void onPostLoad(PostLoadEvent event) {
        MonitorHelper.get().exit("hibernate.load");
    }

    public void onPostUpdate(PostUpdateEvent event) {
        MonitorHelper.get().exit("hibernate.update");
    }

    public void onPostDelete(PostDeleteEvent event) {
        MonitorHelper.get().exit("hibernate.delete");
    }


	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
        MonitorHelper.get().exit("hibernate.collection-recreate");
    }

	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        MonitorHelper.get().exit("hibernate.collection-update");
    }

	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
        MonitorHelper.get().exit("hibernate.collection-remove");
    }

}
