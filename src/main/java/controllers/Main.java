package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import enums.ActivitySortFilter;
import enums.FileFormat;
import enums.UserSortFilter;
import models.Activity;
import models.Location;
import models.User;
import utils.DateTimeUtils;
import utils.DisplayUtils;
import utils.GoogleParser;
import utils.Serializer;
import utils.XMLSerializer;

public class Main {
	PacemakerAPI paceApi;
	
	/**
	 * Lists all the users
	 */
	@Command(description = "List all users details")
	public void listUsers() {
		List<User> users = new ArrayList<User>(paceApi.getUsers());
		if (!users.isEmpty()) {
			System.out.println(DisplayUtils.listUsers(users));
		} else {
			System.out.println("No users found");
		}
	}	
	
	/**
	 * 
	 * @param sortBy
	 */
	@Command(description = "List details of an activity")
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
	 * Lists all the users
	 */
	@Command(description = "Get all users details")
	public void getUsers() {
		listUsers();
	}

	/**
	 * Creates a new user
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 */
	@Command(description = "Create a new User")
	public void createUser(
			@Param(name = "first name") String firstName, 
			@Param(name = "last name") String lastName,
			@Param(name = "email") String email, 
			@Param(name = "password") String password) {
		if(Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", email)){
			User u = paceApi.createUser(firstName, lastName, email, password);
			System.out.println("ok");
			System.out.println(DisplayUtils.listUser(u));
		} else {
			System.out.println("Invalid email format [ " + email + " ]");
		}
		
	}
	
	/**
	 * Displays a user found using their email address
	 * @param email
	 */
	@Command(description = "List a Users details")
	public void listUser(@Param(name = "email") String email) {
		Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
		if (user.isPresent()) {
			System.out.println(DisplayUtils.listUser(user.get()));
		}	else {
			System.out.println("User not found");
		}
	}

	@Command(description = "Get a Users details")
	public void getUser(@Param(name = "email") String email) {
		listUser(email);
	}
	
	/**
	 * Displays a user found using their id
	 * @param id
	 */
	@Command(description = "List a Users details")
	public void listUser(@Param(name = "id") long id) {
		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent()) {
			System.out.println(DisplayUtils.listUser(user.get()));
		}	else {
			System.out.println("No user found");
		}
	}
	
	/**
	 * List the activities associated with a given user
	 * @param id The id of the user you are interested in
	 */
	@Command(description = "List details of an activity")
	public void listActivities(@Param(name = "user-id") Long id) {
		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent()) {
			Collection<Activity> activities = paceApi.listActivities(id);
			System.out.println(DisplayUtils.listActivities(activities));
		} else {
			System.out.println("User not found");
		}
	}
	
	/**
	 * List the locations associated with a given activity
	 * @param id The id of the activity you are interested in
	 */
	@Command(description = "List details of an activity")
	public void listLocations(@Param(name = "activity-id") Long id) {
		Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(id));
		if (activity.isPresent()) {
			List<Location> locations = paceApi.listLocations(id);
			System.out.println(DisplayUtils.listLocations(locations));
		} else {
			System.out.println("Activity not found");
		}
	}
	
	/**
	 * 
	 * @param id
	 * @param sortBy
	 */
	@Command(description = "List details of an activity")
	public void listActivities(@Param(name = "user-id") Long id,
			@Param(name = "sortBy: id|type|location|distance|date|duration") String sortBy) {
		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent() && ActivitySortFilter.exists(sortBy)) {			
			Collection<Activity> activities = paceApi.listActivities(id,sortBy);
			System.out.println(DisplayUtils.listActivities(activities));
		} else {
			if (user.isPresent()){
				System.out.println("User not found");
			} else{
				System.out.println("Filter not found");
			}			
		}
	}

	

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

	@Command(description = "Add an activity")
	public void addActivity(@Param(name = "user-id") Long id, @Param(name = "type") String type,
			@Param(name = "location") String location, @Param(name = "distance") double distance,
			@Param(name = "date") String date, @Param(name = "duration") String duration) {

		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent() && DateTimeUtils.isValidDate(date) && DateTimeUtils.isValidDuration(duration)) {
			System.out.println("ok");
			Activity activity = paceApi.createActivity(id, type, location, distance, 
					DateTimeUtils.convertStringToLocalDateTime(date), DateTimeUtils.convertStringToDuration(duration));
			System.out.println(DisplayUtils.listActivity(activity));
		} else {
			System.out.println("User not found");
		}
	}
	
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
	
	@Command(description = "Change File Format")
	public void changeFileFormat(@Param(name = "file format: xml|json|binary") String fileFormat) {
		if(FileFormat.exists(fileFormat)){
			paceApi.changeFileFormat(fileFormat);
		} else {
			System.out.println("Invalid file format");
		}
			
		
	}
	
	@Command(description = "Load data from the datastore")
	public void load() {		
			try {
				paceApi.load();
			} catch (Exception e) {
				e.printStackTrace();
			}		
	}
	
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
		if (serializer.getDataStore().isFile()) {
			paceApi.load();
		};
		
	}

	public static void main(String[] args) throws Exception {
		Main main = new Main();

		Shell shell = ShellFactory.createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions",
				main);
		shell.commandLoop();

		main.paceApi.store();
	}

}