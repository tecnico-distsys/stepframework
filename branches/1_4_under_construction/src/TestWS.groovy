/**
 *  Groovy script to invoke Web Service
 */

def root = ".";

// add library locations to class loader
this.class.classLoader.rootLoader.addURL(FileURLHelper.toFileURL(root + "/lib/stepframework.jar"))
this.class.classLoader.rootLoader.addURL(FileURLHelper.toFileURL(root + "/flight-ws-cli/dist/flight-ws-cli.jar"))

// create Web Service stub
def service = Class.forName("org.tripplanner.flight.wsdl.FlightService").newInstance();
def port = service.getFlightPort();

// set endpoint address
def StubUtil = Class.forName("step.framework.ws.StubUtil");
StubUtil.setPortEndpointAddress(port, "http://localhost:8080/flight-ws/endpoint");

//
//  Search flights
//

// fill in request
def sfIn = Class.forName("org.tripplanner.flight.view.SearchFlightsInput").newInstance();
sfIn.depart = "Lisbon";
sfIn.arrive = "New York";

try {
    // invoke
    def sfOut = port.searchFlights(sfIn);
    // print results
    println "Response includes " + sfOut.flights.size() + " flights";
    sfOut.flights.each {
        println it.number;
        println it.date;
        println it.depart;
        println it.arrive;
        println it.price.currencyCode + " " + it.price.value;
    }

} catch(e) {
    println("Caught exception " + e);
}


//
//  Create single reservation
//


//
//  Create multiple reservations
//

