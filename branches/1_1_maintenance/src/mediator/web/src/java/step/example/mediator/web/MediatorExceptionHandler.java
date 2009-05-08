package step.example.mediator.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.exception.AutoExceptionHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.domain.DomainException;

public class MediatorExceptionHandler implements AutoExceptionHandler {
	protected Log log;

	/* This method simply dumps the exception's stack trace. */
	public Resolution handle(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {

		LogFactory.getLog(this.getClass()).debug(ex);

		request.setAttribute("exception", ex);
		return new ForwardResolution("/error.jsp");
	}

	public Resolution handleDomainException(DomainException ex,
			HttpServletRequest request, HttpServletResponse response) {

		LogFactory.getLog(this.getClass()).debug(ex);

		ActionBean bean = (ActionBean) request
				.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);
		String reason = ex.getMessage();
		if (reason == null) {
			reason = "Unknown";
		}
		bean.getContext().getMessages().add(
				new LocalizableMessage("msg.booking.failed", reason));
		return new ForwardResolution("/default.jsp");
	}
}
