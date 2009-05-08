package step.framework.ws.registry;

import java.io.File;

/** Command line interface for JAX-R web service registry access class.
*/
public class Main {

    public static void main(String[] args) {

        if(args.length < 2) {
            System.out.println("usage: {registryConfig} publish|query|delete {parameters}");
            return;
        }

        Registry registry = null;
        try {
            System.out.println("Using registry configuration file: " + args[0]);
            registry = new Registry(new File(args[0]));
            System.out.println(Registry.OPTION_OUTPUT_MESSAGES_PROPERTY + " is " + registry.isOptionOutputMessages());

            System.out.println("Connecting to registry");
            registry.connect(true);

            System.out.println("Registry action is " + args[1]);

            if(args[1].equalsIgnoreCase("publish")) {

                if (args.length < 3) {
                    System.out.println("usage: {registryConfig} publish {registrationFile} {registrationKeyFile}?");
                    return;
                }

                System.out.println("Using registration file: " + args[2]);
                Registration registration = new Registration(new File(args[2]));
                registry.publish(registration);

                String keyFile = null;
                if (args.length == 4) {
                    keyFile = args[3];
                } else {
                    keyFile = args[2];
                }
                System.out.println("Storing key to file: " + keyFile);
                registration.getRegistrationKey().storeKey(new File(keyFile));

            } else if( args[1].equalsIgnoreCase("query")) {

                final String usage = "usage: {registryConfig} query all|namePattern|classification {queryFile}";
                if(args.length < 3) {
                    System.out.println(usage);
                    return;
                }

                System.out.println("Query type is: " + args[2]);

                if(args[2].equalsIgnoreCase("all")) {
                    registry.queryAll();
                } else if(args[2].equalsIgnoreCase("namePattern")) {
                    if(args.length < 4) {
                        System.out.println(usage);
                        return;
                    }
                    System.out.println("Using query file: " + args[3]);

                    NamePatternQuery namePatternQuery = new NamePatternQuery(new File(args[3]));
                    registry.query(namePatternQuery);
                } else if(args[2].equalsIgnoreCase("classification")) {
                    if(args.length < 4) {
                        System.out.println(usage);
                        return;
                    }
                    System.out.println("Using query file: " + args[3]);

                    ClassificationQuery classificationQuery = new ClassificationQuery(new File(args[3]));
                    registry.query(classificationQuery);
                } else {
                    System.out.println(usage);
                    return;
                }

            } else if( args[1].equalsIgnoreCase("delete")) {

                final String usage = "usage: {registryConfig} delete all|key|namePattern|classification {registrationKeyFile}|{queryFile}";
                if(args.length < 3) {
                    System.out.println(usage);
                    return;
                }

                System.out.println("Delete type is: " + args[2]);

                if(args[2].equalsIgnoreCase("all")) {
                    registry.deleteAll();
                } else if(args[2].equalsIgnoreCase("key")) {
                    if(args.length < 4) {
                        System.out.println(usage);
                        return;
                    }
                    System.out.println("Using registration key file: " + args[3]);

                    RegistrationKey registrationKey = new RegistrationKey(new File(args[3]));
                    registry.delete(registrationKey);
                } else if(args[2].equalsIgnoreCase("namePattern")) {
                    if(args.length < 4) {
                        System.out.println(usage);
                        return;
                    }
                    System.out.println("Using name pattern file: " + args[3]);

                    NamePatternQuery query = new NamePatternQuery(new File(args[3]));
                    registry.delete(query);
                } else if(args[2].equalsIgnoreCase("classification")) {
                    if(args.length < 4) {
                        System.out.println(usage);
                        return;
                    }
                    System.out.println("Using classification file: " + args[3]);

                    ClassificationQuery query = new ClassificationQuery(new File(args[3]));
                    registry.delete(query);
                } else {
                    System.out.println(usage);
                    return;
                }

            } else {
                System.out.println("Registry action " + args[1] + " is unknown! Try publish, query or delete.");
            }

        } catch(Exception  e) {
            System.out.println("Caught exception while executing " + args[1]);
            System.out.println("Exception class:   " + e.getClass().toString());
            System.out.println("Exception message: " + e.getMessage());
        } finally {
            try {
                if(registry != null)
                    registry.disconnect();
            } catch(Exception e) {
                System.out.println("Caught exception while disconnecting");
                System.out.println("Exception class:   " + e.getClass().toString());
                System.out.println("Exception message: " + e.getMessage());
            }
        }

    }

}
