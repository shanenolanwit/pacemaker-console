package utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import models.Activity;
import models.Location;
import models.User;

/**
 * YAMLSerializer 
 * @author Shane Nolan
 * Used to serialise pacemaker information to a yaml datastore
 */
public class YAMLSerializer implements Serializer {
	
	private Stack stack = new Stack();
	private File dataStore = new File("datastore.yml");
	
	public YAMLSerializer() {
		super();
	}
	
	public YAMLSerializer(File datastore) {
		this.dataStore = datastore;
	}

	public void push(Object o) {
		stack.push(o);
	}

	public Object pop() {
		return stack.pop();
	}

	
	/**
	 * Sanitises and normalises pacemaker data. Converts yaml incompatible types to string equivalents.
	 */
	@Override
	public void write() throws Exception {
		Stack tmpStack = new Stack();
		tmpStack.push(stack.pop()); //users counter
		tmpStack.push(stack.pop()); //activities counter
		
		//process activities
		final Map<String,Object> activities = new HashMap<>();
		((Map<Long,Activity>)stack.pop()).forEach((k,v) ->{
			Map<String,Object> activity = new HashMap<>();
			activity.put("id", String.valueOf(v.id));
			activity.put("type", v.type);
			activity.put("location", v.location);
			activity.put("distance", v.distance);
			//2 digits left padded with zeros if required
			activity.put("date", 
					String.format("%02d", v.date.getDayOfMonth()) + ":" + 
					String.format("%02d", v.date.getMonthValue()) + ":" + 
					String.format("%04d", v.date.getYear()) + " " + 
					String.format("%02d", v.date.getHour()) + ":" + 
					String.format("%02d", v.date.getMinute()) + ":" + 
					String.format("%02d", v.date.getSecond()));
			activity.put("duration", DateTimeUtils.convertDurationToString(v.duration));
			activity.put("route", v.route);
			activities.put(String.valueOf(k), activity);
		});
		tmpStack.push(activities);
		
		//process emails
		final Map<String,Object> emails = new HashMap<>();
		((Map<String,User>)stack.pop()).forEach((k,v) ->{
			Map<String,Object> user = new HashMap<>();
			user.put("id", String.valueOf(v.id));
			user.put("firstname", v.firstName);
			user.put("lastname", v.lastName);
			user.put("email", v.email);
			user.put("password", v.password);
			user.put("activities", v.activities.keySet());
			emails.put(String.valueOf(k), user);
		});
		tmpStack.push(emails);
		
		// process users
		final Map<String, Object> users = new HashMap<>();
		((Map<Long, User>) stack.pop()).forEach((k, v) -> {
			Map<String, Object> user = new HashMap<>();
			user.put("id", String.valueOf(v.id));
			user.put("firstname", v.firstName);
			user.put("lastname", v.lastName);
			user.put("email", v.email);
			user.put("password", v.password);
			user.put("activities", v.activities.keySet());
			users.put(String.valueOf(k), user);
		});
		tmpStack.push(users);
		
		stack.push(tmpStack.pop());
		stack.push(tmpStack.pop());
		stack.push(tmpStack.pop());
		stack.push(tmpStack.pop());
		stack.push(tmpStack.pop());
		
		YamlWriter writer = null;
		
		try {
			writer = new YamlWriter(new FileWriter(getDataStore()));
			writer.write(stack);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

	}

	/**
	 * Converts normalised yaml data to pacemaker data
	 */
	@Override
	public void read() throws Exception {
		YamlReader reader = null;
		try {
			reader = new YamlReader(new FileReader(getDataStore()));
			Stack tmpStack = (Stack) reader.read();
			Long userCounter = Long.valueOf((String)tmpStack.pop());
			Long activityCounter = Long.valueOf((String)tmpStack.pop());
			
			//activities
			Map<Long,Activity> activities = new HashMap<>();
			Map<String,Object> activitiesMap = (Map<String,Object>) tmpStack.pop();
			activitiesMap.forEach((k,v) -> {
				Activity a = new Activity();
				a.id = Long.valueOf((String)((Map<String,Object>)v).get("id"));
				a.type = (String)((Map<String,Object>)v).get("type");
				a.location = (String)((Map<String,Object>)v).get("location");
				a.distance = Double.valueOf((String)((Map<String,Object>)v).get("distance"));
				a.date = DateTimeUtils.convertStringToLocalDateTime((String)((Map<String,Object>)v).get("date"));
				a.duration = DateTimeUtils.convertStringToDuration((String)((Map<String,Object>)v).get("duration"));
				List<Location> locs = new ArrayList<>();
				((List<Location>)((Map<String,Object>)v).get("route")).forEach(l -> {
					Location lo = new Location();
					lo.id = Long.valueOf(l.id);
					lo.latitude = l.latitude;
					lo.longitude = l.longitude;
					locs.add(lo);
				});
				a.route = locs;
				activities.put(a.id, a);
			});
			
			//emails and users
			Map<String,User> emails = new HashMap<>();
			Map<Long,User> users = new HashMap<>();
			Map<String,Object> emailsMap = (Map<String,Object>) tmpStack.pop();
			emailsMap.forEach((k,v) -> {
				User user = new User();
				user.id = Long.valueOf((String)((Map<String,Object>)v).get("id"));
				user.firstName = (String)((Map<String,Object>)v).get("firstname");
				user.lastName = (String)((Map<String,Object>)v).get("lastname");
				user.email = (String)((Map<String,Object>)v).get("email");
				user.password = (String)((Map<String,Object>)v).get("password");
				Set hs = (Set) ((Map<String,Object>)v).get("activities");
				hs.forEach(a -> {
					Long l = Long.valueOf((String) a);
					user.activities.put(l, activities.get(l));
				});
				emails.put(k, user);
				users.put(user.id, user);
			});
			stack.push(users);
			stack.push(emails);
			stack.push(activities);

			stack.push(activityCounter);
			stack.push(userCounter);
			
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		
	}

	@Override
	public File getDataStore() {
		return this.dataStore;
	}

}
