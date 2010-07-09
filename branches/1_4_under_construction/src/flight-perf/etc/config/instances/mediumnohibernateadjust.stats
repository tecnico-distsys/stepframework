/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "medium_"
                filterId = "nohibernateadjust"
                filterClosure = { record ->
                    // no actual filtering; just to provide a filter label
                    return true;
                }
                numberSamples = 30
                adjustHibernateTimes = false
            }
        }
    }
}
