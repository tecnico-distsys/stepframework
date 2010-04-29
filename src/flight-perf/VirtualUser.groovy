/**
 *  Groovy command to execute simulated user requests using a provided workload.
 */

import org.apache.commons.cli.*;
import groovy.sql.Sql;

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
            //ClassLoaderHelper.addJarDir(env["STEP_HOME"] + "/lib");
            ClassLoaderHelper.addFile("../framework/dist/stepframework.jar");
            ClassLoaderHelper.addFile("../flight-ws-cli/dist/flight-ws-cli.jar");

            // clear flag
            setupFlag = false;
        }
    }


    // --- instance ---

    File inputFile;

    ObjectInputStream ois;
    String endpoint;


    @Override protected Options createCommandLineOptions() {
        Options options = super.createCommandLineOptions();

        options.addOption(CommandHelper.buildInputOption());
        options.addOption(CommandHelper.buildOutputOption());

        Option endpointOption = new Option("e", "endpoint",
            /* hasArg */ true, "Web Service endpoint address");
        endpointOption.setArgName("url");
        endpointOption.setArgs(1);
        options.addOption(endpointOption);

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
        err.printf("input file %s, endpoint %s",
            inputFile, endpoint);
        err.println();

        // create Web Service stub
        def service = Class.forName("org.tripplanner.flight.wsdl.FlightService").newInstance();
        def port = service.getFlightPort();

        // set endpoint address
        def StubUtil = Class.forName("step.framework.ws.StubUtil");
        StubUtil.setPortEndpointAddress(port, endpoint);

        //
        //  Send requests
        //

        def eof = false;
        while (!eof) {
            try {
                def operation = ois.readObject();

                if ("THINK".equals(operation)) {
                    def thinkSeconds = ois.readObject();
                    if(thinkSeconds > 0) {
                        err.println("Think " + thinkSeconds + " seconds");
                        Thread.sleep(thinkSeconds * 1000);
                    }

                } else if ("SEARCH_FLIGHTS".equals(operation)) {
                    def sfIn = ois.readObject();
                    err.println("Search flights from " + sfIn.depart + " to " + sfIn.arrive);
                    port.searchFlights(sfIn);

                } else if ("CREATE_SINGLE_RESERVATION".equals(operation)) {
                    def csrIn = ois.readObject();
                    err.println("Create single reservation for flight " + csrIn.flightNumber);
                    port.createSingleReservation(csrIn);

                } else if ("CREATE_MULTIPLE_RESERVATIONS".equals(operation)) {
                    def cmrIn = ois.readObject();
                    err.println("Create " + cmrIn.passengers.size() + " reservations for flight " + cmrIn.flightNumber);
                    port.createMultipleReservations(cmrIn);

                } else {
                    throw new RuntimeException("Unknown operation " + operation + ".");
                }

            } catch(EOFException eofe) {
                eof = true;
            } catch(Exception e) {
                err.println("Caught " + e);
                err.println("Proceeding");
            }
        }

        err.println("Done!");

    }

}
