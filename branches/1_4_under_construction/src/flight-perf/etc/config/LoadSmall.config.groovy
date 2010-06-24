/**
 *  Flight Performance load instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        load {
            instance {
                id = "small"
                numberSessions = 50
                maxGroup = 10
                numberSamples = 30
                randomSeedList = [
                                         326265, 
                                      209304811, 
                                       51190515, 
                                       44840055,
                                      138720126, 
                                        1996033, 
                                       -4528235,
                                        1923772,
                                      178253926, 
                                       30226030, 
                                        -581665,
                                       97928046,
                                       41826168, 
                                        3525740, 
                                         903810,
                                      127190033, 
                                       36884707, 
                                        3537054, 
                                      -67954739, 
                                        7556758, 
                                         179176, 
                                       19384899,
                                      192888118, 
                                      -72966628,
                                         692348, 
                                        3337547, 
                                        1258893,
                                       36237365, 
                                      -12439698, 
                                       36194170 
                                ];
                maxThinkTime = 0
                errorProbability = 0.25
            }
        }
    }
}
