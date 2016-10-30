INTERESTING/EXTRA FEATURES: 
1. Detecting duplicate users - uses streams and inline predicates to detect duplicate users
2. MySQL script creator - the mysqldump command creates a file that when imported into mysql will recreate the pacemaker datastore
3. Comparators v Streams v Factory Pattern - when sorting users and activities I decided that using seperate comparator objects is a more scalable 
	and readable solution. See the listUsers method in the pacemaker api class to see how this has been implemented.
	However, I added a sort method to the UserSortFilter class which demonstrates how you could use java 8 and the factory pattern to implement a sorting strategy.
4. Geocoding - I put together a quick solution to convert latitute longitude coordinates into location descriptions. See the GoogleParser class.
5. Tree view - see the main method printUserTree for a differnt way to view users
6. Java Time v Joda Time v Primitives - Each approach had their own issues, I eventually settled on using java 8s date time and java 8s duration
	Jodas website now reccomends users yse javas own time objects, and using primitive types (ie Long representing usec instead of LocalDateTime) makes code more difficult to follow.
7. I have added proof on concept testing for the main class. By redirecting sysout to a byte stream  in the test setup we can essentially test console output.
8. YAML and JSON serialisers - these were difficult to implement, neither played nice with Longs,LocalDateTime or Duration. As a result, the serialisers 
	read/write methods do some pre processing -> sanitise and normalise
	


KNOWN ISSUES:

1. Duplicate users - 	No two users are allowed to have the same email address.
		    	This is to maintain the integrity of the pacemaker API email index.
2. Datastore - 		There is no automated call to the pacemaker API load method on startup.
3. Validation - 	The pacemaker API makes the general assumption that arguments have already been validated.
			For example, the main view checks that a user exists before asking the pacemaker API to find it.
4. Mocking - The GoogleParser class uses a network connection to convert coordinates to addresses. There are no junit tests in place to test different network scenarios.
			