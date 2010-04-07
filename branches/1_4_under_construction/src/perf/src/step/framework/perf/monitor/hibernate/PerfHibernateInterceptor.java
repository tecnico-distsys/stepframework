package step.framework.perf.monitor.hibernate;

import java.io.*;
import java.util.*;

import org.hibernate.*;
import org.hibernate.type.*;

import org.apache.commons.logging.*;

import org.perf4j.*;

import step.framework.perf.monitor.*;


/**
 *  This class implements the Hibernate Interceptor interface.
 *  Its purpose is intercept and monitor the data layer of
 *  a STEP application.<br />
 *  <br />
 *  An Hibernate Interceptor cannot be configured declaratively
 *  in the hibernate-configuration file.<br />
 *  It must be configured programmatically after the .configure() method.<br />
 *  <br />
 */
public class PerfHibernateInterceptor extends EmptyInterceptor implements Interceptor {

    /** Logging */
    private static Log log = LogFactory.getLog(PerfHibernateInterceptor.class);


    //
    //  Interceptor
    //

    public void afterTransactionBegin(Transaction tx) {
        log.trace("afterTransactionBegin");
    }

    public void afterTransactionCompletion(Transaction tx) {
        log.trace("afterTransactionCompletion");
    }

    public void beforeTransactionCompletion(Transaction tx) {
        log.trace("beforeTransactionCompletion");
    }

    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        log.trace("findDirty");
        return super.findDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    public Object getEntity(String entityName, Serializable id) {
        log.trace("getEntity");
        return super.getEntity(entityName, id);
    }

    public String getEntityName(Object object) {
        log.trace("getEntityName");
        return super.getEntityName(object);
    }

    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
        log.trace("instantiate");
        return super.instantiate(entityName, entityMode, id);
    }

    public Boolean isTransient(Object entity) {
        log.trace("isTransient");
        return super.isTransient(entity);
    }

    public void onCollectionRecreate(Object collection, Serializable key) {
        log.trace("onCollectionRecreate");
    }

    public void onCollectionRemove(Object collection, Serializable key) {
        log.trace("onCollectionRemove");
    }

    public void onCollectionUpdate(Object collection, Serializable key) {
        log.trace("onCollectionUpdate");
    }

    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        log.trace("onDelete");
    }

    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        log.trace("onFlushDirty");
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        log.trace("onLoad");
        return super.onLoad(entity, id, state, propertyNames, types);
    }

    public String onPrepareStatement(String sql) {
        log.trace("onPrepareStatement");
        return super.onPrepareStatement(sql);
    }

    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        log.trace("onSave");

        if(log.isTraceEnabled()) {
            log.trace("Saving the persistent Object " + entity.getClass() + " with Id " + id);
        }

        return super.onSave(entity, id, state, propertyNames, types);
    }

    public void postFlush(Iterator entities) {
        log.trace("postFlush");
    }

    public void preFlush(Iterator entities) {
        log.trace("preFlush");
    }


}
