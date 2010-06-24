/**
 *  Flight Performance load instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        load {
            instance {
                id = "n100mg10"
                numberSessions = 100
                maxGroup = 10
                numberSamples = 30
                randomSeedList = [       -31,      6383,    175522,     -2355,      6129,
                                   834683468,    234423,    114563,   -121234,       963,
                                          12,      8907,      2357,       754,     -3434,
                                       94562,  34834267,   8999411,   -237890,     54854,
                                      102894,      5610,    993672,       -23,      1231,
                                    69736383,     -3443,  56423211,   7854901,       -84,
                                      653200,      -856,       -88,      1675,     98465,
                                      662921,       -12,      3143, 874562353,  -1154564,
                                     -345836,      9533,   6202111,     45937,  -3898250,
                                      342564, 567623400,   7645688,   -974398,       655 ];
                maxThinkTime = 0
                errorProbability = 0.20
            }
        }
    }
}
