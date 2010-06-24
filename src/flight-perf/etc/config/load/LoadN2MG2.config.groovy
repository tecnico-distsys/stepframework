/**
 *  Flight Performance load instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        load {
            instance {
                id = "n2mg2"
                numberSessions = 2
                maxGroup = 2
                numberSamples = 2
                randomSeedList = [       1755,
                                   -746289129];
                maxThinkTime = 0
                errorProbability = 0.33
            }
        }
    }
}
