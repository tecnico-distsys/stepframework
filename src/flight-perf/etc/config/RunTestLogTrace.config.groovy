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

                saveLog = true

                perfLogFormat = "perf4j"
                perf4j {
                    aggregateContiguousEntries = true
                }                
            }
        }
    }
}
