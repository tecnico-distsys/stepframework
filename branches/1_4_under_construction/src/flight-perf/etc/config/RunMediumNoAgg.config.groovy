/**
 *  Flight Performance run instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        run {
            instance {
                loadId = "medium"
                configId = "noagg"
                numberSamples = 30
                aggregatePerfLog = true
                saveLog = false
            }
        }
    }
}
