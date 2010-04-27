/**
 *  Command script to load database
 */
import org.apache.commons.cli.*;

public class LoadDB extends DBCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        LoadDB instance = new LoadDB();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    Long seed;
    Random random;


    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildSeedOption());

        return options;
    }

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (seed == null) {                
            seed = CommandHelper.initLong(settings[CommandHelper.SEED_LOPT], null);
        }
        
        if (random == null) {
            random = CommandHelper.initRandom(seed);
        }

        return true;
    }


    @Override public void dbRun() {

        def del = new DeleteDB();
        //del.loadSettingsFromProperties("db.properties");
        del.settings["url"] = settings["url"];
        del.settings["user"] = settings["user"];
        del.settings["pass"] = settings["pass"];
        del.run();
/*
        def flightMan = new LoadFlightManager();
        flightMan.settings = this.settings;
        flightMan.sql = this.sql;
        flightMan.dbRun();

        def airports = new LoadAirports();
        airports.settings = this.settings;
        airports.dataFile = new File("data\\airports.csv");
        airports.sql = this.sql;
        airports.random = this.random;
        airports.dbRun();

        def airplanes = new LoadAirplanes();
        airplanes.settings = this.settings;
        airplanes.dataFile = new File("data\\fleet-BA.csv");
        airplanes.sql = this.sql;
        airplanes.random = this.random;
        airplanes.dbRun();

        def flights = new LoadFlights();
        flights.settings = this.settings;
        flights.sql = this.sql;
        flights.random = this.random;
        flights.dbRun();
*/
        println("Done!");

    }


}
