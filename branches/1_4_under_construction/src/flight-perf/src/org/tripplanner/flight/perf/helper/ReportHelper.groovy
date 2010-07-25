package org.tripplanner.flight.perf.helper;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import step.groovy.Helper;


/**
 *  Report helper.
 */
class ReportHelper {

    // definitions -------------------------------------------------------------
    
    static final def HR = "HibernateReads";
    static final def HW = "HibernateWrites";
    static final def HE = "HibernateEngine";
    static final def S  = "Service";
    static final def WS = "WebService";
    static final def W  = "Web";
    
    static def getDataHeaderList() {
        def dataHeaderList = [ W, WS, S, HE, HW, HR ];
        return dataHeaderList;
    }


    // calculations ------------------------------------------------------------
    
    /* Convert overall statistics to time slices */
    static def computeTimeSlices(overallStatsMap) {
        assert overallStatsMap

        // convert strings to double values
        def valuesMap = [ : ];
        overallStatsMap.each { key, value ->
            valuesMap[key] = value as Double;
        }

        def slicesMap = [ : ];
        slicesMap[HR] = valuesMap["hibernate_read_time-mean"];
        slicesMap[HW] = valuesMap["hibernate_write_time-mean"];
        slicesMap[HE] = valuesMap["hibernate_time-mean"] - valuesMap["hibernate_read_time-mean"] - valuesMap["hibernate_write_time-mean"];
        slicesMap[S] = valuesMap["si_time-mean"] - valuesMap["hibernate_time-mean"];
        slicesMap[WS] = valuesMap["soap_time-mean"] - valuesMap["si_time-mean"];
        slicesMap[W] = valuesMap["filter_time-mean"] - valuesMap["soap_time-mean"];

        slicesMap.each{ key, value ->
            if (value <= 0.0) {
                printf("Forcing negative value %.2f to 0.0 for %s slice%n", value, key);
                slicesMap[key] = 0.0;
            }
        }

        return slicesMap;     
    }

    // csv ---------------------------------------------------------------------

    static def readOverallStatsCSV(overallStatsFile) {
        def overallStatisticsHeaderList = CSVHelper.getOverallStatisticsHeaderList();
        def overallStatisticsHeaderArray = overallStatisticsHeaderList as String[];   

        CsvMapReader csvMR = new CsvMapReader(new FileReader(overallStatsFile), CsvPreference.STANDARD_PREFERENCE);
        // ignore headers in 1st line
        csvMR.read(overallStatisticsHeaderArray);
        // read data
        def statsMap = csvMR.read(overallStatisticsHeaderArray);
        assert statsMap

        return statsMap;
    }

    static def readVirtualUserOverallStatsCSV(virtualUserOverallStatsFile) {
        def overallStatisticsHeaderList = CSVHelper.getVirtualUserOutputOverallCountsHeaderList();
        def overallStatisticsHeaderArray = overallStatisticsHeaderList as String[];   

        CsvMapReader csvMR = new CsvMapReader(new FileReader(virtualUserOverallStatsFile), CsvPreference.STANDARD_PREFERENCE);
        // ignore headers in 1st line
        csvMR.read(overallStatisticsHeaderArray);
        // read data
        def statsMap = csvMR.read(overallStatisticsHeaderArray);
        assert statsMap

        return statsMap;
    }


    // gnuplot -----------------------------------------------------------------

    static def execGnuplot(reportId, tempDir, plotFile, outputDir) {
        execGnuplot(reportId, tempDir, plotFile, outputDir, true);
    }

    static def execGnuplot(reportId, tempDir, plotFile, outputDir, deleteTempDirFlag) {

        def ant = new AntBuilder();

        ant.copy(todir: tempDir.absolutePath, file: plotFile)

        def command = "gnuplot " + plotFile.name;
        println "Invoking " + command
        Helper.exec(tempDir, command)

        ant.copy(todir: outputDir.absolutePath, overwrite: "true") {
            ant.fileset(dir: tempDir.absolutePath) {
                ant.include(name: "*.gp")
                ant.include(name: "*.dat")

                ant.include(name: "*.gptable")
                ant.include(name: "*.textable")

                ant.include(name: "*.png")
                ant.include(name: "*.tex")
                ant.include(name: "*.pdf")
            }
        }

        if (deleteTempDirFlag)
            ant.delete(dir: tempDir)
    }


    // Graphviz ----------------------------------------------------------------

    static def execGraphvizDot(reportId, tempDir, dotFile, outputDir) {
        execGraphvizDot(reportId, tempDir, dotFile, outputDir, true);
    }

    static def execGraphvizDot(reportId, tempDir, dotFile, outputDir, deleteTempDirFlag) {

        def ant = new AntBuilder();

        def formatList = [ "png", "pdf" ]
        formatList.each { format ->
            def command = "dot -T" + format + " " + dotFile.name + " -o " + reportId + "." + format;
            println "Invoking " + command
            Helper.exec(tempDir, command)
        }

        ant.copy(todir: outputDir.absolutePath, overwrite: "true") {
            ant.fileset(dir: tempDir.absolutePath) {
                ant.include(name: "*.gv")

                ant.include(name: "*.png")
                ant.include(name: "*.pdf")
            }
        }

        ant.delete(dir: tempDir)
    }

}
