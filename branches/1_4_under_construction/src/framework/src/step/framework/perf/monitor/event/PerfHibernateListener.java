package step.framework.perf.monitor.event;

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
        MonitorHelper.get().event("enter-hibernate.insert");
        // do not veto operation
        return false;
    }

    public void onPreLoad(PreLoadEvent event) {
        MonitorHelper.get().event("enter-hibernate.load");
    }

    public boolean onPreUpdate(PreUpdateEvent event) {
        MonitorHelper.get().event("enter-hibernate.update");
        // do not veto operation
        return false;
    }

    public boolean onPreDelete(PreDeleteEvent event) {
        MonitorHelper.get().event("enter-hibernate.delete");
        // do not veto operation
        return false;
    }

    public void onPreRecreateCollection(PreCollectionRecreateEvent event) {
        MonitorHelper.get().event("enter-hibernate.collection-recreate");
    }

	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
        MonitorHelper.get().event("enter-hibernate.collection-update");
    }

	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
        MonitorHelper.get().event("enter-hibernate.collection-remove");
    }


    public void onPostInsert(PostInsertEvent event) {
        MonitorHelper.get().event("exit-hibernate.insert");
    }

    public void onPostLoad(PostLoadEvent event) {
        MonitorHelper.get().event("exit-hibernate.load");
    }

    public void onPostUpdate(PostUpdateEvent event) {
        MonitorHelper.get().event("exit-hibernate.update");
    }

    public void onPostDelete(PostDeleteEvent event) {
        MonitorHelper.get().event("exit-hibernate.delete");
    }


	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
        MonitorHelper.get().event("exit-hibernate.collection-recreate");
    }

	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        MonitorHelper.get().event("exit-hibernate.collection-update");
    }

	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
        MonitorHelper.get().event("exit-hibernate.collection-remove");
    }

}
