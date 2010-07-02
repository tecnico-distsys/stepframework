/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "WLANmedium_"
                filterId = ""
                numberSamples = 29  // sample 27 was damaged and replaced by sample 30
                adjustHibernateTimes = true
            }
        }
    }
}
