package step.example.mediator.web;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class MediatorAction implements ActionBean {
	protected MediatorContext context;
	protected Log log;

	public MediatorContext getContext() {
		return context;
	}

	public void setContext(ActionBeanContext context) {
		this.context = (MediatorContext) context;
	}

	public MediatorAction() {
		this.log = LogFactory.getLog(this.getClass());
	}
}
