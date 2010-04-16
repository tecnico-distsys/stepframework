/**
 *  Groovy script create the single flight manager
 */

//
//  check arguments
//

System.err.println("Running " + this.class.getSimpleName() + " with arguments " + args);


//
//  initialization
//

def sql = DBHelper.init();

// id is auto incremented
def objVersion = 0;

sql.execute("INSERT INTO flightmanager (objVersion) VALUES (?)", [objVersion]);
