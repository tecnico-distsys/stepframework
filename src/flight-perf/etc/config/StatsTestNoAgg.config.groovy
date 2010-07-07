/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "test_perf4jnoagg"
                filterId = ""
                numberSamples = 2
                adjustHibernateTimes = false
            }
        }
    }
}
