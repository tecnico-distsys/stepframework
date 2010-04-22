import org.apache.commons.cli.*;
import groovy.sql.Sql;

public class DBCommand extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        DBCommand instance = new DBCommand();
        if (instance.parseArgs(args)) {
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

            //System.err.println("Adding STEP libraries location to class loader");

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


    //
    //  Initialization
    //

    @Override protected Options createOptions() {
        Options options = super.createOptions();

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

    @Override protected void setDefaultValues() {
        super.setDefaultValues();

        driver = "com.mysql.jdbc.Driver";   // MySQL
        url = "jdbc:mysql://localhost:3306/";
        user = "";
        pass = "";
    }

    @Override protected boolean handleOptions(Options options, CommandLine cmdLine) {
        if (!super.handleOptions(options, cmdLine)) return false;

        String driverValue = getSetting("driver", cmdLine);
        if (driverValue != null) {
            driver = driverValue;
        }

        String urlValue = getSetting("url", cmdLine);
        if (urlValue != null) {
            url = urlValue;
        }

        String userValue = getSetting("user", cmdLine);
        if (userValue != null) {
            user = userValue;
        }

        String passValue = getSetting("pass", cmdLine);
        if (passValue != null) {
            pass = passValue;
        }

        return true;
    }


    //
    //  Runnable
    //

    /**
     *  Run template method.
     *  Subclasses should override invoked methods to customize behavior.
     **/
    @Override final public void run() {
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

    /** Database connection */
    protected def sql;

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
        println("By your DataBase command!");
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
