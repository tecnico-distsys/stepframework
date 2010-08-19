package org.tripplanner.flight.perf.report_generator;

import step.groovy.Helper;
import org.tripplanner.flight.perf.helper.*;


def reportId = "LogLevel"
def purpose = "compare impact of different log level settings"

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "LogLevelReport")
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
assert config.perf.flight : "Expecting flight performance configuration file"
Helper.configStringToFile(config);


// main ------------------------------------------------------------------------
println "Generating " + reportId + " report"
println "to " + purpose

// stats base dir
def statsBaseDir = config.perf.flight.stats.outputBaseDir;
assert statsBaseDir.exists() && statsBaseDir.isDirectory()

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
def tempDir = Helper.createTempDir("report" + reportId, "");


// --- metadata ---

def dirNameList = [
    "LogOff",
    "LogFatal",
    "LogError",
    "LogWarn",
    "LogInfo",
    "LogDebug",
    "LogTrace"
]

def descMap = [
    "LogOff"   : "off",
    "LogFatal" : "fatal",
    "LogError" : "error",
    "LogWarn"  : "warn",
    "LogInfo"  : "info",
    "LogDebug" : "debug",
    "LogTrace" : "trace"
];
Helper.doublequoteMapValues(descMap);

def dataHeaderList = [ "filter_time-mean" ];


// data output -----------------------------------------------------------------

// create data file
def dataFile = new File(tempDir, reportId + ".dat");
def o = new PrintStream(dataFile);

// create table file
def tableFile = new File(tempDir, reportId + ".textable");
def oTable = new PrintStream(tableFile);

// print data file header
o.printf("# 1_desc");
for (int i = 0; i < dataHeaderList.size(); i++) {
    o.printf(" %d_%s", i+2, dataHeaderList.get(i));
}
o.printf("%n");

// print table file header
oTable.printf("%% desc");
dataHeaderList.each { dataHeader ->
    oTable.printf(" %s", dataHeader);
}
oTable.printf("%n");

// print data file records
dirNameList.each { dirName ->
    def dir = new File(statsBaseDir, dirName);
    def file = new File(dir, config.perf.flight.stats.overallFileName);
    if (!dir.exists())
        println "WARNING: " + dirName + " not found."
    if (!file.exists())
        return;

    // read overall stats from file
    def statsMap = ReportHelper.readOverallStatsCSV(file);

    // use stats values directly

    // write stats values to data file
    o.printf("%s", descMap[dirName]);
    dataHeaderList.each { dataHeader ->
        o.printf(" %s", statsMap[dataHeader]);
    }
    o.printf("%n");
    // write rows to table file
    oTable.printf("%s", descMap[dirName]);
    dataHeaderList.each { dataHeader ->
        // fixed locale to force "." as decimal separator
        oTable.print(String.format(Locale.US, " & %.2f", 
            (statsMap[dataHeader] as Double)));
    }
    oTable.printf("\\\\%n\\hline%n");
}
o.close();
oTable.close();


// invoke gnuplot --------------------------------------------------------------

ReportHelper.execGnuplot(reportId, tempDir, plotFile, outputDir);


// -----------------------------------------------------------------------------
println "Report " + reportId + " done!"
println ""
