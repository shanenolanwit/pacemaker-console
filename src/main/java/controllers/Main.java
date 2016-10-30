
package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.common.base.Optional;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import enums.ActivitySortFilter;
import enums.FileFormat;
import enums.UserSortFilter;
import exceptions.ValidationException;
import models.Activity;
import models.Location;
import models.User;
import utils.DateTimeUtils;
import utils.DisplayUtils;
import utils.Serializer;
import utils.XMLSerializer;

/**
 * The launch point for the pacemaker console app
 * @author Shane Nolan
 *
 */
public class Main {
	PacemakerAPI paceApi;
	
	/**
	 * List all users details
	 * <p>Equivalent to {@link controllers.Main#getUsers()}</p>
	 */
	@Command(description = "List all users details")
	public void listUsers() {
		List<User> users = new ArrayList<User>(paceApi.getUsers());
		if (!users.isEmpty()) {
			System.out.println("ok");
			System.out.println(DisplayUtils.listUsers(users));
		} else {
			System.out.println("No users found");
		}
	}	
	
	/**
	 * Get all users details
	 * <p>Equivalent to {@link controllers.Main#listUsers()}</p>
	 */
	@Command(description = "Get all users details")
	public void getUsers() {
		listUsers();
	}
	
	/**
	 * List all users in a particular order
	 * @param sortBy  The String identifier for the sort order - valid identifiers are (id|firstname|lastname|email)
	 * <ul>
	 *  <li>{@link enums.UserSortFilter#ID}</li>
	 *  <li>{@link enums.UserSortFilter#FIRSTNAME}</li>
	 *  <li>{@link enums.UserSortFilter#LASTNAME}</li>
	 *  <li>{@link enums.UserSortFilter#EMAIL}</li>
	 * </ul>
	 */
	@Command(description = "List all users details")
	public void listUsers(@Param(name = "sortBy: id|firstname|lastname|email") String sortBy) {
		List<User> users = new ArrayList<User>(paceApi.getUsers());
		if (!users.isEmpty() && UserSortFilter.exists(sortBy)) {
			System.out.println("ok");
			System.out.println(DisplayUtils.listUsers(paceApi.listUsers(sortBy)));
		} else {
			if(users.isEmpty()){
				System.out.println("No users found");
			} else {
				System.out.println("Invalid sort filter");
			}
			
		}
	}
	
	/**
	 * Prints a tree diagram representing the users
	 */
	@Command(description = "Prints a tree diagram representing the users")
	public void printUserTree(){
		List<User> users = new ArrayList<User>(paceApi.getUsers());
		if (!users.isEmpty()) {
			System.out.println("ok");
			paceApi.getPacemakerTree(users).print();
		} else {
			System.out.println("No users found");
		}
	}

	/**
	 * Displays a user associated with a given email address
	 * <p>Equivalent to {@link controllers.Main#getUser(String)}</p>
	 * @param email  A users email address 
	 * @see models.User#email
	 */
	@Command(description = "List a Users details")
	public void listUser(@Param(name = "email") String email) {
		Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
		if (user.isPresent()) {
			System.out.println("ok");
			System.out.println(DisplayUtils.listUser(user.get()));
		}	else {
			System.out.println("User not found");
		}
	}
	
	/**
	 * Displays a user associated with a given email address
	 * <p>Equivalent to {@link controllers.Main#listUser(String)}</p>
	 * @param email  A users email address 
	 * @see models.User#email
	 */
	@Command(description = "Get a Users details")
	public void getUser(@Param(name = "email") String email) {
		listUser(email);
	}
	
