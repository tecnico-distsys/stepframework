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
                numberSamples = 5
                randomSeedList = [       -31,
                                   834683468,
                                          12,
                                       94562,
                                      102894];
                maxThinkTime = 0
                errorProbability = 0.20
            }
        }
    }
}
