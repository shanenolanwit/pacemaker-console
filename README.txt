Persistence approach - standard serializers for xml/binary, modified json and yaml serializers to work with pacemaker 
	(normalise data such as Long keys and Java 8 time objects)
Presentation approach - displays information in tables, some validation performed in presentation layer to ensure clean data passed to the api
	added a mysqldump command that outputs the current datastore to a pacemaker.sql file that can replicate the pacemaker app in mysql
Testing approach - junit 4 and junit 5 (proof of concept), mixture of standard junit asserts and hamcrests assertThats for demonstration purposes and completeness
	added some test for Main class as a proof of concept. Integrated jacoco with gradle build script to provide code coverage report.
Build system approach - used maven and gradle for completeness, added gradle wrapper file for convienience

Components Used ( taken from pom.xml / build.gradle )
jacoco 
commons-lang3:3.0
cliche:110413
guava:19.0
xstream:1.4.8
gson:2.7
yamlbeans:1.09
asciitable:0.2.5
junit:4.12

=======================================================================================================================================================



Run the scripts independently to avoid duplicate data
src/test/resources
main.script -> creates users/activities/locations
userlist.script -> creates users/activities/locations, runs user list commands
activitylist.script -> creates users/activities/locations, runs activity list commands

No auto loads are enabled
If you wish to load data use the load command


INTERESTING/EXTRA FEATURES: 
1. Detecting duplicate users - uses streams and inline predicates to detect duplicate users
2. MySQL script creator - the mysqldump command creates a file that when imported into mysql will recreate the pacemaker datastore
3. Comparators v Streams v Factory Pattern - when sorting users and activities I decided that using seperate comparator objects is a more scalable 
	and readable solution. See the listUsers method in the pacemaker api class to see how this has been implemented.
	However, I added a sort method to the UserSortFilter class which demonstrates how you could use java 8 and the factory pattern to implement a sorting strategy.
4. Geocoding - I put together a quick solution to convert latitute longitude coordinates into location descriptions. See the GoogleParser class and list locations command.
5. Tree view - see the main method printUserTree for a differnt way to view users
6. Java Time v Joda Time v Primitives - Each approach had their own issues, I eventually settled on using java 8s date time and java 8s duration
	Jodas website now reccomends users yse javas own time objects, and using primitive types (ie Long representing usec instead of LocalDateTime) makes code more difficult to follow.
7. I have added proof on concept testing for the main class. By redirecting sysout to a byte stream  in the test setup we can essentially test console output.
8. YAML and JSON serialisers - these were difficult to implement, neither played nice with Longs,LocalDateTime or Duration. As a result, the serialisers 
	read/write methods do some pre processing -> sanitise and normalise
9. Junit 5 tests can be found in the test junit 5 package
10. Gradle/Jacoco have been integrated.
	


KNOWN ISSUES:

1. Duplicate users - 	No two users are allowed to have the same email address.
		    	This is to maintain the integrity of the pacemaker API email index.
2. Datastore - 		There is no automated call to the pacemaker API load method on startup.
3. Validation - 	The pacemaker API makes the general assumption that arguments have already been validated.
			For example, the main view checks that a user exists before asking the pacemaker API to find it.
4. Mocking - The GoogleParser class uses a network connection to convert coordinates to addresses. 
	There are no junit tests in place to test different network scenarios.
5. Unit testing style - Mixture of junit libraries / hamcrest librares / junit4 and junit5 tests mixed together
	This is done on purpose to demonstrate a range of testing options available to the developer
			