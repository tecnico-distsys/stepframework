/**
 *  Groovy script to load flights into database
 */

//
//  check arguments
//

System.err.println("Running " + this.class.getSimpleName() + " with arguments " + args);
if(args.length < 1) {
    System.err.println("Please provide number of flights to generate as argument.");
    return;
}


//
//  initialization
//

def total = Integer.parseInt(args[0]);
assert(total > 0);

def random = null;
if(args.length >= 2) {
    def seed = Long.parseLong(args[1]);
    println "Using random seed value of " + seed;
    random = new Random(seed);
} else {
    println "Using default random seed";
    random = new Random();
}

def sql = DBHelper.init();
sql.connection.autoCommit = false;

def flightManagerId = DBHelper.getFlightManagerId(sql);

// id is auto incremented
def objVersion = 0;
def costPerPassenger;
def dateTime = (new GregorianCalendar(2012, Calendar.MARCH, 14, 0, 0, 0)).getTime();
def number;
def pricePerPassenger;
def lastReservationId = 0;
def airplaneId;
def destinationId;
def originId;

final def MAX_TO_GEN = 5;
final def MAX_COST = 1000;
final def PROFIT = 0.2;
final def MAX_NUMBER = 10000;

def count = 0;
while(count < total) {

    // pick origin and destination
    originId = DBHelper.pickRandomAirport(sql, random).id;
    destinationId = DBHelper.pickRandomAirport(sql, random).id;

    def toGen = random.nextInt(MAX_TO_GEN) + 1;
    if(total - count < toGen)
        toGen = total - count;

    // generate flights between origin and destination (within total limit)
    for(int i = 0; i < toGen; i++) {
        airplaneId = DBHelper.pickRandomAirplane(sql, random).id;
        costPerPassenger = random.nextInt(MAX_COST);
        pricePerPassenger = costPerPassenger * (1.0 + PROFIT);

        number = originId + "-" + destinationId + "-" + random.nextInt(MAX_NUMBER);

        def keys = sql.executeInsert("INSERT INTO flight (objVersion, costPerPassenger, dateTime, number," +
        "pricePerPassenger, lastReservationId, airplane_id, destination_id, origin_id)" +
        " VALUES (?,?,?,?,?,?,?,?,?)",
        [objVersion, costPerPassenger, dateTime, number,
        pricePerPassenger, lastReservationId, airplaneId, destinationId, originId]);
        def id = keys[0][0];
        sql.execute("INSERT INTO flightmanager_flight (FlightManager_id, flights_id) " +
        " VALUES (?,?)", [flightManagerId, id]);

        count++;
    }

}

sql.connection.commit();
