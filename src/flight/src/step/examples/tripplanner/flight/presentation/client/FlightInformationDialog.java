package step.examples.tripplanner.flight.presentation.client;

import java.util.Date;

import step.examples.tripplanner.flight.view.FlightInformation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;

public class FlightInformationDialog extends DialogBox {
	final TextBox flightNumber;
	final DatePicker departureDate;

	public FlightInformationDialog(final AirlineManagerAsync server) {
		super(true, true);
		final VerticalPanel request = new VerticalPanel();
		final Button sendButton = new Button("Request");
		flightNumber = new TextBox();
		departureDate = new DatePicker();
		
		this.setText("Request flight information");
		this.setAnimationEnabled(true);
		this.setWidget(request);
		request.add(new Label("Flight #"));
		request.add(flightNumber);
		request.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		request.add(new Label("Departure Date:"));
		request.add(departureDate);
		request.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		request.add(sendButton);
		sendButton.addStyleName("sendButton");

		// response panel
		final DialogBox responseWindow = new DialogBox(false, true);
		final VerticalPanel response = new VerticalPanel();
		final HTML message = new HTML();
		final Button closeButton = new Button("Close");
		responseWindow.setWidget(response);
		responseWindow.setText("Flight information");
		responseWindow.setAnimationEnabled(true);
		response.add(message);
		response.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		response.add(closeButton);
		message.addStyleName("errorMessage");
		closeButton.addStyleName("closeButton");
		
		// Add a handler to process the form and send the request
		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (flightNumber.getText().equals("")) {
					message.setHTML("Please enter a flight number!");
					message.addStyleName("errorMessage");
					responseWindow.center();
					return;
				}
				

				server.requestFlightInformation(flightNumber.getText(), departureDate.getValue(), new AsyncCallback<FlightInformation>() {
					public void onFailure(Throwable caught) {
						message.setHTML("RPC invocation failed!");
						FlightInformationDialog.this.hide();
						message.addStyleName("errorMessage");
						responseWindow.center();
					}

					public void onSuccess(FlightInformation result) {
						message.setHTML("Flight #" + result.getFlightNumber()
								+ "<br/>Departure Time: " + DateTimeFormat.getShortTimeFormat().format(result.getDeparture())
								+ "<br/>Origin:" + result.getOrigin().getCity() + " (" + result.getOrigin().getIATACode() + ")"
								+ "<br/>Destination:" + result.getDestination().getCity() + " (" + result.getDestination().getIATACode() + ")"
								+ "<br/>" + (result.isAvailable() ? "Available seats" : "Fully booked"));
						message.removeStyleName("errorMessage");
						FlightInformationDialog.this.hide();
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
	

	@Override
	public void show() {
		this.flightNumber.setText("");
		Date now = new Date();
		this.departureDate.setCurrentMonth(now);
		this.departureDate.setValue(now);
		super.show();
	}

}
