/**
 *  Flight Performance static config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        sourceCodeDir = ".."
        databasePropertiesFile = "etc/config/db.properties"

//      ------------------------------------------------------------------------
        domain {
            dataDir = "etc/domain-data"

            namesFile = this.perf.flight.domain.dataDir + "/names.txt"
            surnamesFile = this.perf.flight.domain.dataDir + "/surnames.txt"

            airports {
                randomSeed = 6534
                file = this.perf.flight.domain.dataDir + "/airports.csv"
                maxCost = 150000
            }

            airplanes {
                randomSeed = 673
                file = this.perf.flight.domain.dataDir + "/fleet-BA.csv"
                maxCost = 100000
            }

            flights {
                randomSeed = 888
                maxCost = 1500
                profit = 0.20
                number = 564
                maxGroup = 100
            }
        }

//      ------------------------------------------------------------------------
        load {
            instanceDir = "etc/config/testinstances"
            instanceFileNameRegex = "(.*?).load"

            outputBaseDir = "../../../flight-perf-testdata/load"
            outputFileNameFormat = "load-%d.obj"
        }

//      ------------------------------------------------------------------------
        run {
            instanceDir = "etc/config/testinstances"
            instanceFileNameRegex = "(.*?).run"

            configFilesBaseDir = "etc/config/files"

            endpoint = "http://localhost:8080/flight-ws/endpoint"

            logFileName = "flight-ws_log.txt"

            perf4JLogFileName = "flight-ws_perfLog.txt"
            eventMonLogFileNameRegex = "PerfEvent-thr([0-9]+)\\.log"
            layerMonLogFileNameRegex = "PerfLayer-thr([0-9]+)\\.log"

            outputBaseDir = "../../../flight-perf-testdata/run"

            outputSysInfoFileName = "_sys-info.txt"

            outputRunInfoFileName = "_run-info.txt"

            outputFileNameFormat = "run-%d-output.txt"

            outputLogFileNameFormat = "log-%d.txt"
            outputLogSizeFileNameFormat = "log-%d-size.txt"

            outputPerf4JLogFileNameFormat = "Perf4JLog-%d.txt"
            outputEventMonLogFileNameFormat = "EventLog-%d.txt"
            outputLayerMonLogFileNameFormat = "LayerLog-%d.txt"
        }

//      ------------------------------------------------------------------------
        stats {
            instanceDir = "etc/config/testinstances"
            instanceFileNameRegex = "(.*?).stats"

            outputBaseDir = "../../../flight-perf-testdata/stats"

            requestsFileNameFormat = "requests-%d.csv"

            samplesFileName = "samples-stats.csv"
            samplesTextFileName = "samples-stats.txt"

            overallFileName = "stats.csv"
            overallTextFileName = "stats.txt"
        }

//      ------------------------------------------------------------------------
        report {

            plotDir = "etc/plot"

            outputDir = "../../../flight-perf-testdata/report"
        }

    }
}
