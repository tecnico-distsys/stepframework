/**
 *  Flight Performance run instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        run {
            instance {
                loadId = "test"
                configId = "noagg"
                numberSamples = 2
                aggregatePerfLog = false
                saveLog = true
            }
        }
    }
}
