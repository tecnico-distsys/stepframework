package step.framework.presentation.server;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AuthenticationManager<U> extends RemoteServiceServlet {
	private static final long serialVersionUID = 1L;

	static final String AUTH_ATTRIBUTE = "step.user";
	
	protected void setAuth(U user) {
		HttpSession session = this.getThreadLocalRequest().getSession();
		session.setAttribute(AUTH_ATTRIBUTE, user);
	}

	@SuppressWarnings("unchecked")
	protected U getAuth() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		U user = (U) session.getAttribute(AUTH_ATTRIBUTE);
		return user;
	}

	@SuppressWarnings("unchecked")
	protected U resetAuth() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		U user = (U) session.getAttribute(AUTH_ATTRIBUTE);
		session.invalidate();
		return user;
	}
}
