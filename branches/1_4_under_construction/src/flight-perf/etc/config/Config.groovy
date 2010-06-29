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
                randomSeed = -8354
                file = this.perf.flight.domain.dataDir + "/airports.csv"
                maxCost = 150000
            }

            airplanes {
                randomSeed = 926783
                file = this.perf.flight.domain.dataDir + "/fleet-BA.csv"
                maxCost = 100000
            }

            flights {
                randomSeed = 8845868
                maxCost = 1500
                profit = 0.20
                number = 5640
                maxGroup = 100
            }
        }

//      ------------------------------------------------------------------------
        load {
            instanceDir = "etc/config"
            instanceFileNameRegex = "Load(.*?).config.groovy"

            outputBaseDir = "../../../flight-perf-work/load"
            outputFileNameFormat = "load-%d.obj"
        }

//      ------------------------------------------------------------------------
        run {
            instanceDir = "etc/config"
            instanceFileNameRegex = "Run(.*?).config.groovy"

            configFilesBaseDir = "etc/config/files"
            defaultConfigId = "default"

            endpoint = "http://localhost:8080/flight-ws/endpoint"
            logFileName = "flight-ws_log.txt"
            perfLogFileName = "flight-ws_perfLog.txt"

            outputBaseDir = "../../../flight-perf-work/run"
            outputFileNameFormat = "run-%d.out"
            outputLogFileNameFormat = "log-%d.txt"
            outputLogSizeFileNameFormat = "logsize-%d.txt"
            outputPerfLogFileNameFormat = "perfLog-%d.txt"
        }

//      ------------------------------------------------------------------------
        stats {
            instanceDir = "etc/config"
            instanceFileNameRegex = "Stats(.*?).config.groovy"

            outputBaseDir = "../../../flight-perf-work/stats"

            requestsFileNameFormat = "requests-%d.csv"
            filteredRequestsFileNameFormat = "requests-%d-filtered.csv"

            samplesFileName = "samples-stats.csv"
            samplesTextFileName = "samples-stats.txt"

            overallFileName = "stats.csv"
            overallTextFileName = "stats.txt"
        }

//      ------------------------------------------------------------------------
        report {

            plotDir = "etc/plot"

            outputDir = "../../../flight-perf-work/report"
        }

    }
}
