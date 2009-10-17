package org.tripplanner.mediator.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

public class BookFlightAction extends MediatorAction {
	@DefaultHandler
	public Resolution prepareFlight() {
		return new ForwardResolution("/BookFlight.jsp");
	}
}