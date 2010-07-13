package org.tripplanner.flight.perf.domain_data_generator;

import org.apache.commons.cli.*;
import step.groovy.command.*;
import org.tripplanner.flight.perf.helper.*;

/**
 *  Database command to load airports into database
 */
public class LoadAirports extends DBCommand {

    // --- static ---

    public static void main(String[] args) {
        LoadAirports instance = new LoadAirports();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }

    // --- instance ---

    Long seed;
    Random random;

    File dataFile;
    Integer maxCost;


    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildSeedOption());
        options.addOption(CommandHelper.buildFileOption());
        options.addOption(CommandHelper.buildMaxCostOption());

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

        if (dataFile == null) {
            dataFile = CommandHelper.initFile(settings[CommandHelper.FILE_LOPT], null);
            if (dataFile == null || !dataFile.exists()) {
                err.println("Data file is missing or does not exist!");
                return false;
            }
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

        return true
    }


    @Override protected void dbRun() {

        err.println("Running " + this.class.simpleName);
        err.println("Database URL: " + this.url);
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
