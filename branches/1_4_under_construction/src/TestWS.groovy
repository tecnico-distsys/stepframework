/**
 *  Groovy script to invoke Web Service
 */

def root = ".";

// add library locations to class loader
def urlHelper = new FileURLHelper();
this.class.classLoader.rootLoader.addURL(urlHelper.toFileURL(root + "/lib/stepframework.jar"))
this.class.classLoader.rootLoader.addURL(urlHelper.toFileURL(root + "/flight-ws-cli/dist/flight-ws-cli.jar"))

// create Web Service stub
def service = Class.forName("org.tripplanner.flight.wsdl.FlightService").newInstance();
def port = service.getFlightPort();

// set endpoint address
def StubUtil = Class.forName("step.framework.ws.StubUtil");
StubUtil.setPortEndpointAddress(port, "http://localhost:8080/flight-ws/endpoint");

// fill in request
def passenger = Class.forName("org.tripplanner.flight.wsdl.Passenger").newInstance();
passenger.id = 1;
passenger.name = "Mike";

try {
    // invoke
    def result = port.createReservation("Lisbon", "New York", passenger);
    // print results
    printf("Reservation code: %s, Flight Number: %s %n", result.reservationCode, result.flightNumber);
} catch(e) {
    println("Caught exception " + e);
}
