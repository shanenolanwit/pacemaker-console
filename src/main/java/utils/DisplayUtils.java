package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

import models.Activity;
import models.Location;
import models.User;



import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestWord;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Used to format pacemaker data for optimal console display
 * @author Shane Nolan
 *
 */
public class DisplayUtils {
	
	public final static Object[] USER_TABLE_HEADER = new String[]{"ID","FIRSTNAME","LASTNAME","EMAIL","PASSWORD"};
	public final static Object[] ACTIVITY_TABLE_HEADER = new String[]{"ID","TYPE","LOCATION","DISTANCE","STARTTIME","DURATION","ROUTE"};
	public final static Object[] LOCATION_TABLE_HEADER = new String[]{"LATITUDE","LONGITUDE",""};
	
	/**
	 * Creates a table used to display user data
	 * @param users Collection of pacemaker users
	 * @return rendered user table
	 */
	public static RenderedTable listUsers(Collection<User> users){
		V2_AsciiTable at = new V2_AsciiTable();
		at.addStrongRule();
		at.addRow(USER_TABLE_HEADER);
		at.addStrongRule();
		users.stream().forEach( u -> {			
			
			at.addRow(Long.toString(u.id), WordUtils.capitalize(u.firstName), WordUtils.capitalize(u.lastName), u.email, u.password);	
			at.addRule();
		});

		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		rend.setWidth(new WidthLongestWord());
		RenderedTable rt = rend.render(at);
		return rt;
	}
	
	/**
	 * Creates a table used to display user data
	 * @param user A single pacemaker user
	 * @return rendered user table
	 */
	public static RenderedTable listUser(User user){
		Collection<User> users = new ArrayList<>(1);
		users.add(user);
		return listUsers(users);
	}
	
	/**
	 * Creates a table used to display activity data
	 * @param activities Collection of pacemaker activities
	 * @return rendered activity table
	 */
	public static RenderedTable listActivities(Collection<Activity> activities){
		V2_AsciiTable at = new V2_AsciiTable();
		at.addStrongRule();
		at.addRow(ACTIVITY_TABLE_HEADER);
		at.addStrongRule();
		activities.stream().forEach( a -> {			
			
			at.addRow(Long.toString(a.id), a.type, a.location, Double.toString(a.distance), 
					DateTimeUtils.convertLocalDateTimeToString(a.date),DateTimeUtils.convertDurationForDisplay(a.duration), a.route.toString());	
			at.addRule();
		});
		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		rend.setWidth(new WidthLongestWord());
		RenderedTable rt = rend.render(at);
		return rt;
	}
	
	/**
	 * Creates a table used to display activity data
	 * @param activity A single pacemaker activity
	 * @return rendered activity table
	 */
	public static RenderedTable listActivity(Activity activity){
		Collection<Activity> activities = new ArrayList<>(1);
		activities.add(activity);
		return listActivities(activities);
	}

	/**
	 * Creates a table used to display location data
	 * @param locations Collection of pacemaker locations
	 * @return rendered location table
	 */
	public static RenderedTable listLocations(List<Location> locations) {
		V2_AsciiTable at = new V2_AsciiTable();
		at.addStrongRule();
		at.addRow(LOCATION_TABLE_HEADER);
		at.addStrongRule();
		locations.stream().forEach( l -> {				
			at.addRow(l.latitude,l.longitude,GoogleParser.parseLatLong(l.latitude, l.longitude));	
			at.addRule();
		});

		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		rend.setWidth(new WidthLongestWord());
		RenderedTable rt = rend.render(at);
		return rt;
	}
	
	/**
	 * Builds a display tree representing pacemaker in its current state.
	 * @param users Collection of pacemaker users
	 * @return display tree representing users/activities
	 */
	public static DisplayTree getPacemakerTree(Collection<User> users){
		final DisplayTree pdt = new DisplayTree("pacemaker",new ArrayList<>());
		users.forEach(u->{
			final DisplayTree udt = new DisplayTree(u.firstName + " " + u.lastName, new ArrayList<>());
			udt.getChildren().add(new DisplayTree(u.email,new ArrayList<>()));
			final DisplayTree activityListDt = new DisplayTree("activities", new ArrayList<>());
		   	u.activities.values().forEach(a->{
		   		
		   		DisplayTree activityDt = new DisplayTree(a.type, new ArrayList<>());
		   		DisplayTree locationDt = new DisplayTree(a.location, new ArrayList<>());
		   		StringJoiner sj = new StringJoiner("],[", "[", "]");
		   		a.route.forEach(l ->{
		   			sj.add(l.latitude + "," + l.longitude);		   			
		   		});
		   		locationDt.getChildren().add(new DisplayTree(String.valueOf(a.distance), new ArrayList<>()));
		   		if(a.route.size() > 0){
		   			locationDt.getChildren().add(new DisplayTree(sj.toString(),new ArrayList<>()));
		   		}
		   		activityDt.getChildren().add(locationDt);
		   		activityListDt.getChildren().add(activityDt);
		   		
		   	});
		   	if(u.activities.size() > 0){
		   		udt.getChildren().add(activityListDt);
		   	}
		   	
		   	pdt.getChildren().add(udt);
		});
		return pdt;
	}
	

}
