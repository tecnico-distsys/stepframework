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
        if (instance.parseArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    //
    //  Members
    //
    Long seed;
    Random random;


    //
    //  Initialization
    //

    @Override protected void setDefaultValues() {
        super.setDefaultValues();

        seed = null;
        random = null;
    }

    @Override protected Options createOptions() {
        Options options = super.createOptions();

        CommandHelper.addSeedOption(options);

        return options;
    }

    @Override protected boolean handleOptions(Options options, CommandLine cmdLine) {
        if (!super.handleOptions(options, cmdLine)) return false;

        if (!super.handleOptions(options, cmdLine)) return false;

        String seedValue = getSetting(CommandHelper.SEED_OPT, cmdLine);
        seed = CommandHelper.initLong(seedValue, seedValue);

        random = CommandHelper.initRandom(seed);

        return true;
    }


    //
    //  Runnable
    //
    @Override public void dbRun() {

        def del = new DeleteDB().init();
        del.sql = this.sql;
        del.dbRun();

        def flightMan = new LoadFlightManager().init();
        flightMan.sql = this.sql;
        flightMan.dbRun();

        def airports = new LoadAirports().init();
        airports.dataFile = new File("data\\airports.csv");
        airports.sql = this.sql;
        airports.random = this.random;
        airports.dbRun();

        def airplanes = new LoadAirplanes().init();
        airplanes.dataFile = new File("data\\fleet-BA.csv");
        airplanes.sql = this.sql;
        airplanes.random = this.random;
        airplanes.dbRun();

        def flights = new LoadFlights().init();
        flights.sql = this.sql;
        flights.random = this.random;
        flights.dbRun();

        println "Done!";

    }

}
