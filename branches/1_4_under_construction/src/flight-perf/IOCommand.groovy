/**
 *  IOCommand - template for groovy commands that accept file input and output
 */
 
import org.apache.commons.cli.*;

public class IOCommand extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        IOCommand instance = new IOCommand();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    //
    //  Members
    //
    InputStream i;
    PrintStream o;


    //
    //  Initialization
    //

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        Option iOption = new Option("i", "input",
            /* hasArg */ true, "input file (stdin by default)");
        iOption.setArgName("file");
        iOption.setArgs(1);
        options.addOption(iOption);

        Option oOption = new Option("o", "output",
            /* hasArg */ true, "output file (stdout by default)");
        oOption.setArgName("file");
        oOption.setArgs(1);
        options.addOption(oOption);

        return options;
    }


    //
    //  Runnable
    //

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (i == null) {
            def iValue = settings["input"];
            if (iValue == null) {
                i = System.in;
            } else {
                i = new FileInputStream(iValue);
            }
        }

        if (o == null) {
            def oValue = settings["output"];
            if (oValue == null) {
                o = System.out;
            } else {
                o = new PrintStream(new FileOutputStream(oValue));
            }
        }

        return true;
    }

    @Override protected void cmdRun() {
        o.println("By your Input/Output command ;-)");
    }

}
