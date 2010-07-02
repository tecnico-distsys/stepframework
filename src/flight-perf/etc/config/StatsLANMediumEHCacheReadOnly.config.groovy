/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "LANmedium_ehcachereadonly"
                filterId = ""
                numberSamples = 30
                adjustHibernateTimes = true
            }
        }
    }
}
