# DistAssignment2

This assignment is a hub that provides user's with all the important information when it comes to the NBA teams and players.  Users can access previous season stats in preparation for their fantasy draft or just to see what to expect from players this year.  They can also get live score updates of their favourite team whenever there is a game on.
This assignment uses the jsoup-1.12.1.jar to webscrape for live score updates.  This jar file must be added to the classpath before running the program.

To run the program in the command line start the rmiregistry in the server file. After compiling your .java files run the command java -cp jsoup-1.12.1.jar:. -Djava.security.policy=policy.txt nbaServer for the server and run the regular command for java files for the client.
