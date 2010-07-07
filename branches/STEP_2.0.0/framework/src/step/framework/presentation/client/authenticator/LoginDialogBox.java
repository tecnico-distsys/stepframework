package step.framework.presentation.client.authenticator;

import step.framework.presentation.exception.UnauthorizedException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class LoginDialogBox<U, P extends PasswordAuthenticator<U>> extends DialogBox {
	final private TextBox username;
	final private PasswordTextBox password;
	final private Label errorMessage;
	final private Button button;
	final private P passwordAuthenticator;
	
	private U user;

	public LoginDialogBox(P passwordAuthenticator) {
		super(true, true);

		this.passwordAuthenticator = passwordAuthenticator;
		FlexTable layout = new FlexTable();
		button = new Button("Login");
		username = new TextBox();
		password = new PasswordTextBox();
		errorMessage = new Label();
	
		this.setText("Login");
		this.setAnimationEnabled(true);
		super.setWidget(layout);
		layout.setWidget(0, 0, new Label("Username:"));
		layout.setWidget(0, 1, username);
		layout.setWidget(1, 0, new Label("Password:"));
		layout.setWidget(1, 1, password);
		errorMessage.addStyleName("ldb-errorMessage");
		layout.setWidget(2, 0, errorMessage);
		layout.getFlexCellFormatter().setColSpan(2, 0, 2);
		layout.setWidget(3, 0, button);
		layout.getFlexCellFormatter().setAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_BOTTOM);
		layout.getFlexCellFormatter().setColSpan(3, 0, 2);

		username.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				boolean hasText = !getUsername().isEmpty();
				if (!button.isEnabled() && hasText) {
					button.setEnabled(true);
				} else if (button.isEnabled() && !hasText) {
					button.setEnabled(false);
				}
			};
		});
		
		password.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (button.isEnabled() && event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					doLogin();
				}
			}
		});
		
		button.setEnabled(false);
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doLogin();
			};
		});
	}

	private void doLogin() {
		LoginDialogBox.this.setAutoHideEnabled(false);
		GWT.log("Login with username=" + getUsername() + " and password=" + getPassword() + ".", null);
		passwordAuthenticator.login(getUsername(), getPassword(), new AsyncCallback<U>() {
			public void onFailure(Throwable caught) {
				if (caught instanceof UnauthorizedException) {
					errorMessage.setText("Invalid username/password.");
				} else {
					GWT.log("Error logging in", caught);
					errorMessage.setText("Error communicating with server...");
				}
				username.setFocus(true);
				LoginDialogBox.this.setAutoHideEnabled(true);
			}

			public void onSuccess(U result) {
				GWT.log("User successfully logged in.", null);
				LoginDialogBox.this.user = result;
				LoginDialogBox.this.hide();
				LoginDialogBox.this.setAutoHideEnabled(true);
			}
		});
	}
	
	private String getUsername() {
		return username.getText();
	}
	
	private String getPassword() {
		return password.getText();
	}
	
	public U getUser() {
		return user;
	}
	
	@Override
	public void show() {
		errorMessage.setText("");
		username.setText("");
		password.setText("");
		super.show();
		username.setFocus(true);
	}
}