package step.framework.presentation.client.authenticator;

import step.framework.presentation.exception.UnauthorizedException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;

public class AuthenticationManager<T> implements HasValueChangeHandlers<T> {
	private HandlerManager manager;
	private T user = null;
	private Authenticator<T> authenticator;
	private LoginDialogBox<T, PasswordAuthenticator<T>> popup;

	public AuthenticationManager(Authenticator<T> authenticator, PasswordAuthenticator<T> passwordAuthenticator) {
		this.manager = new HandlerManager(this);
		this.authenticator = authenticator;
		this.popup = new LoginDialogBox<T, PasswordAuthenticator<T>>(passwordAuthenticator);
		this.popup.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (!event.isAutoClosed()) {
					GWT.log("Login window closed after user successfully logged in", null);
					setUser(AuthenticationManager.this.popup.getUser());
				} else {
					GWT.log("Login window closed without user logging in", null);
				}
			};
		});
	}

	public void login(final boolean silent) {
		this.authenticator.login(new AsyncCallback<T>() {
			public void onFailure(Throwable caught) {
				if (caught instanceof UnauthorizedException) {
					// not logged in, start login procedure
					GWT.log("No user authenticated", caught);
					if (!silent) {
						AuthenticationManager.this.popup.center();
					}
				} else {
					// nothing could be determined
					GWT.log("Error checking authentication state", caught);
				}
			}

			public void onSuccess(T result) {
				// already logged in
				GWT.log("User already logged in", null);
				setUser(result);
			}
		});
	}
	
	final public void login() {
		login(false);
	}

	final public void logout() {
		if (isLoggedIn()) {
			this.authenticator.logout(new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					GWT.log("Error logging out", caught);
				}
				public void onSuccess(Void result) {
					GWT.log("User successfully logged out", null);
					setUser(null); 
				}
			});
		}
	}

	final public boolean isLoggedIn() { return (this.user != null); }

	final public T getUser() { return this.user; }

	final private void setUser(T user) {
		this.user = user;
		ValueChangeEvent.fire(this, user);
	}

	@Override
	final public void fireEvent(GwtEvent<?> event) {
		this.manager.fireEvent(event);
	}

	@Override
	final public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return this.manager.addHandler(ValueChangeEvent.getType(), handler);
	}
}