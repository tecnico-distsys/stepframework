/**
 *  Groovy script to serialize Web Service requests
 */

// add library locations to class loader
def root = "../..";
ClassLoaderHelper.addFile(root + "/framework/dist/stepframework.jar");
ClassLoaderHelper.addFile(root + "/flight-ws-cli/dist/flight-ws-cli.jar");

//
// fill in requests
//

// search flights
def sfIn = Class.forName("org.tripplanner.flight.view.SearchFlightsInput").newInstance();
sfIn.depart = "Linga Linga";
sfIn.arrive = "Utapao";

// create single reservation
def csrIn = Class.forName("org.tripplanner.flight.view.CreateSingleReservationInput").newInstance();
csrIn.flightNumber = "12345";
def singlePassenger = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
singlePassenger.id = "123";
singlePassenger.name = "Alpha";
csrIn.passenger = singlePassenger;

// create multiple reservations
def cmrIn = Class.forName("org.tripplanner.flight.view.CreateMultipleReservationsInput").newInstance();
cmrIn.flightNumber = "12345";

def passengerList = cmrIn.passengers;

def passenger1 = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
passenger1.id = "1010";
passenger1.name = "Number One";
passengerList.add(passenger1);

def passenger2 = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
passenger2.id = "2020";
passenger2.name = "Number Two";
passengerList.add(passenger2);


//
//  Serialize requests
//

FileOutputStream fos = new FileOutputStream("build/requests.obj");
ObjectOutputStream oos = new ObjectOutputStream(fos);
oos.writeObject(sfIn);
oos.writeObject(csrIn);
oos.writeObject(cmrIn);
oos.close();


//
//  Deserialize requests
//

FileInputStream fis = new FileInputStream("build/requests.obj");
ObjectInputStream ois = new ObjectInputStream(fis);
def sfIn2 = ois.readObject();
def csrIn2 = ois.readObject();
def cmrIn2 = ois.readObject();
ois.close();

println "Search flights from " + sfIn2.depart + " to " + sfIn2.arrive;
println "Create single reservation for flight " + csrIn2.flightNumber;
println "Create multiple reservations for " + cmrIn2.passengers.size() + " passengers";
