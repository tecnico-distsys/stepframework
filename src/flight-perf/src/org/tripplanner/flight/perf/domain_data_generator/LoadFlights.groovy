package org.tripplanner.flight.perf.domain_data_generator;

import org.apache.commons.cli.*;
import step.groovy.command.*;
import org.tripplanner.flight.perf.helper.*;


/**
 *  Database command to load flights into database
 */
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
            maxCost = CommandHelper.initInteger(settings[CommandHelper.MAX_COST_LOPT], null);
            if (maxCost == null) {
                err.println("Maximum cost is missing!");
                return false;
            } else if (maxCost <= 0) {
                err.println("Maximum cost can not be negative or zero!");
                return false;
            }
        }

        if (profit == null) {
            profit = CommandHelper.initDouble(settings[CommandHelper.PROFIT_LOPT], null);
            if (profit == null) {
                err.println("Profit is missing!");
                return false;
            } else if (profit < 0.0) {
                err.println("Profit can not be negative!");
                return false;
            }
        }

        if (nr == null) {
            nr = CommandHelper.initInteger(settings[CommandHelper.NR_LOPT], null);
            if (nr == null) {
                err.println("Number is missing!");
                return false;
            } else if (nr <= 0) {
                err.println("Number can not be negative or zero!");
                return false;
            }
        }

        if (maxGroup == null) {
            maxGroup = CommandHelper.initInteger(settings[CommandHelper.MAX_GROUP_LOPT], null);
            if (maxGroup == null) {
                err.println("Maximum group size is missing!");
                return false;
            } else if (maxGroup <= 0) {
                err.println("Maximum group size can not be negative or zero!");
                return false;
            }
        }

        return true;
    }


    @Override protected void dbRun() {

        err.println("Running " + this.class.simpleName);
        err.println("Database URL: " + this.url);
        err.printf("Generate %d flights (max group size %d), max cost %d, profit %.2f",
            nr, maxGroup, maxCost, profit);
        if(seed != null) err.printf(", random seed %d", seed);
        err.println();

        def flightManagerId = FlightDBHelper.getFlightManagerId(sql);

        // id is auto incremented
        def objVersion = 0;
        def costPerPassenger;
        def dateTime = (new GregorianCalendar(2012, Calendar.MARCH, 13, 0, 0, 0)).getTime();    // TODO
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
