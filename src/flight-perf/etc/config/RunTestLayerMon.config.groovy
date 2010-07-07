/**
 *  Flight Performance run instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        run {
            instance {
                loadId = "test"
                configId = "layermon"
                numberSamples = 2

                saveLog = true

                perfLogFormat = "layermon"
            }
        }
    }
}
