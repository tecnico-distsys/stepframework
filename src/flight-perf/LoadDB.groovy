/**
 *  Groovy script to load database
 */

System.err.println("Running " + this.class.getSimpleName() + " with arguments " + args);

// default values
def seed = null;
def nrFlights = 185;
def airplanesDataFilePath = "data\\fleet-BA.csv";
def airportDataFilePath = "data\\airports.csv";

if(args.length > 0) seed = args[0];
if(args.length > 1) nrFlights = args[1];    
if(args.length > 2) airplanesDataFilePath = args[2];
if(args.length > 3) airportDataFilePath = args[3];

def random = null;
if(seed != null) {
    println "Using random seed value of " + seed;
    random = new Random(Integer.parseInt(seed));
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
    (String[]) [185, 
               random.nextInt()]);

println "Done!";
