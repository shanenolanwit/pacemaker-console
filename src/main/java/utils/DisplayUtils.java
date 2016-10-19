package utils;

import java.util.ArrayList;
import java.util.Collection;

import models.Activity;
import models.User;



import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestWord;

public class DisplayUtils {
	
	public final static Object[] USER_TABLE_HEADER = new String[]{"ID","FIRSTNAME","LASTNAME","EMAIL","PASSWORD"};
	public final static Object[] ACTIVITY_TABLE_HEADER = new String[]{"ID","TYPE","LOCATION","DISTANCE","STARTTIME","DURATION","ROUTE"};
	
	public static RenderedTable listUsers(Collection<User> users){
		V2_AsciiTable at = new V2_AsciiTable();
		at.addStrongRule();
		at.addRow(USER_TABLE_HEADER);
		at.addStrongRule();
		users.stream().forEach( u -> {			
			
			at.addRow(Long.toString(u.id), u.firstName, u.lastName, u.email, u.password);	
			at.addRule();
		});

		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		rend.setWidth(new WidthLongestWord());
		RenderedTable rt = rend.render(at);
		return rt;
	}
	
	public static RenderedTable listUser(User u){
		Collection<User> users = new ArrayList<>(1);
		users.add(u);
		return listUsers(users);
	}
	
	public static RenderedTable listActivities(Collection<Activity> activities){
		V2_AsciiTable at = new V2_AsciiTable();
		at.addStrongRule();
		at.addRow(ACTIVITY_TABLE_HEADER);
		at.addStrongRule();
		activities.stream().forEach( a -> {			
			
			at.addRow(Long.toString(a.id), a.type, a.location, Double.toString(a.distance), 
					DateTimeUtils.convertLocalDateTimeToString(a.date),(a.duration), a.route.toString());	
			at.addRule();
		});
		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		rend.setWidth(new WidthLongestWord());
		RenderedTable rt = rend.render(at);
		return rt;
	}
	
	public static RenderedTable listActivity(Activity a){
		Collection<Activity> activities = new ArrayList<>(1);
		activities.add(a);
		return listActivities(activities);
	}
	

}
