import org.apache.commons.cli.*;

public class IOCommand extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        IOCommand instance = new IOCommand();
        if (instance.parseArgs(args)) {
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

    @Override protected Options createOptions() {
        Options options = super.createOptions();

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

    @Override protected void setDefaultValues() {
        super.setDefaultValues();
        i = System.in;
        o = System.out;
    }

    @Override protected boolean handleOptions(Options options, CommandLine cmdLine) {
        if (!super.handleOptions(options, cmdLine)) return false;

        String iValue = getSetting("i", cmdLine);
        if (iValue != null) {
            i = new FileInputStream(iValue);
        }

        String oValue = getSetting("o", cmdLine);
        if (oValue != null) {
            o = new PrintStream(new FileOutputStream(oValue));
        }

        return true;
    }


    //
    //  Runnable
    //
    @Override public void run() {
        o.println("By your i/o command!");
    }

}
