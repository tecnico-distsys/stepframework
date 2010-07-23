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
assert plotDir.exists() && plotDir.isDirectory()

def plotFile = new File(plotDir, reportId + ".gp")
assert plotFile.exists()

// report output dir
def outputDir = config.perf.flight.report.outputDir;
if (!outputDir.exists()) outputDir.mkDir();
assert outputDir.exists() && outputDir.isDirectory()

// temporary directory
def tempDir = File.createTempFile("report", "");
tempDir.delete();
tempDir.mkdir();
assert tempDir.exists()


// collect data ----------------------------------------------------------------

def dirNameList = [
    "SizeTest", // for testing only
    "SizeSmall",
    "SizeMedium",
    "SizeLarge",
    "SizeXLarge",
    "SizeXXLarge"
]

// create map of stats files
def statsFileMap = [ : ];
dirNameList.each { dirName ->
    def dir = new File(statsBaseDir, dirName);
    if (!dir.exists()) {
        println "WARNING: " + dirName + " not found."
    } else {
        def file = new File(dir, config.perf.flight.stats.overallFileName);
        assert file.exists()
        statsFileMap[dirName] = file;
    }
}


// create data file ------------------------------------------------------------

def dataFile = new File(tempDir, reportId + ".dat");
def o = new PrintStream(dataFile);

def overallStatisticsHeaderList = CSVHelper.getOverallStatisticsHeaderList();
def overallStatisticsHeaderArray = overallStatisticsHeaderList as String[];

// print data file header
o.printf("# 1-desc");
final def dataHeaderList = [
    "filter_time-mean",
    "soap_time-mean",
    "wsi_time-mean",
    "si_time-mean",
    "hibernate_time-mean",
    "hibernate_read_time-mean",
    "hibernate_write_time-mean"
];
for (int i = 0; i < dataHeaderList.size(); i++) {
    o.printf(" %d-%s", i+2, dataHeaderList.get(i));
}
o.printf("%n");

dirNameList.each { dirName ->
    def file = statsFileMap[dirName];
    if (!file) return;

    CsvMapReader csvMR = new CsvMapReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);
    // ignore headers in 1st line
    csvMR.read(overallStatisticsHeaderArray);
    // read data
    def statsMap = csvMR.read(overallStatisticsHeaderArray);
    assert statsMap

    // compute total XML logical length
    def reqLL = statsMap["soap_request_logical_length-mean"] as Double;
    def respLL = statsMap["soap_response_logical_length-mean"] as Double;
    def ll = (reqLL + respLL) as Long;

    o.printf("%s", ll as String);
    dataHeaderList.each { dataHeader ->
        o.printf(" %s", statsMap[dataHeader]);
    }
    o.printf("%n");

}
o.close();


// invoke gnuplot --------------------------------------------------------------

ReportHelper.execGnuplot(reportId, tempDir, plotFile, outputDir, /* delete temp dir */ false);

def zoomPlotFile = new File(plotDir, reportId + "Zoom.gp")
assert zoomPlotFile.exists()

ReportHelper.execGnuplot(reportId, tempDir, zoomPlotFile, outputDir);


// -----------------------------------------------------------------------------
println "Report " + reportId + " done!"
println ""
