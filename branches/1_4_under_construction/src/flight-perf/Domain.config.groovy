/**
 *  Flight Domain config
 *
 *  Uses Groovy ConfigSlurper syntax
 *  (see http://groovy.codehaus.org/ConfigSlurper for more information)
 *
 *  @author Miguel Pardal
 */

flight {
    domain {
        databasePropertiesFilePath = "db.properties"

        namesFileName = "names.txt"
        surnamesFileName = "surnames.txt"

        airports {
            randomSeed = -8354
            fileName = "airports.csv"
            maxCost = 150000
        }
        airplanes {
            randomSeed = 926783
            fileName = "fleet-BA.csv"
            maxCost = 100000
        }
        flights {
            randomSeed = 8845868
            maxCost = 1500
            profit = 0.20
            number = 5640
            maxGroup = 100
        }
    }
}
