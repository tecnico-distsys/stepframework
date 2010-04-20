/**
 *  Groovy script to execute simulated user requests.
 */


//
//  Initialization
//

// add library locations to class loader
def root = "..";
ClassLoaderHelper.addFile(root + "/framework/dist/stepframework.jar");
ClassLoaderHelper.addFile(root + "/flight-ws-cli/dist/flight-ws-cli.jar");

FileInputStream fis = new FileInputStream("build/requests.obj");
ObjectInputStream ois = new ObjectInputStream(fis);

// create Web Service stub
def service = Class.forName("org.tripplanner.flight.wsdl.FlightService").newInstance();
def port = service.getFlightPort();

// set endpoint address
def StubUtil = Class.forName("step.framework.ws.StubUtil");
StubUtil.setPortEndpointAddress(port, "http://localhost:8080/flight-ws/endpoint");


//
//  Simulate requests
//

def eof = false;
while (!eof) {
    try {
        def operation = ois.readObject();

        if ("THINK".equals(operation)) {
            def thinkSeconds = ois.readObject();
            if(thinkSeconds > 0) {
                println "Think " + thinkSeconds + " seconds";
                Thread.sleep(thinkSeconds * 1000);
            }

        } else if ("SEARCH_FLIGHTS".equals(operation)) {
            def sfIn = ois.readObject();
            println "Search flights from " + sfIn.depart + " to " + sfIn.arrive;
            port.searchFlights(sfIn);

        } else if ("CREATE_SINGLE_RESERVATION".equals(operation)) {
            def csrIn = ois.readObject();
            println "Create single reservation for flight " + csrIn.flightNumber;
            port.createSingleReservation(csrIn);

        } else if ("CREATE_MULTIPLE_RESERVATIONS".equals(operation)) {
            def cmrIn = ois.readObject();
            println "Create " + cmrIn.passengers.size() + " reservations for flight " + cmrIn.flightNumber;
            port.createMultipleReservations(cmrIn);

        } else {
            throw new RuntimeException("Unknown operation " + operation + ".");
        }

    } catch(EOFException eofe) {
        eof = true;
    }
}

println "Done!";
