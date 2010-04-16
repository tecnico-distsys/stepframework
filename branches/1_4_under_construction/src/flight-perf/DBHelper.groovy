/**
 *  Database Helper.
 */
import groovy.sql.Sql;

class DBHelper {

    static def init() {

        // load environment properties
        def env = System.getenv();
        
        // load database settings
        def db = new Properties();
        db.load(new FileInputStream("database.properties"));

        // load libraries
        ClassLoaderHelper.addJarDir(env["STEP_HOME"] + "/lib");

        // create database connection
        def sql = Sql.newInstance(db["url"], db["user"], db["pass"], db["driver"]);

        return sql;
    }

    static def getFlightManagerId(sql) {
        def row = sql.firstRow("SELECT id FROM flightmanager ORDER BY id ASC");
        assert(row != null);
        def id = row.id;
        assert(id != null);
        return id;
    }

    static def pickRandomAirplane(sql, random) {
        def row = sql.firstRow("SELECT id FROM airplane ORDER BY rand(?) LIMIT ?;", [random.nextInt(), 1]);
        assert(row != null);
        def id = row.id;
        assert(id != null);
        return id;
    }

    static def pickRandomAirport(sql, random) {
        def row = sql.firstRow("SELECT id FROM airport ORDER BY rand(?) LIMIT ?;", [random.nextInt(), 1]);
        assert(row != null);
        def id = row.id;
        assert(id != null);
        return id;
    }

}
