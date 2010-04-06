package step.examples.tripplanner.mediator.presentation.client;


import java.util.List;

import step.examples.tripplanner.mediator.view.AirportInformation;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
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
	private final MediationServiceAsync airlineManagerService = GWT.create(MediationService.class);
	
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
		header.add(new HTML("<h1>Mediator System</h1>"));
		
		return header;
	}

	private Widget createMenu(final Panel main) {
		MenuBar menu = new MenuBar(true);
		menu.setHeight("100%");
		final MultiWordSuggestOracle airports = new MultiWordSuggestOracle();
		final BookFlightDialog bookingDialog= new BookFlightDialog(airlineManagerService, airports);
		
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
							airports.add(airport.getCity() + " " + airport.getName() + " ( " + airport.getCode() + " )");
						}
					}
				});
				
				bookingDialog.setName("");
				bookingDialog.center();
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
