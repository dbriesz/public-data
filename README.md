Command line interface application that uses a public data set from WorldBank that has been loaded into an H2 database. Users can interact with and edit the data.  The dataset is a combination of two data sources from WorldBank.  For each country of the world, two indicators (or measures) are included: the number of internet users per 100 during 2013, and the adult literacy rate in 2013, given as the number of literate adults out of 100.

Application uses server mode - in order to run, start the server first with the command line:

java -cp h2*jar org.h2.tools.Server

Once the browser window opens, add the following to the JDBC URL field: jdbc:h2:tcp://localhost/./data/worldbank

The user name is "sa" and there is no password.