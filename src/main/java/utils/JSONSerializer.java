package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;

import models.Activity;
import models.Location;
import models.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * JSONSerializer 
 * @author Shane Nolan
 * Used to serialise pacemaker information to a json datastore
 */
public class JSONSerializer implements Serializer {

	private Stack stack = new Stack();
	private File dataStore = new File("datastore.json");

	public JSONSerializer() {
		super();
	}
	
	public JSONSerializer(File dataStore) {
		this.dataStore = dataStore;
	}

	public void push(Object o) {
		stack.push(o);
	}

	public Object pop() {
		return stack.pop();
	}

	/**
	 * Converts jsondata to pacemaker data
	 */
	public void read() throws Exception {
		ObjectInputStream is = null;
		JsonReader reader = new JsonReader(new FileReader(dataStore));
		try {
			Gson gson = new Gson();			
			Stack tmpStack = (Stack) gson.fromJson(reader, Stack.class);
			Long userCounter = ((Number)tmpStack.pop()).longValue();
			Long activityCounter = ((Number)tmpStack.pop()).longValue();
			final Map<Long, Activity> ai = 
					((Map<Long, Activity>) tmpStack.pop()).entrySet()
				                    .stream()
				                    .collect (Collectors.toMap(
				                    		e-> sanitiseId(e.getKey()),
				                            e-> sanitiseActivity(e.getValue())
				                             ));
			
			Map<String, User> ei = 
					((Map<String, User>) tmpStack.pop()).entrySet()
				                    .stream()
				                    .collect (Collectors.toMap(
				                    		e-> String.valueOf(e.getKey()),
				                            e-> sanitiseUser(e.getValue(), ai)
				                             ));
			
			Map<Long, User> ui = 
					((Map<Long, User>) tmpStack.pop()).entrySet()
				                    .stream()
				                    .collect (Collectors.toMap(
				                    		e-> sanitiseId(e.getKey()),
				                            e-> sanitiseUser(e.getValue(), ai)
				                             ));
			stack.push(ui);
			stack.push(ei);
			stack.push(ai);
			stack.push(activityCounter);
			stack.push(userCounter);
			
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (is != null) {
				is.close();
			}

		}
	}

	//xstream bug - can not serialise localdatetime
	//https://github.com/x-stream/xstream/issues/24
	public void write() throws Exception {
		ObjectOutputStream os = null;
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataStore));
		try {
			String json = new Gson().toJson(stack);
			writer.write(json);
		} finally {
			if (os != null) {
				os.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	@Override
	public File getDataStore() {
		return this.dataStore;
	}
	
	//Used for converting json strings representing longs to actual longs
	private Long sanitiseId(Object key) {
		Long l = null;
		if(key instanceof Number){
			l = ((Number)key).longValue();
		} else if(key instanceof String){
			l = Long.valueOf((String)key);
		}
		return l;
	}
	
	//Used for converting json objects representing activities to actual activities
	private Activity sanitiseActivity(Object key) {
		Activity a = null;
		if(key instanceof Activity){
			a = (Activity) key;
		} else if(key instanceof LinkedTreeMap){
			JsonObject jo = new Gson().toJsonTree(key).getAsJsonObject();
			a = new Activity();
			a.id = jo.get("id").getAsLong();
			a.type = jo.get("type").getAsString();
			a.location = jo.get("location").getAsString();
			a.distance = jo.get("distance").getAsDouble();
			JsonObject dateTime = jo.get("date").getAsJsonObject();
			JsonObject duration = jo.get("duration").getAsJsonObject();
			a.date = DateTimeUtils.convertJsonDateTime(dateTime);
			a.duration = DateTimeUtils.convertJsonDuration(duration);
			List<Location> route = new ArrayList<>();
			jo.get("route").getAsJsonArray().forEach((e) ->{
				Location l = new Location();
				l.id = (((JsonObject) e).get("id")).getAsLong();
				l.latitude = (((JsonObject) e).get("latitude")).getAsDouble();
				l.longitude = (((JsonObject) e).get("longitude")).getAsDouble();
				route.add(l);
			});
			a.route = route;
		}
		return a;
	}
	
	//Used to convert json data representing users to actual users
	private User sanitiseUser(Object key, Map<Long, Activity> ai) {
		User u = null;
		if(key instanceof User){
			u = (User) key;
		} else if(key instanceof LinkedTreeMap){
			JsonObject jo = new Gson().toJsonTree(key).getAsJsonObject();
			u = new User();
			u.id = jo.get("id").getAsLong();
			u.firstName = jo.get("firstName").getAsString();
			u.lastName = jo.get("lastName").getAsString();
			u.email = jo.get("email").getAsString();
			u.password = jo.get("password").getAsString();
			Map<Long,Activity> activityMap = new HashMap<>();
			jo.get("activities").getAsJsonObject().entrySet().forEach((s) ->{
				activityMap.put(Long.valueOf(s.getKey()), ai.get(Long.valueOf(s.getKey())));
			});
			u.activities = activityMap;
		}
		return u;
	}
}
