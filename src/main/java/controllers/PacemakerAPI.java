package controllers;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Optional;
import enums.ActivitySortFilter;
import enums.FileFormat;
import enums.UserSortFilter;
import exceptions.ValidationException;
import models.Activity;
import models.Location;
import models.User;
import utils.DisplayTree;
import utils.DisplayUtils;
import utils.FileLogger;
import utils.MySqlUtils;
import utils.Serializer;

/**
 * The Pacemaker API acts as a controller linking the models to the views
 * @author Shane Nolan
 */
public class PacemakerAPI {
	
	private Map<String, User> emailIndex = new HashMap<>();
	private Map<Long, User> userIndex = new HashMap<>();
	private Map<Long, Activity> activitiesIndex = new HashMap<>();
	private Serializer serializer;	

	/**
	 * Initialise the pacemaker api
	 * @param serializer  Serializer to use with the {@link controllers.PacemakerAPI#load()} and 
	 * {@link controllers.PacemakerAPI#store()} methods
	 */
	public PacemakerAPI(Serializer serializer) {
		this.serializer = serializer;
	}
	
	/**
	 * Gets all the users
	 * @return Collection of {@link models.User} objects
	 */
	public Collection<User> getUsers() {
		return userIndex.values();
	}

	/**
	 * Returns the user associated with a specified email address
	 * @param email  String representing a users email address
	 * @return  the user with the specified email address
	 * @see models.User
	 */
	public User getUserByEmail(String email) {
		return emailIndex.get(email);
	}

	/**
	 * Returns the user associated with a specified id
	 * @param id  Long representing a users id
	 * @return  the user with the specified id
	 * @see models.User
	 */
	public User getUser(Long id) {
		return userIndex.get(id);
	}
	
	/**
	 * Converts a collection of users into a tree view
	 * @param users  Collection of users to display in the tree view
	 * @return a {@link utils.DisplayTree}
	 */
	public DisplayTree getPacemakerTree(Collection<User> users) {
		return DisplayUtils.getPacemakerTree(users);
	}
	
	/**
	 * Creates and indexes a new {@link models.User}
	 * @param firstName  String representing a users given name
	 * @param lastName  String representing a users surname
	 * @param email  String representing a users email address
	 * @param password  String representing a users password
	 * @return the newly created user
	 * @throws ValidationException exception describing issues with user creation
	 */
	public User createUser(String firstName, String lastName, String email, String password) throws ValidationException {
		User user = new User(firstName, lastName, email, password);
		emailIndex.put(email, user);
		userIndex.put(user.id, user);
		return user;
	}
	
	/**
	 * Deletes and unindexes a given {@link models.User}
	 * @param user the user you wish to delete
	 */
	public void deleteUser(User user) {
		userIndex.remove(user.id);
		for(Long activityId : user.activities.keySet()){
			activitiesIndex.remove(activityId);
		}
		emailIndex.remove(user.email);
	}
	
	/**
	 * Checks if a given email address his already associated with a {@link models.User}
	 * @param email  String representing a users email address
	 * @return  boolean representing if this email is already in use
	 * @see models.User#email
	 */
	public boolean duplicateUserExists(String email) {
		return getUserIndex().values().stream().anyMatch( u -> {
			return u.email.equalsIgnoreCase(email);
		});
	}
	
