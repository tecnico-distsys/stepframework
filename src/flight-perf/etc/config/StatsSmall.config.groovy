/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "small_"
                filterId = ""
                numberSamples = 30
                adjustHibernateTimes = true
            }
        }
    }
}
