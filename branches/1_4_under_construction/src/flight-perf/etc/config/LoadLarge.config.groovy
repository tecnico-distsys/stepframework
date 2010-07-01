/**
 *  Flight Performance load instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        load {
            instance {
                id = "large"
                numberSessions = 50
                maxGroup = 1000
                numberSamples = 30
                randomSeedList = [
                                        15669991, 
                                        50699001,
                                      -115780805, 
                                       163245730, 
                                        51627159,
                                            1267,
                                         6859167,
                                        44490750, 
                                      -143260843, 
                                        76150764, 
                                       182130732, 
                                          -15961, 
                                        46225509, 
                                        10648678, 
                                        25160686, 
                                          -15841,
                                       130438305, 
                                           82087, 
                                        -9888548,
                                        19847087,
                                        10029475, 
                                        52149570, 
                                       165839755, 
                                        -1351423, 
                                       167082810, 
                                        -6593697, 
                                            7975, 
                                       197398453,
                                         -105439, 
                                         1208560 
                                ];
                maxThinkTime = 0
                errorProbability = 0.25
            }
        }
    }
}