import org.apache.commons.cli.*;

public class ByYourCommand implements Runnable {

    // --- static ---

    /** Main method - create instance, parse commands, and run! */
    public static void main(String[] args) {
        ByYourCommand instance = new ByYourCommand();
        if (instance.parseArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    //
    //  Members
    //

    /** Shortcut for Standard Error output */
    final protected PrintStream err = System.err;

    /** Settings read from property files */
    protected Properties settings;


    //
    //  Initialization
    //

    /** Initialize objects with default values */
    public Object init() {
        this.setDefaultValues();
        return this;
    }

    /**
     *  Parse arguments template method.
     *  Subclasses should override invoked methods to customize behavior.
     **/
    protected boolean parseArgs(String[] args) {
        Options options = this.createOptions();

        CommandLine cmdLine = null;
        try {
            cmdLine = this.parseCommandLine(options, args);
        } catch (ParseException pe ) {
            err.println(pe.getMessage());
            return false;
        }

        this.setDefaultValues();

        return this.handleOptions(options, cmdLine);
    }


    /** parseArgs step: create options */
    protected Options createOptions() {
        Options options = new Options();

        Option hOption = new Option("h", "help",
            /* hasArg */ false, "print help message");
        options.addOption(hOption);

        Option pOption = new Option("p", "properties",
            /* hasArg */ true, "provide property file(s) with arguments");
        pOption.setArgName("property-file");
        pOption.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(pOption);

        return options;
    }

    /** parseArgs step: parse command line */
    protected CommandLine parseCommandLine(Options options, String[] args) throws ParseException {
        CommandLineParser parser = new GnuParser();
        return parser.parse(options, args);
    }

    /** parseArgs step: set default values */
    protected void setDefaultValues() {
        settings = new Properties();
    }

    /** parseArgs step: handle options */
    protected boolean handleOptions(Options options, CommandLine cmdLine) {
        if (!this.handleHelpOption(options, cmdLine))
            return false;
        if (!this.handlePropertiesOption(options, cmdLine))
            return false;
        return true;    // proceed to run
    }

    /** handling of help command - print help string to System.err */
    protected boolean handleHelpOption(Options options, CommandLine cmdLine) {
        if (cmdLine.hasOption("h")) {
            err.println(this.buildHelpString(options));
            return false;
        } else {
            return true;
        }
    }

    /** build help string using options */
    protected String buildHelpString(Options options) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        String cmdLineSyntax = (this instanceof GroovyObject ? "groovy " : "");
        cmdLineSyntax += this.class.name;

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(printWriter, formatter.defaultWidth,
            cmdLineSyntax, /* header */ null,
            options, formatter.defaultLeftPad,
            formatter.defaultDescPad, /* footer*/ null);

        printWriter.close();
        return stringWriter.toString();
    }

    /** handling of properties option - load properties from all specified file paths */
    protected boolean handlePropertiesOption(Options options, CommandLine cmdLine) {
        if (cmdLine.hasOption("p")) {
            try {
                String[] propPaths = cmdLine.getOptionValues("p");
                for (int idx=0; idx < propPaths.length; idx++) {
                    //err.println("Loading settings from " + propPaths[idx]);
                    settings.load(new FileInputStream(propPaths[idx]));
                }
            } catch (IOException ioe) {
                err.println(ioe.getMessage());
                return false;
            }
        }
        return true;
    }


    /** access setting value giving precedence to command line over loaded properties */
    protected String getSetting(String key, CommandLine cmdLine) {
        Object value = cmdLine.getOptionValue(key);
        if (value == null) {
            value = settings.get(key);
        }
        return value;
    }


    //
    //  Runnable
    //
    public void run() {
        println("By your command ;-)");
    }

}
