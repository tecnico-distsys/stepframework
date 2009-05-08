STEP Framework Demo: Trip Planner 1 - single application version (no Web Services)

Table of contents
==============================

1. Introduction
2. Structure
3. Compiling
4. Installing
5. Deploying
6. Running
7. Testing


1. Introduction
==============================

The Trip Planner is a system for selling trip packages via a web interface.
It is composed by several systems, which act together through a
mediator that interacts with the clients.

This demonstration version of the Trip Planner (V1) supports booking a flight.



2. Structure
==============================

The Trip Planner system is composed by 2 separate domains: flight and
mediator.  The flight domain manages flight reservations.  The mediator domain
mediates the interaction between client and the other reservation domains
(only the flight in this demonstration).

Conceptually, each application consists of 4 layers:

        +--------------+
        | Presentation |
        +--------------+
        | Services     |
        +--------------+
        | Domain model |
        +--------------+
        | Data access  |
        +--------------+

These layers are developed in sub-sub-projects of the flight and the mediator
sub-projects.  The entire project' structure is as follows:

        .
        |-- flight
        |   |-- core
        |   `-- view
        `-- mediator
            |-- core
            |-- view
            `-- web

Both 'core' sub-sub-projects implement all the layers from the bottom up to
the services layer.  The 'view' sub-sub-projects implement the classes that
can be returned from the services to the presentation layer; this also
includes any exceptions that can be thrown.  The 'web' sub-sub-project
implements the presentation layer of the mediator.  There are no presentation
layers implemented for the flight domain.


3. Compiling
==============================

The project uses Apache Ant and the ImportAnt library.  At each level there is
a build file that takes care of building the project at that specific level.
Thus, the entire project can be built simply by invoking at the top level:

$ ant

Note that Ant and JWSDP should be properly installed for this command to work.


4. Installing
==============================

A single database must be configured before running the application.
By default these are:

- For flight:

database.host=localhost
database.name=mediator
database.username=sdesXX
database.password=

- For mediator:

database.host=localhost
database.name=mediator
database.username=sdesXX
database.password=

Therefore, either:

- create the default databases on the localhost's mysql server with the
  configured username and password access, or;

- change in the build.properties files the database information
  (flight/build.properties and mediator/build.properties).

Note: after making changes to the configuration propertied you should rebuild
you applications to ensure that the new values have been correctly propagated
to the packed jars. ("ant clean build" at the top level ensures that)

After the database locations are configured, we need to create the tables and
populate them with some data.

To create the tables execute at the top level:

$ ant generate-all-db-schemas

When executing creating the tables for the first time, some errors like the
following may appear:

"[hibernatetool] Error #1: com.mysql.jdbc.exceptions.MySQLSyntaxErrorException: Table mediator.Client' doesn't exist"

They should all be ignored.

Then check that the tables have been correctly created in each database.

Afterwards, to populate all the tables in both domains, execute:

$ ant populate-all-domains


5. Deploying
==============================

It is now possible to deploy the application.

$ ant quick-deploy-mediator

If the application is already deployed use the corresponding
"redeploy" target instead.


6. Running
==============================

Point your favourite web browser to http://localhost:8080/mediator to start
using the application. Try booking a flight from Lisbon to New York!



7. Testing
==============================

To run tests for one domain at a time:

$ ant run-flight-tests
$ ant run-mediator-tests


To run all tests:

$ ant run-all-tests
