/**
 *  Flight Performance stats instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        stats {
            instance {
                runId = "medium_"
                filterId = "reservations"
                numberSamples = 30
                filterClosure = { record ->
                    return (record["soap_name"] ==~ "create(Single|Multiple)Reservation(s)?");
                }
                adjustHibernateTimes = true
            }
        }
    }
}
