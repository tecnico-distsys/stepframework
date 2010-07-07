/**
 *  Flight Performance run instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        run {
            instance {
                loadId = "medium"
                configId = "layermon"
                numberSamples = 30

                saveLog = false

                perfLogFormat = "layermon"
            }
        }
    }
}
