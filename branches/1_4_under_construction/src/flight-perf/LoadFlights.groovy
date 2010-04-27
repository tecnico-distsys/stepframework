/**
 *  Database command to load flights into database
 */
import org.apache.commons.cli.*;

public class LoadFlights extends DBCommand {

    // --- static ---

    public static void main(String[] args) {
        LoadFlights instance = new LoadFlights();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }

    // --- instance ---

    Long seed;
    Random random;

    Integer maxCost;
    Double profit;

    Integer nr;
    Integer maxGroup;


    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildSeedOption());

        options.addOption(CommandHelper.buildMaxCostOption());
        options.addOption(CommandHelper.buildProfitOption());

        options.addOption(CommandHelper.buildNrOption());
        options.addOption(CommandHelper.buildMaxGroupOption());

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

        if (maxCost == null) {
            maxCost = CommandHelper.initInteger(settings[CommandHelper.MAX_COST_LOPT], 1000);
        }

        if (profit == null) {
            profit = CommandHelper.initDouble(settings[CommandHelper.PROFIT_LOPT], 0.15);
        }

        if (nr == null) {
            nr = CommandHelper.initInteger(settings[CommandHelper.NR_LOPT], 100);
        }

        if (maxGroup == null) {
            maxGroup = CommandHelper.initInteger(settings[CommandHelper.MAX_GROUP_LOPT], 10);
        }

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
