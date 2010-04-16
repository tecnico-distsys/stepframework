/**
 *  Groovy script to delete all records from database
 */

//
//  check arguments
//

System.err.println("Running " + this.class.getSimpleName() + " with arguments " + args);

//
//  initialization
//

def sql = DBHelper.init();
sql.connection.autoCommit = false;

// delete taking dependencies into account
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

sql.connection.commit();
