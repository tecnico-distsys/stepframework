TRIP PLANNER 2.0

DESCRIPTION:

The TripPlanner distributed system is provided as a working example of the
complete STEPframework.  It is made of two applications: Flight, an airline
flight management system; and Mediator, a simple travel agency that allows it's
customers to plan and book trips (currently only flights are possible),
relying on the Flight application to perform the actual flight reservation.

Each application exercises the STEP architecture, having it's domain model
described in DML and implemented with the Fénix Framework, a thin-layer
providing service abstraction and a browser-based interface using GWT.  In
addition, the Flight application also provides a web-services interface for
remote invocation, and exemplifies the use of STEP extensions.


REQUIREMENTS:

- Java SE 6.0 (update 16)
- STEPcat 2.0_6.0.14
  (Apache Tomcat 6.0.14, Apache Ant 1.7.1, GWT 2.0.0 and shared libraries of
  the STEPframework 2.0)


DISTRIBUTION FILES:

- TripPlanner_2.0.zip (applications)
- lib.zip (non-shareable libraries)


CONFIGURATION:

Each application uses it's own database (the Flight application uses a
'flight' database, while the Mediator application uses a 'mediator' database).
Both applications expect a user 'step' with no password to have access to the
configured database.
These configurations are defined in the application's
'src/persistence.properties' file and on its build.xml (on the setup target).


DEPLOYMENT:

1. Unzip the TripPlanner_2.0.zip:
 $ unzip TripPlanner_2.0.zip

2. Unzip lib.zip to directory TripPlanner/flight:
 $ unzip lib.zip -d TripPlanner/flight

3. Unzip lib.zip to directory TripPlanner/mediator:
 $ unzip lib.zip -d TripPlanner/mediator

4. Build and deploy Flight application into STEPcat:
 $ ant -f TripPlanner/flight/build.xml create-war deploy

5. Build and deploy Mediator application into STEPcat:
 $ ant -f TripPlanner/mediator/build.xml create-war deploy

It is now possible to access the Flight application at
http://localhost:8080/flight and the Mediator application at
http://localhost:8080/mediator (assuming STEPcat is running on the same machine
and on the default 8080 port)


ANT TARGETS:

The following describes the essential ant targets:
 - clean: cleans the project of all generated artifacts;
 - compile: compiles the application, including any requried source-code
   generation;
 - build: builds the application from scratch (including the web-services
   client JAR in the Flight application);
 - create-war: builds the WAR (Web application ARchive) of the application;
 - deploy: deploys the WAR created by the create-war target in the STEPcat
   application server;
 - setup: resets the Database and populates it with initial data;
 - run: books a flight;
 - client-run: books a flight through a web-services remote invocation.

 - eclipse-compile: compiles the application and then copies the resulting
   artifacts to the war/WEB-INF/classes directory (pre-configured in the
   Eclipse project to be a classes folder) - required in order to be able to
   use the Eclipse IDE for executing and debugging without Eclipse interfering
   with the build process

The Mediator application requires the Flight web-services client JAR for its
remote invocations, so the Flight application should be built first.
