/**
 *  Groovy script to load airports into database
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
def city;
def code;
def costPerUse;

final def MAX_COST = 10000;

dataFile.eachLine{line ->

    // ~ creates a Pattern
    // =~ creates a Matcher
    // ==~ tests if String matches the pattern

    def matcher = (line =~ "\"(.*?)\",\"(.*?)\".*");
    //                           | reluctant
    if(matcher.matches()) {
        code = matcher.group(1);
        city = matcher.group(2);
        costPerUse = random.nextInt(MAX_COST);
    
        def keys = sql.executeInsert("INSERT INTO airport (objVersion, city, code, costPerUse) " +
        " VALUES (?,?,?,?)", [objVersion, city, code, costPerUse]);
        def id = keys[0][0];
        
        sql.execute("INSERT INTO flightmanager_airport (FlightManager_id, airports_id) " +
        " VALUES (?,?)", [flightManagerId, id]); 
    }

}

sql.connection.commit();
