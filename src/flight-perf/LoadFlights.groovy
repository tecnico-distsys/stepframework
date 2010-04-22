/**
 *  Database command to load flights into database
 */
import org.apache.commons.cli.*;

public class LoadFlights extends DBCommand {

    // --- static ---

    public static void main(String[] args) {
        LoadFlights instance = new LoadFlights();
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

    Integer maxCost;
    Double profit;

    Integer nr;
    Integer maxGroup;


    //
    //  Initialization
    //

    @Override protected void setDefaultValues() {
        super.setDefaultValues();

        seed = null;
        random = null;

        maxCost = 1000;
        profit = 0.15;

        nr = 100;
        maxGroup = 10;
    }

    @Override protected Options createOptions() {
        Options options = super.createOptions();

        CommandHelper.addSeedOption(options);

        CommandHelper.addMaxCostOption(options);
        CommandHelper.addProfitOption(options);

        CommandHelper.addNrOption(options);
        CommandHelper.addMaxGroupOption(options);

        return options;
    }

    @Override protected boolean handleOptions(Options options, CommandLine cmdLine) {
        if (!super.handleOptions(options, cmdLine)) return false;

        String seedValue = getSetting(CommandHelper.SEED_OPT, cmdLine);
        seed = CommandHelper.initLong(seedValue, seedValue);

        random = CommandHelper.initRandom(seed);

        String maxCostValue = getSetting(CommandHelper.MAX_COST_OPT, cmdLine);
        maxCost = CommandHelper.initInteger(maxCostValue, maxCost);

        String profitValue = getSetting(CommandHelper.PROFIT_OPT, cmdLine);
        profit = CommandHelper.initDouble(profitValue, profit);

        String nrValue = getSetting(CommandHelper.NR_OPT, cmdLine);
        nr = CommandHelper.initInteger(nrValue, nr);

        String maxGroupValue = getSetting(CommandHelper.MAX_GROUP_OPT, cmdLine);
        maxGroup = CommandHelper.initInteger(maxGroupValue, maxGroup);

        return true;
    }


    @Override protected void dbRun() {

        err.println("Running " + this.class.simpleName);
        err.printf("Generate %d flights (max group size %d), max cost %d, profit %.2f",
            nr, maxGroup, maxCost, profit);
        if(seed != null) err.printf(", random seed %d", seed);
        err.println();

        def flightManagerId = FlightDBHelper.getFlightManagerId(sql);

        // id is auto incremented
        def objVersion = 0;
        def costPerPassenger;
        def dateTime = (new GregorianCalendar(2012, Calendar.MARCH, 14, 0, 0, 0)).getTime();    // TODO
        def number;
        def pricePerPassenger;
        def lastReservationId = 0;
        def airplaneId;
        def destinationId;
        def originId;

        def count = 0;
        while(count < nr) {

            // pick origin and destination
            originId = FlightDBHelper.pickRandomAirport(sql, random).id;
            destinationId = FlightDBHelper.pickRandomAirport(sql, random).id;

            def toGen = random.nextInt(maxGroup) + 1;
            if(nr - count < toGen)
                toGen = nr - count;

            // generate flights between origin and destination (within total limit)
            for(int i = 0; i < toGen; i++) {
                airplaneId = FlightDBHelper.pickRandomAirplane(sql, random).id;
                costPerPassenger = random.nextInt(maxCost);
                pricePerPassenger = costPerPassenger * (1.0 + profit);

                number = originId + "-" + destinationId + "-" + random.nextInt(Integer.MAX_VALUE);

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

    }

}
