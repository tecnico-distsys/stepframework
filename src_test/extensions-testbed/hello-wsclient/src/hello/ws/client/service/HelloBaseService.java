package hello.ws.client.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hello.exception.ws.HelloWSException;
import step.framework.service.Service;

public abstract class HelloBaseService<T> extends Service<T> {
	protected Log log;

	public HelloBaseService() {
		this.log = LogFactory.getLog(this.getClass());
	}

	// this class may contain auxiliary methods used in all remote invocation
	// services

	@Override
	protected abstract T action() throws HelloWSException;

}
