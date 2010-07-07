/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "test_eventmon"
                filterId = ""
                numberSamples = 2
                adjustHibernateTimes = false
            }
        }
    }
}
