/**
 *  Groovy script to calculate request totals in perf4j performance log file
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

def master = "filter";
def totalMap = new HashMap();

i.eachLine { line ->
    lineMatcher = ( line =~ "start\\[(.*)\\] time\\[(.*)\\] tag\\[([A-Za-z]+)\\]" );
    if(lineMatcher.matches()) {
        def lineFormat = "tag[%s] time[%d] percent[%s]%n";

        def tag = lineMatcher[0][3];
        def time = Long.parseLong(lineMatcher[0][2]);
        
        if(lineMatcher[0][3] ==~ "filter") {
            // process start of new request (implicit end of previous request)
            def requestTotal = time;
            
            // dump request data
            for(Object key : totalMap.keySet()) {
                def percent = totalMap[key] / requestTotal;
                o.printf(lineFormat, key, totalMap[key], percent);
            }
            // reset map
            totalMap = new HashMap();
            
            o.printf(lineFormat, tag, requestTotal, "100");
            
        } else {
            // process request slice
            def total = totalMap[tag];
            if(total == null) total = 0;
            total += time;
            
            totalMap[tag] = total;
            System.err.println("new total for " + tag + " is " + total);
        }
    } else {
        o.println(line);
    }
}

