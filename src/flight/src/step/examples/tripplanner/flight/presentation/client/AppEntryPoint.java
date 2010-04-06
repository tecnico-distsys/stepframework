package step.examples.tripplanner.flight.presentation.client;


import java.util.List;

import step.examples.tripplanner.flight.view.AirportInformation;
import step.examples.tripplanner.flight.view.FlightInformation;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AppEntryPoint implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side AirlineManager service.
	 */
	private final AirlineManagerAsync airlineManagerService = GWT.create(AirlineManager.class);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		setupLayout();
	}
	private Widget createHeader() {
		HorizontalPanel header = new HorizontalPanel();
		header.setStylePrimaryName("header");
		header.setWidth("100%");
		header.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		header.add(new Image("logo.png"));
		header.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		header.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		header.add(new HTML("<h1>Flight Booking System</h1>"));
		
		return header;
	}

	private Widget createMenu(final Panel main) {
		MenuBar menu = new MenuBar(true);
		menu.setHeight("100%");
		final MultiWordSuggestOracle airports = new MultiWordSuggestOracle();
		final BookFlightDialog bookingDialog= new BookFlightDialog(airlineManagerService, airports);
		final DialogBox infoDialog= new FlightInformationDialog(airlineManagerService);
		
		menu.addItem("Book flight", new Command() {
			@Override
			public void execute() {
				// reset airport list
				airports.clear();
				
				// update airport list from server
				airlineManagerService.requestAirports(new AsyncCallback<List<AirportInformation>>() {
					public void onFailure(Throwable caught) {
						// silently ignore failure, just don't give any suggestion
					}
					
					public void onSuccess(List<AirportInformation> result) {
						for (AirportInformation airport : result) {
							airports.add(airport.getCity() + " (" + airport.getIATACode() + ")");
						}
					}
				});
				
				bookingDialog.setName("");
				bookingDialog.center();
			}
		});

		menu.addItem("View flight information", new Command() {
			@Override
			public void execute() {
				infoDialog.center();
			}
		});
		
		menu.addSeparator();
		
		menu.addItem("View Available flights", new Command() {
			@Override
			public void execute() {
				airlineManagerService.requestAvailableFlights(new AsyncCallback<List<FlightInformation>>() {
					public void onFailure(Throwable caught) {
						// show error Dialog Box
						final DialogBox responseWindow = new DialogBox(true, true);
						final VerticalPanel response = new VerticalPanel();
						final HTML message = new HTML("RPC invocation failed!");
						final Button closeButton = new Button("Close");
						responseWindow.setText("Available Flights");
						responseWindow.setAnimationEnabled(true);
						responseWindow.setWidget(response);
						response.add(message);
						response.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
						response.add(closeButton);
						message.addStyleName("errorMessage");
						closeButton.addStyleName("closeButton");
						
						// Add a handler to close the response window
						closeButton.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								responseWindow.hide();
							}
						});
						responseWindow.center();
					}
					
					public void onSuccess(List<FlightInformation> result) {
						// populate table
						FlexTable flightTable = new FlexTable();
						flightTable.addStyleName("flightTable");
						
						int i = 1;
						
						flightTable.setWidget(0, 0, new Label("Flight #"));
						flightTable.setWidget(0, 1, new Label("From"));
						flightTable.setWidget(0, 2, new Label("To"));
						flightTable.setWidget(0, 3, new Label("Date"));
						flightTable.setWidget(0, 4, new Label("Time"));
						
						for (FlightInformation flight : result) {
							flightTable.setWidget(i, 0, new Label(flight.getFlightNumber()));
							flightTable.setWidget(i, 1, new Label(flight.getOrigin().getCity() + " (" + flight.getOrigin().getIATACode() + ")"));
							flightTable.setWidget(i, 2, new Label(flight.getDestination().getCity() + " (" + flight.getDestination().getIATACode() + ")"));
							flightTable.setWidget(i, 3, new Label(DateTimeFormat.getShortDateFormat().format(flight.getDeparture())));
							flightTable.setWidget(i, 4, new Label(DateTimeFormat.getShortTimeFormat().format(flight.getDeparture())));
							i++;
						}
						
						// show results on main panel
						main.clear();
						main.add(flightTable);
					}
				});
			}
		});
		
		return menu;
	}
	
	private Widget createFooter() {
		FlowPanel footer = new FlowPanel();
		
		footer.setWidth("100%");
		footer.add(new InlineHTML("&copy; 2010 Eng. Software, Dep. Eng. Informática, Instituto Superior Técnico&nbsp;|&nbsp;"));
		footer.add(new Anchor("Webmaster", "mailto:webmaster@someplace.pt"));
		
		return footer;
	}

	public void setupLayout() {
		DockLayoutPanel layout = new DockLayoutPanel(Unit.EM);
		SimplePanel main = new SimplePanel();
		layout.setWidth("100%");
		layout.addNorth(createHeader(), 5);
		layout.addSouth(createFooter(), 2);
		layout.addWest(createMenu(main), 12);
		layout.add(main);
		RootLayoutPanel.get().add(layout);
	}
}
