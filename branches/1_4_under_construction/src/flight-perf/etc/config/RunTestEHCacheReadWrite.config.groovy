/**
 *  Flight Performance run instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        run {
            instance {
                loadId = "test"
                configId = "ehcachereadwrite"
                numberSamples = 2

                saveLog = true

                perfLogFormat = "perf4j"
                perf4j {
                    aggregateContiguousEntries = true
                }
            }
        }
    }
}
