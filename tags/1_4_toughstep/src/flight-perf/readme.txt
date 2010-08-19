STEP Framework Flight WS Performance testbed

This subproject provides a testbed to assess the performance of the Flight Web Service.
The code can be adapted to other STEP services and applications.

The source code is written in Groovy, a Java-based scripting language. More information available at:
http://groovy.codehaus.org/

The graphs are plotted using gnuplot. More information available at:
http://www.gnuplot.info/


--

Components / Tools:

- Domain Data Generator - populate the Flight database

- Load Generator - generate a set of requests

- Load Executor - configure server, invoke set of requests, gather performance logs

- Monitor - part of the STEP Framework source code - disabled by default - intercepts the application code to gather performance data

- Analyzer - analyze raw data and produce request records, sample statistics, and overall statistics

- Report Generator - use analysis data to plot ad-hoc graphs


--

Configuration:

The main configuration file is etc/config/Config.groovy.
There are also load, run, and stats configuration files. Each one defines a specific load, run, and stats instance.

One of the most important settings is the 'work directory', where all data files are read from and written to.


--
2010-06-29
Miguel.Pardal@ist.utl.pt
