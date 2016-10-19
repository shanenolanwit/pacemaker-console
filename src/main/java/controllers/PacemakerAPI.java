package controllers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import enums.ActivitySortFilter;
import enums.FileFormat;
import models.Activity;
import models.Location;
import models.User;
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
		emailIndex.clear();
	}

	public User createUser(String firstName, String lastName, String email, String password) {
		User user = new User(firstName, lastName, email, password);
		emailIndex.put(email, user);
		userIndex.put(user.id, user);
		return user;
	}

	public Activity createActivity(Long id, String type, String location, double distance, LocalDateTime date, Duration duration) {
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
		return userIndex.get(id).activities.values();
	}

	public Collection<Activity> listActivities(Long id, String sortBy) {
		List<Activity> values = new ArrayList<>(
				userIndex.get(id).activities.values());
		Collections.sort(values,
				ActivitySortFilter.identify(sortBy).getComparator());

		return values;

	}

	public void deleteUser(Long id) {
		User user = userIndex.remove(id);
		emailIndex.remove(user.email);
	}

	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		try{
			User.counter = (long) serializer.pop();
			Activity.counter = (long) serializer.pop();

			activitiesIndex = (Map<Long, Activity>) serializer.pop();
			emailIndex = (Map<String, User>) serializer.pop();
			userIndex = (Map<Long, User>) serializer.pop();
		} catch( EmptyStackException e ){
			System.out.println("Empty Stack");
		}
		
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

}