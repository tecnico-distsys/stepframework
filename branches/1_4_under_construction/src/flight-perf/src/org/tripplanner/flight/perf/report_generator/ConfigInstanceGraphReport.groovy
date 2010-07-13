package org.tripplanner.flight.perf.report_generator;

import step.groovy.Helper;

def reportId = "ConfigInstanceGraph"
def purpose = "produce a load, run, and stats configuration instances graph"

// command line options --------------------------------------------------------
def cli = new CliBuilder(usage: "ConfigInstanceGraphReport")
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

// report output dir
def outputDir = config.perf.flight.report.outputDir;
if (!outputDir.exists()) outputDir.mkDir();
assert outputDir.exists() && outputDir.isDirectory()

// temporary directory
def tempDir = File.createTempFile("report", "");
tempDir.delete();
tempDir.mkdir();
assert tempDir.exists()


// collect data from instance files --------------------------------------------

println "Collecting configuration instances data"

def loads = [ ]
def runs = [ ]
def stats = [ ]

def nodes = [ ]
def edges = [ ]

class Edge {
    def source;
    def target;
    def label

}

def loadDir = config.perf.flight.load.instanceDir;
loadDir.eachFile { f ->
    if (f.name ==~ config.perf.flight.load.instanceFileNameRegex) {
        def loadConfig = new ConfigSlurper().parse(f.toURL()).perf.flight.load.instance

        loads.add("L_" + loadConfig.id);

    }
}

def runDir = config.perf.flight.run.instanceDir;
runDir.eachFile { f ->
    if (f.name ==~ config.perf.flight.run.instanceFileNameRegex) {
        def runConfig = new ConfigSlurper().parse(f.toURL()).perf.flight.run.instance

        runs.add("R_" + runConfig.id);

        edges.add(new Edge(source: "L_" + runConfig.loadId,
                           target: "R_" + runConfig.id,
                           label: (runConfig.configId ? runConfig.configId : "")))
    }
}

def statsDir = config.perf.flight.stats.instanceDir;
statsDir.eachFile { f ->
    if (f.name ==~ config.perf.flight.stats.instanceFileNameRegex) {
        def statsConfig = new ConfigSlurper().parse(f.toURL()).perf.flight.stats.instance

        stats.add("S_" + statsConfig.id);

        edges.add(new Edge(source: "R_" + statsConfig.runId,
                           target: "S_" + statsConfig.id,
                           label: (statsConfig.filterId ? statsConfig.filterId : "")))
    }
}

// output graph to file in DOT format (Graphviz) -------------------------------

def dotFile = new File(tempDir, reportId + ".gv");
def out = new PrintStream(new FileOutputStream(dotFile));

println "Writing graph data to " + dotFile.absolutePath

out.println "digraph " + reportId + " {"
loads.each{
    out.println it.toString() + " [shape=box];"
}
runs.each{
    out.println it.toString() + " [shape=ellipse];"
}
stats.each{
    out.println it.toString() + " [shape=invtriangle];"
}
out.println ""
edges.each{ edge ->
    out.println edge.source + " -> " +
        edge.target +
        (edge.label ? " [label=\"" + edge.label + "\"]" : "") + ";";
}
out.println "}"

out.close();


// invoke graphviz -------------------------------------------------------------

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

// -----------------------------------------------------------------------------
println "Report " + reportId + " done!"
println ""

