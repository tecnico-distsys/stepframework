/**
 *  Groovy script to generate Web Service requests
 */

//
//  Constants
//

final def MAX_THINK = 5;     // seconds
final def MAX_GROUP = 6;     // multiple reservation group size
final def MAX_PASSENGER_ID = 100000;


//
//  Initialization
//

// add library locations to class loader
def root = "..";
ClassLoaderHelper.addFile(root + "/framework/dist/stepframework.jar");
ClassLoaderHelper.addFile(root + "/flight-ws-cli/dist/flight-ws-cli.jar");

// random
def random = new Random(); // TODO seed

NameGenerator nameGenerator = new NameGenerator("data/names.txt", "data/surnames.txt", random);

// object output stream
FileOutputStream fos = new FileOutputStream("build/requests.obj");
ObjectOutputStream oos = new ObjectOutputStream(fos);

// database
def sql = DBHelper.init();

// operation identifier string
def operation;


def count = 0;
while(count < 10) {
    count++;

    //
    //  generate search flights request
    //
    operation = "SEARCH_FLIGHTS";

    def flight = DBHelper.pickRandomFlight(sql, random);

    def depart = DBHelper.getAirport(sql, flight.origin_id).city;
    def arrive = DBHelper.getAirport(sql, flight.destination_id).city;

    def sfIn = Class.forName("org.tripplanner.flight.view.SearchFlightsInput").newInstance();
    sfIn.depart = depart;
    sfIn.arrive = arrive;

    oos.writeObject(operation);
    oos.writeObject(sfIn);


    //
    //  generate think time
    //
    operation = "THINK";

    oos.writeObject(operation);
    oos.writeObject(random.nextInt(MAX_THINK));


    //
    //  generate create reservations
    //
    def groupSize = random.nextInt(MAX_GROUP);

    if(groupSize == 0) {
        // no reservation

    } else if(groupSize == 1) {
        //
        //  generate create single reservation
        //
        operation = "CREATE_SINGLE_RESERVATION";

        def csrIn = Class.forName("org.tripplanner.flight.view.CreateSingleReservationInput").newInstance();
        csrIn.flightNumber = flight.number;
        def singlePassenger = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
        singlePassenger.id = random.nextInt(MAX_PASSENGER_ID);
        singlePassenger.name = nameGenerator.nextName();
        csrIn.passenger = singlePassenger;

        oos.writeObject(operation);
        oos.writeObject(csrIn);

    } else {
        //
        //  generate multiple reservations
        //
        operation = "CREATE_MULTIPLE_RESERVATIONS";

        def cmrIn = Class.forName("org.tripplanner.flight.view.CreateMultipleReservationsInput").newInstance();
        cmrIn.flightNumber = flight.number;

        def passengerList = cmrIn.passengers;

        for(int i = 0; i < groupSize; i++) {
            def groupPassenger = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
            groupPassenger.id = random.nextInt(MAX_PASSENGER_ID);
            groupPassenger.name = nameGenerator.nextName();
            passengerList.add(groupPassenger);
        }

        oos.writeObject(operation);
        oos.writeObject(cmrIn);

    }


    //
    //  generate think time
    //
    operation = "THINK";

    oos.writeObject(operation);
    oos.writeObject(random.nextInt(MAX_THINK));

}


//
//  Finish
//
oos.close();
