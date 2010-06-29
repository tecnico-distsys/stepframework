/**
 *  Flight Performance load instance config
 *  (syntax http://groovy.codehaus.org/ConfigSlurper)
 */

perf {
    flight {
        load {
            instance {
                id = "xxxlarge"
                numberSessions = 50
                maxGroup = 20000
                numberSamples = 30
                randomSeedList = [
                                      11511486, 
                                       -138637, 
                                     -15108926,
                                      19476892, 
                                      10630466, 
                                      -8814113, 
                                      57876336, 
                                      -1644081, 
                                     -11849800, 
                                     -15787257,
                                     131102490, 
                                     -21194792, 
                                      -9104119,
                                       1086361, 
                                         79414, 
                                      21406059, 
                                     -11997164, 
                                   -1674220135, 
                                     -13003279, 
                                       7842504, 
                                     -62415388, 
                                      11187438, 
                                        632143, 
                                        335423, 
                                       7352103,
                                      11641119, 
                                      -5396293,
                                        149834, 
                                       1974115, 
                                      15235032  
                                ];
                maxThinkTime = 0
                errorProbability = 0.25
            }
        }
    }
}
