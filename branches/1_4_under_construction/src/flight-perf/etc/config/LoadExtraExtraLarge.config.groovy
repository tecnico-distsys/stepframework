/**
 *  Flight Performance load instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        load {
            instance {
                id = "xxlarge"
                numberSessions = 50
                maxGroup = 10000
                numberSamples = 30
                randomSeedList = [
                                    -825884733,
                                    -859100750,
                                     610996202,
                                    -147802361,
                                     994922434,
                                     -47177591,
                                    -672438864,
                                    -243367630,
                                    -321857749,
                                     601750476,
                                    -662072740,
                                    -305609431,
                                    -524721290,
                                     589263694,
                                    -950097886,
                                     994058263,
                                    -736486156,
                                     441935478,
                                    -700407079,
                                     525336782,
                                    -572761656,
                                    -204033561,
                                      46219587,
                                    -931952779,
                                     992457703,
                                    -669190661,
                                    -433428381,
                                      39741201,
                                    -790758165,
                                    -974368018
                                ];
                maxThinkTime = 0
                errorProbability = 0.25
            }
        }
    }
}