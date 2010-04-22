/**
 *  Database command to load airports into database
 */
import org.apache.commons.cli.*;

public class LoadAirports extends DBCommand {

    // --- static ---

    public static void main(String[] args) {
        LoadAirports instance = new LoadAirports();
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

    @Override protected void setDefaultValues() {
        super.setDefaultValues();

        seed = null;
        random = null;

        dataFile = null;
        maxCost = 10000;
    }

    @Override protected Options createOptions() {
        Options options = super.createOptions();

        CommandHelper.addSeedOption(options);
        CommandHelper.addDataFileOption(options);
        CommandHelper.addMaxCostOption(options);

        return options;
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
        def city;
        def code;
        def costPerUse;

        dataFile.eachLine{line ->

            // ~ creates a Pattern
            // =~ creates a Matcher
            // ==~ tests if String matches the pattern

            def matcher = (line =~ "\"(.*?)\",\"(.*?)\".*");
            //                           | reluctant
            if(matcher.matches()) {
                code = matcher.group(1);
                city = matcher.group(2);
                costPerUse = random.nextInt(maxCost);

                def keys = sql.executeInsert("INSERT INTO airport (objVersion, city, code, costPerUse) " +
                " VALUES (?,?,?,?)", [objVersion, city, code, costPerUse]);
                def id = keys[0][0];

                sql.execute("INSERT INTO flightmanager_airport (FlightManager_id, airports_id) " +
                " VALUES (?,?)", [flightManagerId, id]);
            }

        }

    }


}
