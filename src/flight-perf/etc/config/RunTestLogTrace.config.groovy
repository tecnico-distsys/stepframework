/**
 *  Flight Performance run instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        run {
            instance {
                loadId = "test"
                configId = "logleveltrace"
                numberSamples = 1
                aggregatePerfLog = true
                saveLog = true
            }
        }
    }
}
