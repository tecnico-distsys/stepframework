/**
 *  Groovy script to remove subtags in perf4j performance log file
 */

System.err.println("Running " + this.class.getSimpleName() + " with arguments " + args);
 
def i = System.in;
def o = System.out;

if(args.length >= 1) {
    i = new File(args[0]);
    assert i.exists();
}

if(args.length >= 2) {
    o = new PrintStream(new File(args[1]));
}

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

