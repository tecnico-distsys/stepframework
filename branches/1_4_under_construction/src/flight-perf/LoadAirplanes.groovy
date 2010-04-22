/**
 *  Database command to load airplanes into database
 */
import org.apache.commons.cli.*;

public class LoadAirplanes extends DBCommand {

    // --- static ---

    public static void main(String[] args) {
        LoadAirplanes instance = new LoadAirplanes();
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

    File dataFile;
    Integer maxCost;


    //
    //  Initialization
    //

    @Override protected Options createOptions() {
        Options options = super.createOptions();

        CommandHelper.addSeedOption(options);
        CommandHelper.addDataFileOption(options);
        CommandHelper.addMaxCostOption(options);

        return options;
    }

    @Override protected void setDefaultValues() {
        super.setDefaultValues();

        seed = null;
        random = null;

        dataFile = null;
        maxCost = 50000;
    }

    @Override protected boolean handleOptions(Options options, CommandLine cmdLine) {
        if (!super.handleOptions(options, cmdLine)) return false;

        String seedValue = getSetting(CommandHelper.SEED_OPT, cmdLine);
        seed = CommandHelper.initLong(seedValue, seedValue);

        random = CommandHelper.initRandom(seed);

        String dataFileValue = getSetting(CommandHelper.DATA_FILE_OPT, cmdLine);
        dataFile = CommandHelper.initFile(dataFileValue, dataFile);
        if (dataFile == null || !dataFile.exists()) {
            err.println("Data file is missing or file does not exist!");
            return false;
        }

        String maxCostValue = getSetting(CommandHelper.MAX_COST_OPT, cmdLine);
        maxCost = CommandHelper.initInteger(maxCostValue, maxCost);

        return true;
    }


    @Override protected void dbRun() {

        err.println("Running " + this.class.simpleName);
        err.printf("data file %s, max cost %d", dataFile, maxCost);
        if(seed != null) err.printf(", random seed %d", seed);
        err.println();


        def flightManagerId = FlightDBHelper.getFlightManagerId(sql);

        // id is auto incremented
        def objVersion = 0;
        def capacity;
        def costPerUse;
        def model;
        def registration;

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

                costPerUse = random.nextInt(maxCost);

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

    }
}
