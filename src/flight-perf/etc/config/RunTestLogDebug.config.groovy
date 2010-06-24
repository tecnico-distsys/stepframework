/**
 *  Flight Performance run instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        run {
            instance {
                loadId = "test"
                configId = "logleveldebug"
                numberSamples = 2
                aggregatePerfLog = true
                saveLog = true
            }
        }
    }
}
