STEP Framework Demo: Trip Planner

Table of contents
==============================

1. Introduction
2. Structure
3. Compiling
4. Installing
5. Deploying
6. Running
7. Testing
8. Extensions


1. Introduction
==============================

The Trip Planner is a system for selling trip packages via a web interface.
It is composed by several distributed systems, which act together through a
mediator that interacts with the clients.

This demonstration version of the Trip Planner (V3) supports booking a flight
and uses 3 demonstration Framework Extensions:
- hello - just says hi when it intercepts the services or web services
- trace - prints tracing data
- errors - allows creating test errors

Extensions enable the programmer to add code around the services layer
and around the Web Services layer, orthogonally to the application code.
Each extension can be enabled or disabled simply by editing the
extensions.properties configuration file.



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

Two databases must be configured before running the distributed applications.
By default these are:

- For flight:

database.host=localhost
database.name=flight
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

It is now possible to deploy the flight and mediator applications.  Each
application can be deployed by its own build.xml file, but for convenience
there are two top level ant targets to do that.  Execute:

$ ant quick-deploy-flight
$ ant quick-deploy-mediator

If any of these application is already deployed use the corresponding
"redeploy" targets instead.


6. Running
==============================

Point your favourite web browser to http://localhost:8080/mediator to start
using the application. Try booking a flight from Lisbon to New York!



7. Testing
==============================

Testing each application individually is done just like in trip-planner V1,
through the "run-tests" target of each core.  However, running tests in a
distributed system requires all the tables to be stored in the same database.
This is necessary because of the way that DBUnit populates data before each
test method.

To run tests for the distributed application, edit flight/build.properties and
mediator/build.properties and set the same database in both.

Then, at the top level rebuild the applications and create the tables.

$ ant clean build generate-all-db-schemas

Then, redeploy the flight web server

$ ant quick-redeploy-flight

Finally, run the tests for the workflow services in the mediator/web
component:

$ cd mediator/web
$ ant run-tests


8. Extensions
==============================

To see output from the extension engine and from the extensions themselves,
look at the application server's log file.

Feel free to experiment with the extensions configuration files and source code.

The flight extensions configuration is located in flight/ws/resources.
The mediator extensions configuration is located in mediator/web/resources.

