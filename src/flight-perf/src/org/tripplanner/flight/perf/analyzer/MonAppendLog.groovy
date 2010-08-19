package org.tripplanner.flight.perf.analyzer;

import org.apache.commons.cli.*;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import step.groovy.command.*;
import org.tripplanner.flight.perf.helper.*;


/**
 *  Groovy command to append thread log to master log
 */
public class MonAppendLog extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        MonAppendLog instance = new MonAppendLog();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    //
    //  Members
    //
    File iFile;
    File oFile;

    String regex;


    //
    //  Initialization
    //

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildInputOption());
        options.addOption(CommandHelper.buildOutputOption());
        options.addOption(CommandHelper.buildRegexOption());

        return options;
    }

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (iFile == null) {
            def iValue = settings[CommandHelper.INPUT_LOPT];
            if (iValue == null) {
                err.println("Input file is missing!");
                return false;
            } else {
                iFile = new File(iValue);
            }
        }

        if (oFile == null) {
            def oValue = settings[CommandHelper.OUTPUT_LOPT];
            if (oValue == null) {
                err.println("Output file is missing!");
                return false;
            } else {
                oFile = new File(oValue);
            }
        }

        if (regex == null) {
            def regexValue = settings[CommandHelper.REGEX_LOPT];
            if (regexValue == null) {
                err.println("Regular expression for input file is missing!");
                return false;
            } else {
                regex = regexValue;
            }
        }

        return true;
    }


    //
    //  Runnable
    //

    @Override protected void cmdRun() {

        err.println("Running " + this.class.simpleName);

        // extract thread id from file name
        def matcher = ( iFile.name =~ regex );
        def matcherResult = matcher.matches();
        assert matcherResult
        def thrId = matcher.group(1);

        // append to file
        def oFileWriter = new FileWriter(oFile, /* append */ true);
        iFile.eachLine{ line ->
            String stringToWrite;
            if (line.length() == 0) {
                // write separator (empty line)
                stringToWrite = String.format("%n");
            } else {
                // write record headed by thread id
                stringToWrite = String.format("thread[%s] %s%n", thrId as String, line);
            }
            oFileWriter.write(stringToWrite);
        }
        // close file
        oFileWriter.close();

    }

}
