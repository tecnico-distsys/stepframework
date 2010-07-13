package org.tripplanner.flight.perf.load_generator;

import org.apache.commons.cli.*;
import groovy.sql.Sql;

import step.groovy.command.*;
import step.groovy.perf.load_generator.RandomNameGenerator;
import org.tripplanner.flight.perf.helper.*;


/**
 *  Groovy command to generate Web Service requests
 */
public class WorkloadGenerator extends DBCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        WorkloadGenerator instance = new WorkloadGenerator();
        if (instance.handleCommandLineArgs(args)) {
            instance.run();
        }
    }


    //
    //  1-time setup
    //
    static setupFlag = true;

    static void setup() {
        if (setupFlag) {
            // load environment properties
            def env = System.getenv();
            // load libraries
            //ClassLoaderHelper.addJarDir(env["STEP_HOME"] + "/lib");
            //ClassLoaderHelper.addFile("../framework/dist/stepframework.jar");
            //ClassLoaderHelper.addFile("../flight-ws-cli/dist/flight-ws-cli.jar");

            // clear flag
            setupFlag = false;
        }
    }


    // --- instance ---

    final def MAX_PASSENGER_ID = Integer.MAX_VALUE;

    Long seed;
    Random random;

    Integer number;

    Integer maxGroup;
    Integer maxThinkTime;

    File namesFile;
    File surnamesFile;

    File outputFile;
    ObjectOutputStream oos;

    Double errorProbability;


    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildSeedOption());
        options.addOption(CommandHelper.buildNrOption());
        options.addOption(CommandHelper.buildMaxGroupOption());

        options.addOption(CommandHelper.buildErrorProbabilityOption());

        Option mttOption = new Option("Mtt", "maxthinktime",
            /* hasArg */ true, "Maximum think time between requests");
        mttOption.setArgName("seconds");
        mttOption.setArgs(1);
        options.addOption(mttOption);

        Option nOption = new Option("nms", "names",
            /* hasArg */ true, "Names file");
        nOption.setArgName("data file");
        nOption.setArgs(1);
        options.addOption(nOption);

        Option snOption = new Option("snms", "surnames",
            /* hasArg */ true, "Surnames file");
        snOption.setArgName("data file");
        snOption.setArgs(1);
        options.addOption(snOption);

        options.addOption(CommandHelper.buildOutputOption());

        return options;
    }

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (seed == null) {
            seed = CommandHelper.initLong(settings[CommandHelper.SEED_LOPT], null);
        }

        if (random == null) {
            random = CommandHelper.initRandom(seed);
        }

        if (number == null) {
            number = CommandHelper.initInteger(settings[CommandHelper.NR_LOPT], null);
            if (number == null) {
                err.println("Number is missing!");
                return false;
            } else if (number <= 0) {
                err.println("Number can not be negative or zero!");
                return false;
            }
        }

        if (maxGroup == null) {
            maxGroup = CommandHelper.initInteger(settings[CommandHelper.MAX_GROUP_LOPT], null);
            if (maxGroup == null) {
                err.println("Maximum group size is missing!");
                return false;
            } else if (maxGroup <= 0) {
                err.println("Maximum group size can not be negative or zero!");
                return false;
            }
        }

        if (errorProbability == null) {
            errorProbability = CommandHelper.initDouble(settings[CommandHelper.ERROR_PROB_LOPT], 0.0);
            if (errorProbability < 0.0 || errorProbability > 1.0 ) {
                err.println("Error probability value must be inside range 0.0 to 1.0!");
                return false;
            }
        }

        if (maxThinkTime == null) {
            maxThinkTime = CommandHelper.initInteger(settings["maxthinktime"], null);  // in seconds
            if (maxThinkTime == null) {
                err.println("Maximum think time is missing!");
                return false;
            } else if (maxThinkTime < 0) {
                err.println("Maximum think time can not be negative!");
                return false;
            }
        }

        if (namesFile == null) {
            namesFile = CommandHelper.initFile(settings["names"], null);
            if (namesFile == null || !namesFile.exists()) {
                err.println("Names file is missing or does not exist!");
                return false;
            }
        }

        if (surnamesFile == null) {
            surnamesFile = CommandHelper.initFile(settings["surnames"], null);
            if (surnamesFile == null || !surnamesFile.exists()) {
                err.println("Surnames file is missing or does not exist!");
                return false;
            }
        }

        if (outputFile == null) {
            outputFile = CommandHelper.initFile(settings[CommandHelper.OUTPUT_LOPT], null);
            if (outputFile == null) {
                err.println("Output file setting is missing!");
                return false;
            }
        }

        if (oos == null) {
            FileOutputStream fos = new FileOutputStream(outputFile);
            oos = new ObjectOutputStream(fos);
        }

        return true
    }


    @Override protected void dbRun() {
        WorkloadGenerator.setup();

        err.println("Running " + this.class.simpleName);
        err.printf("output %d request sessions to %s, name data files %s %s, max group %d, max think time %d, error probability %.3f",
            number, outputFile, namesFile, surnamesFile, maxGroup, maxThinkTime, errorProbability);
        if(seed != null) err.printf(", random seed %d", seed);
        err.println();

        // name generator
        RandomNameGenerator nameGenerator = new RandomNameGenerator(namesFile.path, surnamesFile.path, random);

        // operation identifier string
        def operation;

        def count = 0;
        def errorCount = 0;
        while(count < number) {
            count++;

            //
            //  generate search flights request
            //
            operation = "SEARCH_FLIGHTS";

            def flight = FlightDBHelper.pickRandomFlight(sql, random);

            def depart = FlightDBHelper.getAirport(sql, flight.origin_id).city;
            def arrive = FlightDBHelper.getAirport(sql, flight.destination_id).city;

            // simulate user input error
            if (random.nextDouble() < errorProbability) {
                errorCount++;
                depart += " intentional mistake";
                arrive += " intentional mistake";
            }

            def sfIn = Class.forName("org.tripplanner.flight.view.SearchFlightsInput").newInstance();
            sfIn.depart = depart;
            sfIn.arrive = arrive;

            oos.writeObject(operation);
            oos.writeObject(sfIn);


            //
            //  generate think time
            //
            generateThinkTime(oos);


            //
            //  generate create reservations
            //
            def groupSize = random.nextInt(maxGroup);

            if(groupSize == 0) {
                // no reservation

            } else if(groupSize == 1) {
                //
                //  generate create single reservation
                //
                operation = "CREATE_SINGLE_RESERVATION";

                def csrIn = Class.forName("org.tripplanner.flight.view.CreateSingleReservationInput").newInstance();
                csrIn.flightNumber = flight.number;
                def singlePassenger = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
                singlePassenger.id = random.nextInt(MAX_PASSENGER_ID);
                singlePassenger.name = nameGenerator.nextName();
                csrIn.passenger = singlePassenger;

                // simulate user input error
                if (random.nextDouble() < errorProbability) {
                    errorCount++;
                    csrIn.flightNumber = "intentional mistake " + csrIn.flightNumber;
                }

                oos.writeObject(operation);
                oos.writeObject(csrIn);

            } else {
                //
                //  generate multiple reservations
                //
                operation = "CREATE_MULTIPLE_RESERVATIONS";

                def cmrIn = Class.forName("org.tripplanner.flight.view.CreateMultipleReservationsInput").newInstance();
                cmrIn.flightNumber = flight.number;

                def passengerList = cmrIn.passengers;

                for(int i = 0; i < groupSize; i++) {
                    def groupPassenger = Class.forName("org.tripplanner.flight.view.PassengerView").newInstance();
                    groupPassenger.id = random.nextInt(MAX_PASSENGER_ID);
                    groupPassenger.name = nameGenerator.nextName();
                    passengerList.add(groupPassenger);
                }

                // simulate user input error
                if (random.nextDouble() < errorProbability) {
                    errorCount++;
                    cmrIn.flightNumber = "intentional mistake " + cmrIn.flightNumber;
                }

                oos.writeObject(operation);
                oos.writeObject(cmrIn);

            }


            //
            //  generate think time
            //
            generateThinkTime(oos);

        }


        //
        //  Finish
        //
        oos.close();

        err.printf("Generated %d request sessions with %d intentional request errors%n",
            count, errorCount);
        err.println("Done!");

    }


    private def generateThinkTime(oos) {
        def operation = "THINK";

        oos.writeObject(operation);
        if (maxThinkTime == 0) {
            // think time will be zero, no need to randomize
            oos.writeObject(0);
        } else {
            oos.writeObject(random.nextInt(maxThinkTime * 1000));
        }
    }

}
