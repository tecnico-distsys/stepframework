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

def flightToBook = null;

// fill in request
def sfIn = Class.forName("org.tripplanner.flight.view.SearchFlightsInput").newInstance();
sfIn.depart = "Lisbon";
sfIn.arrive = "New York";

try {
    // invoke
    def sfOut = port.searchFlights(sfIn);
    // print results
    println "Response:";
    println sfOut.flights.size() + " flights";
    sfOut.flights.each {
        print "Flight " + it.number + " on " + it.date;
        print " from " + it.depart + " to " + it.arrive;
        println " costing " + it.price.code + " " + it.price.value;
    }

    if(sfOut.flights.size() > 0) {
        flightToBook = sfOut.flights.get(0);
        println "Picked flight " + flightToBook.number + " to book reservations";
    }

} catch(e) {
    println("Caught exception " + e);
}


//
//  Create single reservation
//

// fill in request
def csrIn = Class.forName("org.tripplanner.flight.view.CreateSingleReservationInput").newInstance();
csrIn.flightNumber = flightToBook.number;
def singlePassenger = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
singlePassenger.id = "123";
singlePassenger.id = "Alpha";
csrIn.passenger = singlePassenger;

try {
    // invoke
    def csrOut = port.createSingleReservation(csrIn);
    // print results
    println "Response:";
    println "Reservation code " + csrOut.reservation.code + " for flight " + csrOut.reservation.flightNumber;

} catch(e) {
    println("Caught exception " + e);
}


//
//  Create multiple reservations
//

// fill in request
def cmrIn = Class.forName("org.tripplanner.flight.view.CreateMultipleReservationsInput").newInstance();
cmrIn.flightNumber = flightToBook.number;

def passengerList = cmrIn.passengers;

def passenger1 = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
passenger1.id = "1010";
passenger1.id = "Number One";
passengerList.add(passenger1);

def passenger2 = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
passenger2.id = "2020";
passenger2.id = "Number Two";
passengerList.add(passenger2);

try {
    // invoke
    def cmrOut = port.createMultipleReservations(cmrIn);

    // print results
    println "Response:";
    println cmrOut.reservations.size() + " reservations";
    cmrOut.reservations.each {
        println "Reservation code " + it.code + " for flight " + it.flightNumber;
    }

} catch(e) {
    println("Caught exception " + e);
}

