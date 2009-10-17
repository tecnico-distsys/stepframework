
*** Tomcat for STEP ***


This file summarizes the installation procedure and describes the 
configuration changes made to prepare the tomcat-step-1.3 package.

The package version number tomcat-step-1.3 refers to the STEP Framework version.
Please refer to webapps/ROOT/index.html for the listing of other included and 
recommended software versions

--

Installation guide:

1 - Install recommended JDK

2 - Install recommended database

3 - Unzip tomcat-step-1.3.zip

4 - Define environment variables

JAVA_HOME C:\Java\jdk\1.6.0_16
CATALINA_HOME C:\Java\tomcat-step-1.3
ANT_HOME C:\Java\tomcat-step-1.3\apache-ant
STEP_HOME C:\Java\tomcat-step-1.3

5 - Extend PATH environment variable to include:
%JAVA_HOME%\bin;%ANT_HOME%\bin;%CATALINA_HOME%\bin;

6 - Open new console and test command line tools:

java -version
javac -version
wsimport -version
xjc -version

ant -version
catalina version

7 - Start server

catalina start
browse http://localhost:8080


--

Configurations steps:

Unzipped  apache-tomcat-6.0.14.zip to tomcat-step-1.3

Unzipped  apache-ant-1.7.1.zip to tomcat-step-1.3/apache-ant

Copied tail.exe (win32 binary) to tomcat-step-1.3/bin

Copied servlet-api.jar and jsp-api.jar to STEP lib

Copied all STEP lib jar files to tomcat-step-1.3/lib

Copied junit-4.5 to tomcat-step-1.3/apache-ant/lib/junit.jar and 
to tomcat-step-1.3/lib/junit.jar

Deleted all tomcat-step-1.3/webapps except ROOT and manager.

Renamed tomcat-step-1.3/webapps/ROOT/index.html to 
tomcat-step-1.3/webapps/ROOT/index-tomcat.html 
Added new index.html to tomcat-step-1.3/webapps/ROOT 
(added STEP logo, quick link to manager)

Added new user with the role manager to 
tomcat-step-1.3/conf/tomcat-users.xml:
    <role rolename="manager"/>
    <user username="admin" password="adminadmin" roles="manager"/>

Documentations were moved to tomcat-step-1.3_docs
Added index.html to tomcat-step-1.3_docs.


--

Acknowledgements:

Apache Software Foundation and 
all other authors of used open-source software packages.

--

Miguel Pardal (miguel.pardal@ist.utl.pt)
2009-10-17
