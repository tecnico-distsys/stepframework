package step.proto.mediator.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import step.proto.mediator.service.BookFlightService;
import step.proto.mediator.view.ReservationView;
import step.framework.domain.DomainException;
import step.framework.exception.ServiceException;

public class BookingAction extends MediatorAction {
    @Validate(required=true, on="flight")
    private String origin;

    @Validate(required=true, on="flight")
    private String destination;

    @Validate(required=true, on="flight")
    private String id;
    
    // Should disappear once login is implemented
    @Validate(required=true, on="flight")
    private String name;
    
    private ReservationView result;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReservationView getResult() {
        return result;
    }

    public void setResult(ReservationView result) {
        this.result = result;
    }
    
    @DefaultHandler
    public Resolution flight() throws DomainException, ServiceException {
        setResult(new BookFlightService(origin, destination, id, name).execute());
        return new ForwardResolution("/default.jsp");
    }
}
