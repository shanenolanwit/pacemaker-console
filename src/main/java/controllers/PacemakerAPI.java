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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import enums.ActivitySortFilter;
import enums.FileFormat;
import enums.UserSortFilter;
import exceptions.ValidationException;
import models.Activity;
import models.Location;
import models.User;
import utils.DateTimeUtils;
import utils.DisplayTree;
import utils.DisplayUtils;
import utils.FileLogger;
import utils.MySqlUtils;
import utils.Serializer;

public class PacemakerAPI {
	private Map<String, User> emailIndex = new HashMap<>();
	private Map<Long, User> userIndex = new HashMap<>();
	private Map<Long, Activity> activitiesIndex = new HashMap<>();
	private Serializer serializer;	

	public PacemakerAPI(Serializer serializer) {
		this.serializer = serializer;
	}

	public Collection<User> getUsers() {
		return userIndex.values();
	}

	public void deleteUsers() {
		userIndex.clear();
		activitiesIndex.clear();
		emailIndex.clear();
	}

	public User createUser(String firstName, String lastName, String email, String password) throws ValidationException {
		User user = new User(firstName, lastName, email, password);
		emailIndex.put(email, user);
		userIndex.put(user.id, user);
		return user;
	}

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

	public Activity getActivity(Long id) {
		return activitiesIndex.get(id);
	}

	public void addLocation(Long id, double latitude, double longitude) {
		Optional<Activity> activity = Optional.fromNullable(activitiesIndex.get(id));
		if (activity.isPresent()) {
			activity.get().route.add(new Location(latitude, longitude));
		}
	}

	public User getUserByEmail(String email) {
		return emailIndex.get(email);
	}

	public User getUser(Long id) {
		return userIndex.get(id);
	}
	
	public Collection<Activity> listActivities(Long id) {
		return listActivities(id,ActivitySortFilter.ID.toString());
	}

	public Collection<Activity> listActivities(Long id, String sortBy) {
		List<Activity> values = new ArrayList<>(
				userIndex.get(id).activities.values());
		Collections.sort(values,
				ActivitySortFilter.identify(sortBy).getComparator());

		return values;

	}
	
	public Collection<User> listUsers(String sortBy) {
		List<User> values = new ArrayList<>(userIndex.values());
		Collections.sort(values,
				UserSortFilter.identify(sortBy).getComparator());

		return values;

	}
	
	public void deleteUser(User user) {
		userIndex.remove(user.id);
		for(Long activityId : user.activities.keySet()){
			activitiesIndex.remove(activityId);
		}
		emailIndex.remove(user.email);
	}

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
	
	public File mysqlDump(){
		return MySqlUtils.writeToFile(getUsers());		
	}	

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

	public void changeFileFormat(String fileFormat) {
		setSerializer(FileFormat.identify(fileFormat).getSerializer());
	}

	public List<Location> listLocations(Long id) {
		return getActivity(id).route;
	}
	
	public List<Activity> listActivitiesBetweenDates(LocalDateTime start, LocalDateTime finish){
		List<Activity> result = activitiesIndex.values().stream()
				.filter(activity -> {
					return ((activity.date.isAfter(start.minusNanos(1))) && (activity.date.isBefore(finish.plusNanos(1))));
				})
				.collect(Collectors.toList());
		return result;
	}

	public boolean duplicateUserExists(String firstName, String lastName, String email) {
		return getUserIndex().values().stream().anyMatch( u -> {
			return u.firstName.equalsIgnoreCase(firstName)
					&& u.lastName.equalsIgnoreCase(lastName)
					&& u.email.equalsIgnoreCase(email);
		});
	}

}