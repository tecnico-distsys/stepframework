/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "test_loglevelinfo"
                filterId = ""
                numberSamples = 2
                adjustHibernateTimes = true
            }
        }
    }
}
