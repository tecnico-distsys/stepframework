/**
 *  Flight Performance run instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        run {
            instance {
                loadId = "medium"
                configId = "perf4jnoagg"
                numberSamples = 30

                saveLog = false

                perfLogFormat = "perf4j"
                perf4j {
                    aggregateContiguousEntries = false
                }
            }
        }
    }
}
