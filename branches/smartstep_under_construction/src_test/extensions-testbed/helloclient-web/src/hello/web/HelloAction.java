package hello.web;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class HelloAction implements ActionBean {
	protected HelloContext context;
	protected Log log;

	public HelloContext getContext() {
		return context;
	}

	public void setContext(ActionBeanContext context) {
		this.context = (HelloContext) context;
	}

	public HelloAction() {
		this.log = LogFactory.getLog(this.getClass());
	}
}
