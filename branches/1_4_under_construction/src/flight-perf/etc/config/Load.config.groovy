/**
 *  Flight Performance load instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        load {
            instance {
                id = "n5mg5"
                numberSessions = 5
                maxGroup = 5
                numberSamples = 3
                randomSeed = [ -31 ]
                maxThinkTime = 0
                errorProbability = 0.20
            }
        }
    }
}
