package step.framework.presentation.client.authenticator;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Authenticator<U> {
	void login(AsyncCallback<U> callback);
	void logout(AsyncCallback<Void> callback);
}
