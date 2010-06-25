/**
 *  Flight Performance run instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        run {
            instance {
                loadId = "test"
                configId = "ehcachereadonly"
                numberSamples = 2
                aggregatePerfLog = true
                saveLog = true
            }
        }
    }
}
