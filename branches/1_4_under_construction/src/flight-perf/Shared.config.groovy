/**
 *  Flight shared config
 *
 *  Uses Groovy ConfigSlurper syntax
 *  (see http://groovy.codehaus.org/ConfigSlurper for more information)
 *
 *  @author Miguel Pardal
 */

flight {
    shared {
        dir {
            domain = ".\\0_domain-data-generator\\data"
            load = ".\\build\\load"
            config = ".\\2_load-executor\\config"
            run = ".\\build\\log"
            statistics = ".\\build\\stats"
            report = ".\\build\\reports"
        }
        endpoint {
            run = "http://localhost:8080/flight-ws/endpoint"    
        }
        fileName {
            runLog = "flight-ws_log.txt"
            runPerfLog = "flight-ws_perfLog.txt"
            sampleStatistics = "samples-stats.csv"
            overallStatistics = "stats.csv"
        }
        format {
            load = "load-%d.obj"
            runOutput = "load-%d.out"
            runLog = "flight-ws_log-%d.txt"
            runPerfLog = "flight-ws_perfLog-%d.txt"
            requests = "requests-%d.csv"
        }
        pattern {
            load = "Load(.*?).config.groovy"
            run = "Run(.*?).config.groovy"
            statistics = "Statistics(.*?).config.groovy"
        }
    }
}
