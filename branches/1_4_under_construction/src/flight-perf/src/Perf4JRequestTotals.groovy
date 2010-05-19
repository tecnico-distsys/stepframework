/**
 *  Groovy command to calculate request totals in perf4j performance log file.
 *  Output is in CSV format.
 */

import org.apache.commons.cli.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;


public class RequestTotals extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        RequestTotals instance = new RequestTotals();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    // --- instance ---

    //
    //  Members
    //
    InputStream i;
    File file;


    //
    //  Initialization
    //

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildInputOption());
        options.addOption(CommandHelper.buildFileOption());

        return options;
    }

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (i == null) {
            def iValue = settings[CommandHelper.INPUT_LOPT];
            if (iValue == null) {
                i = System.in;
            } else {
                i = new FileInputStream(iValue);
            }
        }

        if (file == null) {
            def fileValue = settings[CommandHelper.FILE_LOPT];
            if (fileValue == null) {
                err.println("File is missing!");
                return false;
            } else {
                file = new File(fileValue);
            }
        }

        return true;
    }


    //
    //  Runnable
    //

    @Override protected void cmdRun() {

        err.println("Running " + this.class.simpleName);


        //
        //  Define output file structure
        //
        ICsvMapWriter writer = new CsvMapWriter(new FileWriter(file), CsvPreference.STANDARD_PREFERENCE);

        def headerList = ["filter", "soap", "soap-meta", "wsi", "si", "hibernate"];
        final String[] headerArray = new String[headerList.size];
        for (int i = 0; i < headerArray.length; i++) {
            headerArray[i] = headerList[i];
        }

        // write header
        writer.writeHeader(headerArray);


        //
        //  process records
        //

        // init map with all entries empty
        def totalMap = [ : ];
        for (int i = 0; i < headerArray.length; i++) {
            totalMap[headerArray[i]] = "";
        }

        i.eachLine { line ->

            //
            //  Extract current line data
            //
            def lineMatcher = ( line =~ "start\\[(.*)\\] time\\[(.*)\\] tag\\[(.*)\\]" );
            def lineMatcherResult = lineMatcher.matches();
            assert(lineMatcher.matches());

            def start = Long.parseLong(lineMatcher[0][1]);
            def time = Long.parseLong(lineMatcher[0][2]);
            def tag = lineMatcher[0][3];

            // process line

            if(tag ==~ "filter") {
                // process start of new request (implicit end of previous request)

                // dump previous request data
                totalMap[tag] = time;
                writer.write(totalMap, headerArray);

                // reset map
                totalMap = [ : ];
                for (int i = 0; i < headerArray.length; i++) {
                    totalMap[headerArray[i]] = "";
                }

            } else {
                // process request slice

                // match tag
                def tagMatcher = ( tag =~ "([A-Za-z\\-_]+)\\.?(.*)" );
                def tagMatchResult = tagMatcher.matches();
                assert(tagMatchResult);

                def mainTag = tagMatcher[0][1];
                def metaTag = mainTag + "-meta";
                def metaData = tagMatcher[0][2];

                // add value to total
                def total = totalMap[mainTag];
                if (!total) total = 0;
                total += time;
                totalMap[mainTag] = total;

                // store meta-data
                if (totalMap.containsKey(metaTag) && metaData) {
                    totalMap[metaTag] = metaData;
                }
            }

        }

        // close file
        writer.close();

    }

}
