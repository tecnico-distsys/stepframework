package org.tripplanner.flight.perf.report_generator;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import step.groovy.Helper;
import org.tripplanner.flight.perf.*;

def reportId = "LogLevel"
def purpose = "compare impact of different log level settings"

println "Generating " + reportId + " report"
println "to " + purpose

// load initial configuration --------------------------------------------------
def config = Helper.parseConfig("etc/config/Config.groovy");
assert (config.perf.flight) : "Expecting flight performance configuration file"
Helper.configStringToFile(config);

// stats base dir
def statsBaseDir = config.perf.flight.stats.outputBaseDir;
assert (statsBaseDir.exists() && statsBaseDir.isDirectory())

// plot files dir
def plotDir = config.perf.flight.report.plotDir;
assert (plotDir.exists() && plotDir.isDirectory())

def plotFile = new File(plotDir, reportId + ".gp")
assert (plotFile.exists())

// report output dir
def outputDir = config.perf.flight.report.outputDir;
if (!outputDir.exists()) outputDir.mkDir();
assert (outputDir.exists() && outputDir.isDirectory())

// temporary directory
def tempDir = File.createTempFile("report", "");
tempDir.delete();
tempDir.mkdir();
assert tempDir.exists()


// collect data ----------------------------------------------------------------

def loadId = "medium"

def configIdList = ["off", "fatal", "error", "warn", "info", "debug", "trace"];
configIdList = configIdList.collect { "loglevel" + it }

def filterId = ""

// create map of stats files
def statsFileMap = [ : ];
configIdList.each { configId ->
    def dirName = loadId + "_" + configId + "_" + filterId;
    def dir = new File(statsBaseDir, dirName);
    assert (dir.exists() && dir.isDirectory())

    def file = new File(dir, config.perf.flight.stats.overallFileName);
    assert (file.exists())

    statsFileMap[configId] = file;
}

// create data file
def dataFile = new File(tempDir, reportId + ".dat");
def o = new PrintStream(dataFile);

def overallStatisticsHeaderList = CSVHelper.getOverallStatisticsHeaderList();
def overallStatisticsHeaderArray = overallStatisticsHeaderList as String[];

// header
o.println("# type mean error");

configIdList.each { configId ->

    CsvMapReader csvMR = new CsvMapReader(new FileReader(statsFileMap[configId]), CsvPreference.STANDARD_PREFERENCE);
    // ignore headers in 1st line
    csvMR.read(overallStatisticsHeaderArray);

    // read data
    def statsMap = csvMR.read(overallStatisticsHeaderArray);
    assert (statsMap)

    o.printf("%s %s %s%n",
        configId.substring("loglevel".length()),
        statsMap["filter_time-mean"],
        statsMap["filter_time-mean-error-95pctconf"]);
}
o.close();


// invoke gnuplot --------------------------------------------------------------

def ant = new AntBuilder();

ant.copy(todir: tempDir.absolutePath, file: plotFile)

def command = "gnuplot " + plotFile.name;
println "Invoking " + command
Helper.exec(tempDir, command)

ant.copy(todir: outputDir.absolutePath, overwrite: "true") {
    ant.fileset(dir: tempDir.absolutePath) {
        ant.include(name: "*.dat")
        ant.include(name: "*.png")
        ant.include(name: "*.tex")
        ant.include(name: "*.pdf")
    }
}

ant.delete(dir: tempDir)

// -----------------------------------------------------------------------------
println "Report " + reportId + " done!"
println ""