	/**
	 * Deletes indexes and clears model counters
	 */
	public void deleteUsers() {
		userIndex.clear();
		activitiesIndex.clear();
		emailIndex.clear();
		User.counter = new Long(0);
		Activity.counter = new Long(0);
		Location.counter = new Long(0);
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
	 * @return a collection of users
	 */
	public Collection<User> listUsers(String sortBy) {
		List<User> values = new ArrayList<>(userIndex.values());
		Collections.sort(values,
				UserSortFilter.identify(sortBy).getComparator());
		return values;
	}

	/**
	 * Adds an {@link models.Activity} to the specified {@link models.User} activity list
	 * @param id  The user id associated with this activity
	 * @param type  String representing the type of activity
	 * @param location  String representing the location of the activity
	 * @param distance  Double representing the distance in terms of kilometers
	 * @param date  String representing the activities starting datetime ( dd:MM:yyyy H:mm:ss )
	 * @param duration  String representing the duration of the activity ( HH:mm:ss )
	 * @return the newly created activity
	 * @throws ValidationException exception describing issues with activity creation
	 */
	public Activity createActivity(Long id, String type, String location, 
			double distance, LocalDateTime date, Duration duration) throws ValidationException {
		Activity activity = null;
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		if (user.isPresent()) {
			activity = new Activity(type, location, distance, date, duration);
			user.get().activities.put(activity.id, activity);
			activitiesIndex.put(activity.id, activity);
		}
		return activity;
	}

	/**
	 * Returns an {@link models.Activity} associated with the given id
	 * @param id  Long representing an activity
	 * @return  the activity with the specified id
	 */
	public Activity getActivity(Long id) {
		return activitiesIndex.get(id);
	}

	/**
	 * Lists the activities associated with a specified {@link models.User}
	 * @param id  Id of the user whose activities you are interested in
	 * @return  Collection of activities
	 */
	public Collection<Activity> listActivities(Long id) {
		return listActivities(id,ActivitySortFilter.ID.toString());
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
	 * @return ordered collection of activities
	 */
	public Collection<Activity> listActivities(Long id, String sortBy) {
		List<Activity> values = new ArrayList<>(
				userIndex.get(id).activities.values());
		Collections.sort(values,
				ActivitySortFilter.identify(sortBy).getComparator());

		return values;
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
	 * @return a list of activities that match the given criteria
	 */
	public List<Activity> listActivitiesBetweenDates(LocalDateTime start, LocalDateTime finish){
		List<Activity> result = activitiesIndex.values().stream()
				.filter(activity -> {
					return ((activity.date.isAfter(start.minusNanos(1))) && (activity.date.isBefore(finish.plusNanos(1))));
				})
				.collect(Collectors.toList());
		return result;
	}
	
	/**
	 * Adds a location to an activities route
	 * @param id  the actvity you wish to update
	 * @param latitude  Double representing latitude value
	 * @param longitude  Double representing longitude value
	 */
	public void addLocation(Long id, double latitude, double longitude) {
		Optional<Activity> activity = Optional.fromNullable(activitiesIndex.get(id));
		if (activity.isPresent()) {
			activity.get().route.add(new Location(latitude, longitude));
		}
	}

	/**
	 * Returns a list of locations associated with a given activity
	 * @param id  a Long representing an {@link models.Activity} id
	 * @return a list of locations
	 * @see models.Location
	 */
	public List<Location> listLocations(Long id) {
		return getActivity(id).route;
	}
	
	/**
	 * Prints a mysql representation of the user datastore to an sql file
	 * @return  mysql file
	 * @see utils.MySqlUtils
	 */
	public File mysqlDump(){
		return MySqlUtils.writeToFile(getUsers());		
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
	 * @see controllers.PacemakerAPI#store()
	 * @see controllers.PacemakerAPI#load()
	 */
	public void changeFileFormat(String fileFormat) {
		setSerializer(FileFormat.identify(fileFormat).getSerializer());
	}
		
	/**
	 * Loads data from the datastore associated with the current file format
	 * Resets model counters and pacemaker indexes
	 * @throws Exception e
	 */
	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		serializer.read();
		try{
			User.counter = ((Number)serializer.pop()).longValue();
			Activity.counter = ((Number)serializer.pop()).longValue();			
			setActivitiesIndex( (Map<Long, Activity>) serializer.pop() );			
			setEmailIndex( (Map<String, User>) serializer.pop() );			
			setUserIndex( (Map<Long, User>) serializer.pop() );
		} catch( EmptyStackException e ){
			FileLogger.getLogger().log("Empty Stack");
		}
		
	}	

	/**
	 * Writes model data and pacemaker indexes to the datastore associated with the current file format
	 * @throws Exception e
	 */
	public void store() throws Exception {
		serializer.push(userIndex);
		serializer.push(emailIndex);
		serializer.push(activitiesIndex);

		serializer.push(Activity.counter);
		serializer.push(User.counter);
		serializer.write();
	}
	
	
	public Map<String, User> getEmailIndex() {
		return emailIndex;
	}

	public void setEmailIndex(Map<String, User> emailIndex) {
		this.emailIndex = emailIndex;
	}

	public Map<Long, User> getUserIndex() {
		return userIndex;
	}

	public void setUserIndex(Map<Long, User> userIndex) {
		this.userIndex = userIndex;
	}

	public Map<Long, Activity> getActivitiesIndex() {
		return activitiesIndex;
	}

	public void setActivitiesIndex(Map<Long, Activity> activitiesIndex) {
		this.activitiesIndex = activitiesIndex;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	


}