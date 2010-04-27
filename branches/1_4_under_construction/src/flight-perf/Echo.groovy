import org.apache.commons.cli.*;

public class Echo extends IOCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        Echo instance = new Echo();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    //
    //  Runnable
    //
    
    @Override protected void cmdRun() {
        i.eachLine{ line ->
            o.println(line);
        }
    }

}
