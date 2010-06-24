/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "test_"
                filterId = "searches"
                numberSamples = 2
                filterClosure = { record ->
                    return (record["soap_name"] ==~ "searchFlights");
                }
            }
        }
    }
}
