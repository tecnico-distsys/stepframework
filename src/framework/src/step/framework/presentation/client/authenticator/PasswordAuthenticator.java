package step.framework.presentation.client.authenticator;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PasswordAuthenticator<U> {
	void login(String username, String password, AsyncCallback<U> callback);

}
