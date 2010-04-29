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

        def dbDel = new DeleteDB();
        dbDel.dbImportSettings(this);
        dbDel.run();

        def dbFM = new LoadFlightManager();
        dbFM.dbImportSettings(this);
        dbFM.run();

        def dbAirports = new LoadAirports();
        dbAirports.dbImportSettings(this);
        dbAirports.settings["seed"] = random.nextInt();
        dbAirports.settings["file"] = "data\\airports.csv";
        dbAirports.settings["maxcost"] = 150000;
        dbAirports.run();

        def dbAirplanes = new LoadAirplanes();
        dbAirplanes.dbImportSettings(this);
        dbAirplanes.settings["seed"] = random.nextInt();
        dbAirplanes.settings["file"] = "data\\fleet-BA.csv";
        dbAirports.settings["maxcost"] = 100000;
        dbAirplanes.run();

        def dbFlights = new LoadFlights();
        dbFlights.dbImportSettings(this);
        dbFlights.settings["seed"] = random.nextInt();
        dbFlights.settings["maxcost"] = 1500;
        dbFlights.settings["profit"] = 0.20;
        def number = 235 * 0.80 * 30; // fleet size * use rate * number of days
        dbFlights.settings["number"] = number.intValue();
        dbFlights.settings["maxgroup"] = 50;   // maximum nr of flights between destinations
        dbFlights.run();

        println("Done!");

    }


}
