/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "medium_loglevelerror"
                filterId = ""
                numberSamples = 30
                adjustHibernateTimes = true
            }
        }
    }
}
