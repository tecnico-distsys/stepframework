package org.tripplanner.flight.perf.report_generator;

import step.groovy.Helper;
import org.tripplanner.flight.perf.helper.*;


def reportId = "Users"
def purpose = "compare simultaneous users"

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "UsersReport")
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
    "SizeMedium",
    "Users2",
    "Users4",
    "Users8",
    "Users16",
]

def descMap = [
    "SizeMedium" : "1",
    "Users2"     : "2",
    "Users4"     : "4",
    "Users8"     : "8",
    "Users16"    : "16",
];
Helper.doublequoteMapValues(descMap);

def dataHeaderList = ReportHelper.getDataHeaderList();


// data output -----------------------------------------------------------------

// create data files
def dataFile = new File(tempDir, reportId + ".dat");
def o = new PrintStream(dataFile);

def dataFile2 = new File(tempDir, reportId + "Errors.dat");
def o2 = new PrintStream(dataFile2);


// create table file
def tableFile = new File(tempDir, reportId + ".textable");
def oTable = new PrintStream(tableFile);

// print data files header
o.printf("# 1_desc");
for (int i = 0; i < dataHeaderList.size(); i++) {
    o.printf(" %d_%s", i+2, dataHeaderList.get(i));
}
o.printf("%n");


o2.printf("# 1_desc 2_requests 3_apperr 4_syserr");
o2.printf("%n");


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
    def file2 = new File(dir, config.perf.flight.stats.virtualUserOutputOverallFileName);
    if (!dir.exists())
        println "WARNING: " + dirName + " not found."
    if (!file.exists() || !file2.exists())
        return;

    // read stats from files
    def statsMap = [ : ]
    ReportHelper.readOverallStatsCSV(file).each { k, v ->
        statsMap[k] = v;
    }
    ReportHelper.readVirtualUserOverallStatsCSV(file2).each { k, v ->
        statsMap[k] = v;
    }

    // convert to time slices
    def slicesMap = ReportHelper.computeTimeSlices(statsMap)

    // write slice values to data file
    o.printf("%s", descMap[dirName]);
    dataHeaderList.each { dataHeader ->
        o.printf(" %s", slicesMap[dataHeader]);
    }
    o.printf("%n");

    // compute total for percentages
    def total = 0.0;
    slicesMap.each{ k, v ->
        total += v;
    }

    // write rows to table file
    oTable.printf("%s", descMap[dirName]);
    dataHeaderList.each { dataHeader ->
        // fixed locale to force "." as decimal separator
        oTable.print(String.format(Locale.US, " & %.2f (%.2f%%)", 
            slicesMap[dataHeader], slicesMap[dataHeader] / total * 100));
    }
    oTable.printf("\\\\%n\\hline%n");

    // compute request totals
    def requests = 0.0;
    def sysErr = 0.0;
    def appErr = 0.0;
    statsMap.each { k, v ->
        if (k ==~ "request-(.*)-mean") {
            requests += (v as Double);
        } else if (k.equals("exception-ServiceUnavailable_Exception-mean") || k.equals("exception-SOAPFaultException-mean")) {
            sysErr += (v as Double);
        } else if (k ==~ "exception-(.*)-mean") {
            appErr += (v as Double);
        }
    }
    o2.printf("%s %s %s %s%n", 
        descMap[dirName].replaceAll("\"", ""), 
        requests as String, appErr as String, sysErr as String);
}
o.close();
o2.close();
oTable.close();


// invoke gnuplot --------------------------------------------------------------

ReportHelper.execGnuplot(reportId, tempDir, plotFile, outputDir, /* delete temp dir */ false);

def errorsPlotFile = new File(plotDir, reportId + "Errors.gp")
assert errorsPlotFile.exists()
ReportHelper.execGnuplot(reportId, tempDir, errorsPlotFile, outputDir);


// -----------------------------------------------------------------------------
println "Report " + reportId + " done!"
println ""
