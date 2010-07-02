/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "test_"
                filterId = "reservations"
                numberSamples = 2
                filterClosure = { record ->
                    return (record["soap_name"] ==~ "create(Single|Multiple)Reservation(s)?");
                }
                adjustHibernateTimes = true
            }
        }
    }
}
