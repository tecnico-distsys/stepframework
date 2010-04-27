/**
 *  Database command to the single flight manager
 */

public class LoadFlightManager extends DBCommand {

    // --- static ---

    public static void main(String[] args) {
        LoadFlightManager instance = new LoadFlightManager();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }

    // --- instance ---

    @Override protected void dbRun() {

        err.println("Running " + this.class.simpleName);

        // id is auto incremented
        def objVersion = 0;

        sql.execute("INSERT INTO flightmanager (objVersion) VALUES (?)", [objVersion]);

    }

}
