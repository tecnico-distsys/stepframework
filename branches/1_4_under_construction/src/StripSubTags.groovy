/**
 *  Groovy script to remove subtags in perf4j performance log file
 */

System.err.println("Running " + this.class.getSimpleName() + " with arguments " + args);
def i = FileIOHelper.initI(args);
def o = FileIOHelper.initO(args);


// ~ creates a Pattern
// =~ creates a Matcher
// ==~ tests if String matches the pattern

i.eachLine { line ->
    lineMatcher = ( line =~ "start\\[(.*)\\] time\\[(.*)\\] tag\\[(([A-Za-z]+)(\\..*))\\]" );
    if(lineMatcher.matches()) {
        def newLine = line.replaceFirst(lineMatcher[0][3], lineMatcher[0][4]);
        o.println(newLine);
    } else {
        o.println(line);
    }
}

