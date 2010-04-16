/**
 *  Groovy script to load airplanes into database
 */

//
//  check arguments
//

System.err.println("Running " + this.class.getSimpleName() + " with arguments " + args);
if(args.length < 1) {
    System.err.println("Please provide data file as argument.");
    return;
}


//
//  initialization
//

def dataFile = new File(args[0]);
assert dataFile.exists();

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
def capacity;
def costPerUse;
def model;
def registration;

final def MAX_COST = 100000;

dataFile.eachLine{line ->

    // ~ creates a Pattern
    // =~ creates a Matcher
    // ==~ tests if String matches the pattern

    def matcher = (line =~ "\"(.*?)\",\"(.*?)\",\"(.*?)\".*");
    //                           | reluctant
    if(matcher.matches()) {
        model = matcher.group(1);
        def nr = matcher.group(2);
        capacity = matcher.group(3);

        costPerUse = random.nextInt(MAX_COST);
    
        def total = Integer.parseInt(nr);
        def count = 0;
        while(count < total) {
            registration = model + "_" + count;

            def keys = sql.executeInsert("INSERT INTO airplane (objVersion, capacity, costPerUse, model, registration) " +
            " VALUES (?,?,?,?,?)", [objVersion, capacity, costPerUse, model, registration]);
            def id = keys[0][0];
            
            sql.execute("INSERT INTO flightmanager_airplane (FlightManager_id, airplanes_id) " +
            " VALUES (?,?)", [flightManagerId, id]); 

            count++;   
        }
    
    }

}

sql.connection.commit();
