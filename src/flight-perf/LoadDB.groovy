/**
 *  Groovy script to load database
 */

System.err.println("Running " + this.class.getSimpleName() + " with arguments " + args);

// default values
def seed = 20120314;
def nrFlights = 185*30; // a month's worth of flights
def airplanesDataFilePath = "data\\fleet-BA.csv";
def airportDataFilePath = "data\\airports.csv";

if(args.length > 0) {
    if("default".equals(args[0])) {
        seed = null;
    } else {
        seed = Integer.parseInt(args[0]);
    }
}
if(args.length > 1) nrFlights = args[1];
if(args.length > 2) airplanesDataFilePath = args[2];
if(args.length > 3) airportDataFilePath = args[3];

def random = null;
if(seed != null) {
    println "Using random seed value of " + seed;
    random = new Random(seed);
} else {
    println "Using default random seed";
    random = new Random();
}

DeleteDB.main();

LoadFlightManager.main();

LoadAirports.main(
    (String[]) [airportDataFilePath,
               random.nextInt()]);

LoadAirplanes.main(
    (String[]) [airplanesDataFilePath,
               random.nextInt()]);

LoadFlights.main(
    (String[]) [nrFlights,
               random.nextInt()]);

println "Done!";
