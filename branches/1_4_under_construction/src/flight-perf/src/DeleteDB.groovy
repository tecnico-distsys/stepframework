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

    @Override protected void dbRun() {

        err.println("Running " + this.class.simpleName);
        err.printf("url %s", settings["url"]);
        err.println();

        // delete all tables taking dependencies into account
        sql.execute("DELETE FROM flightreservation");

        sql.execute("DELETE FROM flightmanager_passenger");
        sql.execute("DELETE FROM passenger");

        sql.execute("DELETE FROM flightmanager_flight");
        sql.execute("DELETE FROM flight");

        sql.execute("DELETE FROM flightmanager_airplane");
        sql.execute("DELETE FROM airplane");

        sql.execute("DELETE FROM flightmanager_airport");
        sql.execute("DELETE FROM airport");

        sql.execute("DELETE FROM flightmanager");

    }

}
