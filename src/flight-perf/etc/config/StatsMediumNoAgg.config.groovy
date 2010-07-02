/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "medium_noagg"
                filterId = ""
                numberSamples = 30
                adjustHibernateTimes = false
            }
        }
    }
}
