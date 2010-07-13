package org.tripplanner.flight.perf.domain_data_generator;

import org.apache.commons.cli.*;
import step.groovy.command.*;
import org.tripplanner.flight.perf.helper.*;

/**
 *  Database command to delete all records from flight database
 */
public class DeleteDB extends DBCommand {

    // --- static ---

    public static void main(String[] args) {
        DeleteDB instance = new DeleteDB();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }

    // --- instance ---

    Boolean quick;

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildQuickOption());

        return options;
    }

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (quick == null) {
            quick = CommandHelper.initBoolean(settings[CommandHelper.QUICK_LOPT], false);
        }
    }


    @Override protected void dbRun() {

        err.println("Running " + this.class.simpleName);
        err.println("Database URL: " + this.url);
        err.println();

        // delete all tables taking dependencies into account
        sql.execute("DELETE FROM flightreservation");

        sql.execute("DELETE FROM flightmanager_passenger");
        sql.execute("DELETE FROM passenger");

        if (quick) {
            err.println("Quick mode - skipping flights, airplanes, airports and manager deletion");

        } else {
            sql.execute("DELETE FROM flightmanager_flight");
            sql.execute("DELETE FROM flight");

            sql.execute("DELETE FROM flightmanager_airplane");
            sql.execute("DELETE FROM airplane");

            sql.execute("DELETE FROM flightmanager_airport");
            sql.execute("DELETE FROM airport");

            sql.execute("DELETE FROM flightmanager");

        }

    }

}
