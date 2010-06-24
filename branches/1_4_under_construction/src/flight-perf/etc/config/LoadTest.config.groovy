/**
 *  Flight Performance load instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        load {
            instance {
                id = "test"
                numberSessions = 5
                maxGroup = 10
                numberSamples = 2
                randomSeedList = [       1755,
                                   -746289129];
                maxThinkTime = 0
                errorProbability = 0.20
            }
        }
    }
}