	/**
	 * Displays a user associated with a given id
	 * @param id  A users id 
	 * @see models.User#id
	 */
	@Command(description = "List a Users details")
	public void listUser(@Param(name = "id") long id) {
		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent()) {
			System.out.println("ok");
			System.out.println(DisplayUtils.listUser(user.get()));
		}	else {
			System.out.println("No user found");
		}
	}
	
	/**
	 * Creates a new {@link models.User}
	 * @param firstName  String representing a users given name
	 * @param lastName  String representing a users surname
	 * @param email  String representing a users email address
	 * @param password  String representing a users password
	 */
	@Command(description = "Create a new User")
	public void createUser(
			@Param(name = "first name") String firstName, 
			@Param(name = "last name") String lastName,
			@Param(name = "email") String email, 
			@Param(name = "password") String password) {
		if(!paceApi.duplicateUserExists(email)){
			try {
				User u = paceApi.createUser(firstName, lastName, email, password);
				System.out.println("ok");
				System.out.println(DisplayUtils.listUser(u));
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
			}
			
		} else {
			System.out.println("Duplicate users are not allowed");
		}
		
	}
	
	/**
	 * Deletes a user associated with a given id
	 * @param id  A Long representing the users pacemaker app identifier
	 */
	@Command(description = "Delete a User")
	public void deleteUser(@Param(name = "id") long id) {
		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent()) {
			System.out.println("ok");
			paceApi.deleteUser(user.get());
		} else {
			System.out.println("User not found");
		}
	}

	
	
	
	
	/**
	 * List the activities associated with a given {@link models.User}
	 * @see models.Activity
	 * @param id A Long representing the id of the user you are interested in
	 */
	@Command(description = "List details of a specified users activities")
	public void listActivities(@Param(name = "user-id") Long id) {
		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent()) {
			Collection<Activity> activities = paceApi.listActivities(id);
			System.out.println("ok");
			if(activities.size() < 1){
				System.out.println("No activities associated with this user");
			} else {
				System.out.println(DisplayUtils.listActivities(activities));
			}			
		} else {
			System.out.println("User not found");
		}
	}
	
	/**
	 * List the activities associated with a given {@link models.User} in a particular order
	 * @see models.Activity
	 * @see enums.ActivitySortFilter
	 * @param id  A Long representing the id of the user you are interested in
	 * @param sortBy  The String identifier for the sort order - valid identifiers are (id|type|location|distance|date|duration)
	 * <ul>
	 *  <li>{@link enums.ActivitySortFilter#ID}</li>	
	 *  <li>{@link enums.ActivitySortFilter#TYPE}</li>	
	 *  <li>{@link enums.ActivitySortFilter#LOCATION}</li>	
	 *  <li>{@link enums.ActivitySortFilter#DISTANCE}</li>	
	 *  <li>{@link enums.ActivitySortFilter#DATE}</li>	
	 *  <li>{@link enums.ActivitySortFilter#DURATION}</li>	
	 * </ul>
	 */
	@Command(description = "List details of an activity")
	public void listActivities(@Param(name = "user-id") Long id,
			@Param(name = "sortBy: id|type|location|distance|date|duration") String sortBy) {
		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent() && ActivitySortFilter.exists(sortBy)) {			
			Collection<Activity> activities = paceApi.listActivities(id,sortBy);
			System.out.println("ok");
			if(activities.size() < 1){
				System.out.println("No activities associated with this user");
			} else {
				System.out.println(DisplayUtils.listActivities(activities));
			}	
		} else {
			if (user.isPresent()){
				System.out.println("User not found");
			} else{
				System.out.println("Filter not found");
			}			
		}
	}
	
	/**
	 * Lists all activities that have a start datetime between two specified datetimes
	 * <p>Parameters should be provided in the following format "dd:MM:yyyy HH:mm:ss"</p>	
	 * 	<pre>
	 * 		{@code
	 * 			"24:12:2016 00:00:00" "25:12:2016 23:59:59"
	 * 		}
	 *	</pre>
	 * @param start  String representing the earliest datetime for this search range
	 * @param finish  String representing the latest datetime for this search range
	 */
	@Command(description = "Lists activities between two dates")
	public void listActivities(@Param(name = "startDate") String start, 
			@Param(name = "endDate") String finish) {

		if (DateTimeUtils.isValidDate(start) && DateTimeUtils.isValidDate(finish)) {
			System.out.println("ok");
			List<Activity> activities = paceApi.listActivitiesBetweenDates( 
					DateTimeUtils.convertStringToLocalDateTime(start), 
					DateTimeUtils.convertStringToLocalDateTime(finish));
			if(activities.size() < 1){
				System.out.println("No activities found between those dates");
			} else{
				System.out.println(DisplayUtils.listActivities(activities));
			}
		} else {
			System.out.println("Invalid Date Format");
		}
	}
	
	/**
	 * List the locations associated with a given @{link models.Activity}
	 * @see models.Location
	 * @param id  A Long representing the id of the activity you are interested in
	 */
	@Command(description = "List details of an activity")
	public void listLocations(@Param(name = "activity-id") Long id) {
		Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(id));
		if (activity.isPresent()) {
			List<Location> locations = paceApi.listLocations(id);
			System.out.println("ok");
			if(locations.size() > 0){
				System.out.println(DisplayUtils.listLocations(locations));
			} else {
				System.out.println("There are no locations associated with this activity");
			}			
		} else {
			System.out.println("Activity not found");
		}
	}
	
	/**
	 * Adds an {@link models.Activity}
	 * @param id  The user id associated with this activity
	 * @param type  String representing the type of activity
	 * @param location  String representing the location of the activity
	 * @param distance  Double representing the distance in terms of kilometers
	 * @param date  String representing the activities starting datetime ( dd:MM:yyyy H:mm:ss )
	 * @param duration  String representing the duration of the activity ( HH:mm:ss )
	 */
	@Command(description = "Add an activity")
	public void addActivity(@Param(name = "user-id") Long id, @Param(name = "type") String type,
			@Param(name = "location") String location, @Param(name = "distance") double distance,
			@Param(name = "date") String date, @Param(name = "duration") String duration) {

		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent() && DateTimeUtils.isValidDate(date) && DateTimeUtils.isValidDuration(duration)) {
			try{
				Activity activity = paceApi.createActivity(id, type, location, distance, 
						DateTimeUtils.convertStringToLocalDateTime(date), DateTimeUtils.convertStringToDuration(duration));
				System.out.println("ok");
				System.out.println(DisplayUtils.listActivity(activity));
			} catch(ValidationException e){
				System.out.println(e.getMessage());
			}
			
		} else {
			System.out.println("User not found");
		}
	}
	
	/**
	 * Adds coordinates to a given activity
	 * @param id  The id of the activity associated with these coordinates
	 * @param longitude  The coorinates longitude
	 * @param latitude  The coordinates latitude
	 */
	@Command(description = "Add a location to an activity")
	public void addLocation(@Param(name = "activity-id") Long id, @Param(name = "longitude") double longitude,
			@Param(name = "latitude") double latitude) {
		Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(id));
		if (activity.isPresent()) {
			System.out.println("ok");
			paceApi.addLocation(activity.get().id, latitude, longitude);
		} else {
			System.out.println("Activity not found");
		}

	}
	
	/**
	 * Exports the current user/activity data into an MySQL file
	 */
	@Command(description = "Export the data as a MySQL script")
	public void mysqldump() {		
			try {
				File f = paceApi.mysqlDump();
				String msg = "MYSQLDUMP : ";
				if(f.exists()){
					msg += f.getAbsolutePath();
				} else{
					msg += "FAILED";
				}
				System.out.println(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	/**
	 * Changes the serialisation / storage strategy
	 * @param fileFormat  A String representing the desired serialisation strategy
	 *<ul>
	 *  <li>{@link enums.FileFormat#XML}</li>
	 *  <li>{@link enums.FileFormat#JSON}</li>
	 *  <li>{@link enums.FileFormat#BINARY}</li>
	 *  <li>{@link enums.FileFormat#YAML}</li>
	 * </ul>
	 * @see controllers.Main#store()
	 * @see controllers.Main#load()
	 */
	@Command(description = "Change File Format")
	public void changeFileFormat(@Param(name = "file format: xml|json|binary|yaml") String fileFormat) {
		if(FileFormat.exists(fileFormat)){			
			paceApi.changeFileFormat(fileFormat);
			System.out.println("ok");
		} else {
			System.out.println("Invalid file format");
		}		
	}
	
	/**
	 * Loads data from the datastore
	 * @see utils.Serializer#getDataStore()
	 */
	@Command(description = "Load data from the datastore")
	public void load() {		
			try {
				paceApi.load();
			} catch (Exception e) {
				e.printStackTrace();
			}		
	}
	
	/**
	 * Adds data to the datastore
	 * @see utils.Serializer#getDataStore()
	 */
	@Command(description = "Add data to the datastore")
	public void store() {		
			try {
				paceApi.store();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	
	

	public Main() throws Exception {
		
		Serializer serializer = new XMLSerializer();

		paceApi = new PacemakerAPI(serializer);
		
		
	}

	public static void main(String[] args) throws Exception {
		Main main = new Main();

		Shell shell = ShellFactory.createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions",
				main);
		shell.commandLoop();

		main.paceApi.store();
	}

}