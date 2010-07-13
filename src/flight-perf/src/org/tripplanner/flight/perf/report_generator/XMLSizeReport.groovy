package org.tripplanner.flight.perf.report_generator;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import step.groovy.Helper;
import org.tripplanner.flight.perf.helper.*;

def reportId = "XMLSize"
def purpose = "compare different SOAP message sizes"

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "XMLSizeReport")
cli.h(longOpt: "help", required: false, args: 0, "Print this message")
cli.cfg(longOpt: "config", required: false, args: 1, "Specify master configuration file")

def options = cli.parse(args)
assert (options)

if (options.help) {
    cli.usage();
    return;
}

// load initial configuration --------------------------------------------------
def configPath = "etc/config/Config.groovy";
if (options.cfg) configPath = options.cfg;

def config = Helper.parseConfig(configPath);
assert (config.perf.flight) : "Expecting flight performance configuration file"
Helper.configStringToFile(config);

// main ------------------------------------------------------------------------
println "Generating " + reportId + " report"
println "to " + purpose

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

// -----------------------------------------------------------------------------

// collect data ----------------------------------------------------------------

def loadIdList = ["small", "medium", "large", "xlarge", "xxlarge", "xxxlarge"];

def configId = ""

def filterId = ""

// create map of stats files
def statsFileMap = [ : ];
loadIdList.each { loadId ->
    def dirName = loadId + "_" + configId + "_" + filterId;
    def dir = new File(statsBaseDir, dirName);
    assert (dir.exists() && dir.isDirectory())

    def file = new File(dir, config.perf.flight.stats.overallFileName);
    assert (file.exists())

    statsFileMap[loadId] = file;
}

// create data file
def dataFile = new File(tempDir, reportId + ".dat");
def o = new PrintStream(dataFile);

def overallStatisticsHeaderList = CSVHelper.getOverallStatisticsHeaderList();
def overallStatisticsHeaderArray = overallStatisticsHeaderList as String[];

// header
o.println("# 1-type 2-web 3-soap 4-wsi 5-si 6-hibernate_r 7-hibernate_w");

loadIdList.each { loadId ->

    CsvMapReader csvMR = new CsvMapReader(new FileReader(statsFileMap[loadId]), CsvPreference.STANDARD_PREFERENCE);
    // ignore headers in 1st line
    csvMR.read(overallStatisticsHeaderArray);
    // read data
    def statsMap = csvMR.read(overallStatisticsHeaderArray);
    assert (statsMap)

    // compute total XML logical length
    def reqLL = statsMap["soap_request_logical_length-mean"] as Double;
    def respLL = statsMap["soap_response_logical_length-mean"] as Double;
    def ll = (reqLL + respLL) as Long;

    o.printf("%s %s %s %s %s %s %s%n",
        ll as String,
        statsMap["filter_time-mean"],
        statsMap["soap_time-mean"],
        statsMap["wsi_time-mean"],
        statsMap["si_time-mean"],
        statsMap["hibernate_read_time-mean"],
        statsMap["hibernate_write_time-mean"]);
}
o.close();


// invoke gnuplot --------------------------------------------------------------

def ant = new AntBuilder();

// additional plot
def plotFile2 = new File(plotDir, reportId + "Zoom.gp")
assert (plotFile2.exists())

ant.copy(todir: tempDir.absolutePath, file: plotFile)
ant.copy(todir: tempDir.absolutePath, file: plotFile2)

def command = "gnuplot " + plotFile.name;
println "Invoking " + command
Helper.exec(tempDir, command)

command = "gnuplot " + plotFile2.name;
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
