package org.tripplanner.flight.perf.analyzer;

import org.apache.commons.cli.*;

import org.supercsv.io.*;
import org.supercsv.prefs.*;

import org.apache.commons.math.stat.*;
import org.apache.commons.math.stat.descriptive.*;

import step.groovy.command.*;
import org.tripplanner.flight.perf.helper.*;


/**
 *  Groovy command to compute Hibernate average reduction and to apply it to existing records.
 *  Input and Output is in CSV format.
 */
public class CSVAdjustHibernateRecords extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        CSVAdjustHibernateRecords instance = new CSVAdjustHibernateRecords();
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

    Integer number;

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


        def recordsHeaderList = CSVHelper.getRequestRecordHeaderList();
        def recordsHeaderArray = recordsHeaderList as String[];


        // first pass to compute average reduction rate ------------------------

        CsvMapReader csvMR = new CsvMapReader(new FileReader(iFile), CsvPreference.STANDARD_PREFERENCE);
        int recordNr = 0;

        // ignore 1st line (headers)
        csvMR.read(recordsHeaderArray);

        //  Initialize statistics object
        def stats = new SummaryStatistics();

        // process records
        def record = null;
        while ( (record = csvMR.read(recordsHeaderArray)) != null ) {
            recordNr++;

            def totalHibernate = (record["hibernate_read_time"] as Long) + (record["hibernate_write_time"] as Long);
            assert (totalHibernate >= 0)

            if ((record["si_time"] as Long) < totalHibernate) {
                def difference = totalHibernate - (record["si_time"] as Long);
                assert (difference > 0)

                // reduction rate = 1.0 - (value after / value before)
                double reductionRate = 1.0 - ((totalHibernate - difference) / (totalHibernate));
                stats.addValue(reductionRate);

                printf "Reduction rate for record %d is %.2f%n", recordNr, reductionRate
            }
        }
        csvMR.close();

        // compute average reduction rate
        double averageRedRate = stats.getMean();
        if (averageRedRate.isNaN()) averageRedRate = 0.0;
        printf "Computed average reduction rate is %.2f%n", averageRedRate

        // 2nd pass - to apply reduction ---------------------------------------

        csvMR = new CsvMapReader(new FileReader(iFile), CsvPreference.STANDARD_PREFERENCE);
        recordNr = 0;
        // ignore 1st line (headers)
        csvMR.read(recordsHeaderArray);

        // write headers
        CsvListWriter csvLW = new CsvListWriter(new FileWriter(oFile), CsvPreference.STANDARD_PREFERENCE);
        csvLW.write(recordsHeaderArray);
        csvLW.close();

        CsvMapWriter csvMW = new CsvMapWriter(new FileWriter(oFile, true), CsvPreference.STANDARD_PREFERENCE);

        // process records
        record = null;
        while ( (record = csvMR.read(recordsHeaderArray)) != null ) {
            recordNr++;

            def totalHibernate = (record["hibernate_read_time"] as Long) + (record["hibernate_write_time"] as Long);
            assert (totalHibernate >= 0)

            if (totalHibernate == 0)
                continue;

            double hrRate = (record["hibernate_read_time"] as Long) / totalHibernate;
            double hwRate = (record["hibernate_write_time"] as Long) / totalHibernate;

            if ((record["si_time"] as Long) < totalHibernate) {
                // apply actual reduction

                def difference = totalHibernate - (record["si_time"] as Long);
                assert (difference > 0)

                // reduction rate = 1.0 - (value after / value before)
                double reductionRate = 1.0 - ((totalHibernate - difference) / (totalHibernate));

                record["hibernate_read_time"] = (((record["hibernate_read_time"] as Double) - difference * hrRate) as Long) as String;
                record["hibernate_write_time"] = (((record["hibernate_write_time"] as Double) - difference * hwRate) as Long) as String;

            } else {
                // apply average reduction
                if (averageRedRate > 0.0) {
                    record["hibernate_read_time"] = (((record["hibernate_read_time"] as Double) * (1.0 - averageRedRate) * hrRate) as Long) as String;
                    if ((record["hibernate_read_time"] as Long) < 0L) record["hibernate_read_time"] = (0L as String);

                    record["hibernate_write_time"] = (((record["hibernate_write_time"] as Double) * (1.0 - averageRedRate) * hwRate) as Long) as String;
                    if ((record["hibernate_write_time"] as Long) < 0L) record["hibernate_write_time"] = (0L as String);
                }
            }

            // write adjusted record
            csvMW.write(record, recordsHeaderArray);
        }
        csvMR.close();
        csvMW.close();

    }

}
