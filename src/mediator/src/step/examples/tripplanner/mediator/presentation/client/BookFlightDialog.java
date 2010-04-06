package step.examples.tripplanner.mediator.presentation.client;

import step.examples.tripplanner.mediator.exception.NoFlightAvailableException;
import step.examples.tripplanner.mediator.view.ReservationView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class BookFlightDialog extends DialogBox {

	private final TextBox name = new TextBox();
	final SuggestBox departFrom;
	final SuggestBox arriveAt;
	final TextBox passport;
	
	public BookFlightDialog(final MediationServiceAsync server, MultiWordSuggestOracle airports) {
		super(true, true);
		final VerticalPanel request = new VerticalPanel();
		final FlexTable form = new FlexTable();
		departFrom = new SuggestBox(airports);
		arriveAt = new SuggestBox(airports);
		passport = new TextBox();
		final Button sendButton = new Button("Request");

		this.setText("Book flight");
		this.setAnimationEnabled(true);
		this.setWidget(request);
		form.setWidget(0, 0, new Label("From"));
		form.setWidget(1, 0, departFrom);
		form.setWidget(0, 1, new Label("To"));
		form.setWidget(1, 1, arriveAt);
		form.setWidget(2,0, new HTML("<hr/>"));
		form.getFlexCellFormatter().setColSpan(2, 0, 2);
		form.setWidget(3, 0, new Label("Passenger Name"));
		form.setWidget(4, 0, name);
		form.setWidget(3, 1, new Label("Passport #"));
		form.setWidget(4, 1, passport);
		request.add(form);
		request.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		request.add(sendButton);
		sendButton.addStyleName("sendButton");

		// response panel
		final DialogBox responseWindow = new DialogBox(false, true);
		final VerticalPanel response = new VerticalPanel();
		final HTML message = new HTML();
		final Button closeButton = new Button("Close");
		responseWindow.setText("Booking information");
		responseWindow.setAnimationEnabled(true);
		responseWindow.setWidget(response);
		response.add(message);
		response.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		response.add(closeButton);
		closeButton.addStyleName("closeButton");
		
		// Add a handler to process the form and send the request
		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (departFrom.getText().equals("") || arriveAt.getText().equals("") || name.getText().equals("") || passport.getText().equals("")) {
					message.setHTML("Please fill in all data!");
					message.addStyleName("errorMessage");
					responseWindow.center();
					return;
				}
				
				String from = departFrom.getText();
				String to = arriveAt.getText();
				from = from.substring(from.lastIndexOf('(')+2, from.lastIndexOf(')')-1);
				to = to.substring(to.lastIndexOf('(')+2, to.lastIndexOf(')')-1);
				
				server.bookFlight(from, to, passport.getText(), name.getText(),
						new AsyncCallback<ReservationView>() {
					
					public void onFailure(Throwable caught) {
						if (caught instanceof NoFlightAvailableException) {
							message.setHTML("No available flight from '"+ departFrom.getText() + 
									"' to '" + arriveAt.getText() + "'...");
							message.removeStyleName("errorMessage");
						} else {
							BookFlightDialog.this.hide();
							message.setHTML("RPC invocation failed!");
							message.addStyleName("errorMessage");
						}
						responseWindow.center();
					}
					
					public void onSuccess(ReservationView result) {
						BookFlightDialog.this.hide();
						message.setHTML("Reservation #" + result.getCode()
								+ "<br/>Client ID: " + result.getClient().getIdentification()
								+ "<br/>Client Name: " + result.getClient().getName());
						message.removeStyleName("errorMessage");
						responseWindow.center();
					}
				});
			}
		});

		// Add a handler to close the response window
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				responseWindow.hide();
			}
		});
		
	}
	
	public void setName(String name) {
		this.name.setText(name);
	}

	@Override
	public void show() {
		this.departFrom.setText("");
		this.arriveAt.setText("");
		this.passport.setText("");
		super.show();
	}
	
}
