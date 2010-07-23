package org.tripplanner.flight.perf.helper;

import step.groovy.Helper;


/**
 *  Report helper.
 */
class ReportHelper {


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

                ant.include(name: "*.table")

                ant.include(name: "*.png")
                ant.include(name: "*.tex")
                ant.include(name: "*.pdf")
            }
        }

        if (deleteTempDirFlag)
            ant.delete(dir: tempDir)
    }


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
