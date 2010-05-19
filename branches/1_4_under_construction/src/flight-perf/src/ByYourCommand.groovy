/**
 *  ByYourCommand - template for groovy commands
 *
 *  Features:
 *  - define settings using command line, property files, or scripting
 *  - run commands using command line or scripting
 */

import org.apache.commons.cli.*;

public class ByYourCommand implements Runnable {

    // --- static ---

    /** Main method - create instance, parse commands, and run! */
    public static void main(String[] args) {
        ByYourCommand instance = new ByYourCommand();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    //
    //  Members
    //

    /** Shortcut for Standard Error output */
    final protected def err = System.err;

    /** Settings */
    public def settings = [ : ];


    //
    //  Initialization
    //

    /** Load settings from properties file */
    public void loadSettingsFromProperties(String propFilePath) {
        def props = new Properties();
        props.load(new FileInputStream(propFilePath));
        Enumeration enumeration = props.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            settings.put(key, props.getProperty(key));
        }
    }


    /**
     *  Handle command line arguments template method.
     **/
    protected boolean handleCommandLineArgs(String[] args) {
        // create command line options
        Options options = this.createCommandLineOptions();

        // parse command line
        CommandLine cmdLine = null;
        try {
            cmdLine = this.parseCommandLine(options, args);
        } catch (ParseException pe ) {
            err.println(pe.getMessage());
            return false;
        }

        // handle options
        return this.handleCommandLineOptions(options, cmdLine);
    }


    /** parseArgs step: create options */
    protected Options createCommandLineOptions() {
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

    /** parseArgs step: handle options */
    protected boolean handleCommandLineOptions(Options options, CommandLine cmdLine) {
        if (!this.handleHelpOption(options, cmdLine))
            return false;
        if (!this.handlePropertiesOption(options, cmdLine))
            return false;

        // copy option values to settings
        for (Option option : cmdLine.options) {
            // key is either long option or short option
            def key = option.longOpt;
            if (key == null) key = option.opt;
            assert(key != null);

            // store value in settings as object or as array
            def valueArray = cmdLine.getOptionValues(key);
            assert(valueArray.length > 0);
            if(valueArray.length == 1) {
                settings.put(key, valueArray[0]);
            } else {
                settings.put(key, valueArray);
            }
        }
        def leftOverArray = cmdLine.getArgs();
        if (leftOverArray.length > 0) {
            settings.put("leftovers", leftOverArray);
        }

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
                    loadSettingsFromProperties(propPaths[idx]);
                }
            } catch (IOException ioe) {
                err.println(ioe.getMessage());
                return false;
            }
        }
        return true;
    }


    //
    //  Runnable
    //

    /** Template method */
    final public void run() {
        if (this.cmdInit()) {
            this.cmdRun();
        }
    }

    /** run step - initialize command using settings */
    protected boolean cmdInit() {
        return true;
    }

    /** run step - run command */
    protected void cmdRun() {
        println("By your command ;-)");
    }

}
