package step.framework.perf.monitor.layer;

import java.io.*;
import java.util.*;
import java.sql.Connection;

import org.hibernate.*;
import org.hibernate.metadata.*;
import org.hibernate.engine.FilterDefinition;

import step.framework.context.*;


/**
 *  Hibernate helper methods.
 */
class PerfHibernateHelper {

    static Connection wrapConnection(Connection connection) {
        // wrap connection ?
        return connection;
    }

    static SessionFactory wrapSessionFactory(SessionFactory sessionFactory) {
        if (sessionFactory instanceof PerfHibernateSessionFactory)
            return sessionFactory;
        else
            return new PerfHibernateSessionFactory(sessionFactory);
    }

    static Session wrapSession(Session session) {
        if (session instanceof PerfHibernateSession)
            return session;
        else
            return new PerfHibernateSession(session);
    }

    static StatelessSession wrapStatelessSession(StatelessSession statelessSession) {
        // wrap stateless session ?
        return statelessSession;
    }

    static org.hibernate.classic.Session wrapClassicSession(org.hibernate.classic.Session session) {
        if (session instanceof PerfHibernateSession)
            return session;
        else
            return new PerfHibernateSession(session);
    }

    static Criteria wrapCriteria(Criteria criteria) {
        if (criteria instanceof PerfHibernateCriteria)
            return criteria;
        else
            return new PerfHibernateCriteria(criteria);
    }

    static ScrollableResults wrapScrollableResults(ScrollableResults scrollableResults) {
        // wrap scrollable results ?
        return scrollableResults;
    }

    static Transaction wrapTransaction(Transaction transaction) {
        if (transaction instanceof PerfHibernateTransaction)
            return transaction;
        else
            return new PerfHibernateTransaction(transaction);
    }

    static FilterDefinition wrapFilterDefinition(FilterDefinition filterDefinition) {
        // wrap filter definition ?
        return filterDefinition;
    }

    static Set wrapFilterDefinitionSet(Set filterDefinitionSet) {
        // wrap filter definition set?
        return filterDefinitionSet;
    }

    static Filter wrapFilter(Filter filter) {
        // wrap filter ?
        return filter;
    }

    static Query wrapQuery(Query query) {
        // wrap query ?
        return query;
    }

    static SQLQuery wrapSQLQuery(SQLQuery sqlQuery) {
        // wrap sql query ?
        return sqlQuery;
    }

}
