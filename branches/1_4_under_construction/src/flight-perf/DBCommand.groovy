/**
 *  DBCommand - template for groovy commands that use a database
 */

import org.apache.commons.cli.*;
import groovy.sql.Sql;

public class DBCommand extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        DBCommand instance = new DBCommand();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    //
    //  1-time setup
    //
    static setupFlag = true;

    static void setup() {
        if (setupFlag) {
            // load environment properties
            def env = System.getenv();
            // load libraries
            ClassLoaderHelper.addJarDir(env["STEP_HOME"] + "/lib");

            // clear flag
            setupFlag = false;
        }
    }


    // --- instance ---

    //
    //  Members
    //
    String driver;
    String url;
    String user;
    String pass;

    /** Database object */
    def sql;


    //
    //  Initialization
    //

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        Option dOption = new Option("driver", "driver",
            /* hasArg */ true, "JDBC driver class name");
        dOption.setArgName("classname");
        dOption.setArgs(1);
        options.addOption(dOption);

        Option urlOption = new Option("url", "url",
            /* hasArg */ true, "Database location in JDBC URL format");
        urlOption.setArgName("url");
        urlOption.setArgs(1);
        options.addOption(urlOption);

        Option userOption = new Option("user", "username",
            /* hasArg */ true, "Database username");
        userOption.setArgName("username");
        userOption.setArgs(1);
        options.addOption(userOption);

        Option passOption = new Option("pass", "password",
            /* hasArg */ true, "Database password");
        passOption.setArgName("password");
        passOption.setArgs(1);
        options.addOption(passOption);

        return options;
    }


    //
    //  Runnable
    //

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (sql == null) {

            String driverValue = settings["driver"];
            if (driverValue == null) driverValue = "com.mysql.jdbc.Driver";   // MySQL

            String urlValue = settings["url"];
            if (urlValue == null) {
                err.println("Database URL setting is missing!");
                return false;
            }

            String userValue = settings["user"];
            if (userValue == null) userValue = "";

            String passValue = settings["pass"];
            if (passValue == null) passValue = "";

            this.driver = driverValue;
            this.url = urlValue;
            this.user = userValue;
            this.pass = passValue;

        }

        return true;
    }

    /**
     *  Run template method.
     *  Subclasses should override invoked methods to customize behavior.
     **/
    @Override final public void cmdRun() {
        DBCommand.setup();

        try {
            this.dbOpen();
            this.dbRun();
            this.dbCommit();
        } catch(Throwable t) {
            //err.println(t);
            this.dbAbort();
            throw t;
        } finally {
            this.dbClose();
        }

    }

    /** run step: open database connection */
    protected void dbOpen() {
        // create database connection
        this.sql = Sql.newInstance(this.url, this.user, this.pass, this.driver);
        // start transaction
        //err.println("Begin DB transaction");
        this.sql.connection.autoCommit = false;
    }

    /** run step: execute database commands */
    protected void dbRun() {
        println("By your DataBase command ;-)");
    }

    /** run step: commit transaction */
    protected void dbCommit() {
        if (sql != null) {
            //err.println("Commit DB transaction");
            this.sql.connection.commit();
        }
    }

    /** run step: close database connection */
    protected void dbAbort() {
        if (sql != null) {
            //err.println("Abort DB transaction");
            this.sql.connection.rollback();
        }
    }

    /** run step: close database connection */
    protected void dbClose() {
        if (this.sql != null && this.sql.connection != null) {
            //err.println("Close DB connection");
            this.sql.connection.close();
        }
    }

}
