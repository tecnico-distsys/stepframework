/**
 *  Flight Load config
 *
 *  Uses Groovy ConfigSlurper syntax
 *  (see http://groovy.codehaus.org/ConfigSlurper for more information)
 *
 *  @author Miguel Pardal
 */

flight {
    load {
        id = "n5mg5"
        numberSessions = 5
        maxGroup = 5
        numberSamples = 3
        randomSeed = -31
        maxThinkTime = 0
        errorProbability = 0.20
    }
}
