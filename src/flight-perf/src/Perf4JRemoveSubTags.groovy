/**
 *  Groovy command to remove subtags in perf4j performance log file
 */

public class Perf4JRemoveSubTags extends IOCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        Perf4JRemoveSubTags instance = new Perf4JRemoveSubTags();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    @Override protected void cmdRun() {

        i.eachLine { line ->
            def lineMatcher = ( line =~ "start\\[(.*)\\] time\\[(.*)\\] tag\\[(([A-Za-z]+)(\\..*))\\]" );
            if(lineMatcher.matches()) {
                def newLine = line.replaceFirst(lineMatcher[0][3], lineMatcher[0][4]);
                o.println(newLine);
            } else {
                o.println(line);
            }
        }

    }

}
