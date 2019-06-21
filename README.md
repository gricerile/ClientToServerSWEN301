Randomlog generator: 
Added plugin -> plugin allows maven to find and perform tasks on the randomLogGenerator file, however the logmonitor plugin 
must be commented out so this plugin can run.

mvn clean compile assembly:single -> run this and then it generates executable random.jar file in target folder, remembering 
that the other plugin must be commented out.

java -jar random.jar -> which will then run the jar file created.

LogMonitor Generator:
Added a plugin, however there is a mishap where only one plugin can be run by the maven command at one time, so
the other plugin must be commented out and then the mvn command run to generate the log monitor.

mvn clean compile assembly:single -> generates the log monitor is the other plugin is commented out in the pom
file.

java -jar logMonitor.jar -> runs the log monitor jar file and the program.

Jacoco:
Did not work, jacoco refused and skipped, spent a lot of time finding other dependencies and extentions to fix 
the problem.

Jdepend: There are no two dependencies that reply on each other in the server. No cycles. This is good.

Spot Bugs:

1.Random object created and used only once in nz.ac.vuw.swen301.assignment3.client
.CreateRandomLogs.getRandomMessage()

2.Useless condition: it's known that number != 2 at this point

3.Possible null pointer dereference of getResponse in nz.ac.vuw.swen301.assignment3.client.
LogMonitor$2.actionPerformed(ActionEvent) on exception path

4.Dead store to $L9 in nz.ac.vuw.swen301.assignment3.client.Resthome4LogsAppender.post()

5.Found reliance on default encoding in nz.ac.vuw.swen301.assignment3.client.Resthome4LogsAppender.post(): 
String.getBytes()

The charset is not specified and errors can occur when using the default charset.

6.Possible null pointer dereference in method on exception path

Add check for null potential null pointer to get rid of bug.

7.Dead store to milliSeconds in nz.ac.vuw.swen301.assignment3.client.T1Layout.format(LoggingEvent)

Remove unused field to solve issue.

8.Unread public/protected field: nz.ac.vuw.swen301.assignment3.client.T1Layout.option

Remove unused field to solve issue.

9.Unused field: nz.ac.vuw.swen301.assignment3.client.T1Layout.pattern

Remove unused field to solve issue.

10.Unused field: nz.ac.vuw.swen301.assignment3.client.T1Layout.template

Remove unused field to solve issue.