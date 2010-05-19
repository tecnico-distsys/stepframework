/**
 *  Groovy command to calculate request totals in perf4j performance log file.
 *  Output is in CSV format.
 */

import org.apache.commons.cli.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;


public class Perf4JRequestRecords extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        Perf4JRequestRecords instance = new Perf4JRequestRecords();
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


    //
    //  Initialization
    //

    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildInputOption());
        options.addOption(CommandHelper.buildOutputOption());

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
        ICsvMapWriter writer = new CsvMapWriter(new FileWriter(oFile), CsvPreference.STANDARD_PREFERENCE);

        // write headers
        def headerList = CSVHelper.getRequestRecordsHeaderList();
        final String[] headerArray = CSVHelper.headerListToArray(headerList);
        writer.writeHeader(headerArray);


        //
        //  process records
        //

        // init map with all entries empty
        def totalMap = [ : ];
        for (int i = 0; i < headerArray.length; i++) {
            totalMap[headerArray[i]] = "";
        }

        def iStream = new FileInputStream(iFile);
        iStream.eachLine { line ->

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
