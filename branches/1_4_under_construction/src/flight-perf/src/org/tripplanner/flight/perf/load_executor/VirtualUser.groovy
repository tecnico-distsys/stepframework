package org.tripplanner.flight.perf.load_executor;

import org.apache.commons.cli.*;

import step.groovy.command.*;
import step.groovy.Helper;
import org.tripplanner.flight.perf.*;


/**
 *  Groovy command to execute simulated user requests using a provided workload.
 */
public class VirtualUser extends ByYourCommand {

    // --- static ---

    //
    //  Command line execution
    //
    public static void main(String[] args) {
        VirtualUser instance = new VirtualUser();
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
            /*
            Helper.addJarDirToClassLoader(env["STEP_HOME"] + "/lib");
            Helper.addFileToClassLoader("../framework/dist/stepframework.jar");
            Helper.addFileToClassLoader("../flight-ws-cli/dist/flight-ws-cli.jar");
*/
            // clear flag
            setupFlag = false;
        }
    }


    // --- instance ---

    File inputFile;
    PrintStream o;

    ObjectInputStream ois;
    String endpoint;


    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildInputOption());

        Option oOption = CommandHelper.buildOutputOption();
        oOption.description = "Output file (stdout by default)";
        options.addOption(oOption);

        options.addOption(CommandHelper.buildEndpointOption());

        return options;
    }

    @Override protected boolean cmdInit() {
        if (!super.cmdInit()) return false;

        if (inputFile == null) {
            inputFile = CommandHelper.initFile(settings[CommandHelper.INPUT_LOPT], null);
            if (inputFile == null) {
                err.println("Input file setting is missing!");
                return false;
            }
        }

        if (o == null) {
            def oValue = settings[CommandHelper.OUTPUT_LOPT];
            if (oValue == null) {
                o = System.out;
            } else {
                o = new PrintStream(new FileOutputStream(oValue));
            }
        }

        if (ois == null) {
            FileInputStream fis = new FileInputStream(inputFile);
            ois = new ObjectInputStream(fis);
        }

        if (endpoint == null) {
            endpoint = settings["endpoint"];
            if (endpoint == null) {
                endpoint = "http://localhost:8080/flight-ws/endpoint";
            }
        }

        return true
    }

    @Override protected void cmdRun() {
        VirtualUser.setup();

        err.println("Running " + this.class.simpleName);
        err.printf("input file %s, endpoint %s", inputFile, endpoint);
        err.println();

        // create Web Service stub
        def service = new org.tripplanner.flight.wsdl.FlightService();
        def port = service.getFlightPort();

        // set endpoint address
        def StubUtil = new step.framework.ws.StubUtil();
        StubUtil.setPortEndpointAddress(port, endpoint);

        //
        //  Send requests
        //

        def sessionCounter = 0; // sessions start with search flights request
        def invocationCounter = 0;

        def eof = false;
        while (!eof) {
            try {
                def operation = ois.readObject();

                if ("THINK".equals(operation)) {
                    def thinkMilliseconds = ois.readObject();
                    if(thinkMilliseconds > 0) {
                        o.println("Think " + thinkMilliseconds + " milliseconds");
                        Thread.sleep(thinkMilliseconds);
                    }

                } else if ("SEARCH_FLIGHTS".equals(operation)) {
                    invocationCounter++;

                    sessionCounter++;
                    o.println("Starting session " + sessionCounter);

                    def sfIn = ois.readObject();
                    o.println("Search flights from " + sfIn.depart + " to " + sfIn.arrive);
                    port.searchFlights(sfIn);

                } else if ("CREATE_SINGLE_RESERVATION".equals(operation)) {
                    invocationCounter++;

                    def csrIn = ois.readObject();
                    o.println("Create single reservation for flight " + csrIn.flightNumber);
                    port.createSingleReservation(csrIn);

                } else if ("CREATE_MULTIPLE_RESERVATIONS".equals(operation)) {
                    invocationCounter++;

                    def cmrIn = ois.readObject();
                    o.println("Create " + cmrIn.passengers.size() + " reservations for flight " + cmrIn.flightNumber);
                    port.createMultipleReservations(cmrIn);

                } else {
                    throw new RuntimeException("Unknown operation " + operation + ".");
                }

            } catch(EOFException eofe) {
                eof = true;
            } catch(Exception e) {
                o.println("Caught " + e + ". Proceeding");
            }

            // report progress
            err.print("Session #" + sessionCounter + ", Invocation #" + invocationCounter + "                    \r");
        }

        o.printf("Processed %d sessions with %d invocations%n", sessionCounter, invocationCounter);

        err.println();
        err.println("Done!");

    }

}
