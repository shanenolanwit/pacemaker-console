package utils;

import java.util.Collection;

import models.User;

public class MySqlUtils {
	
	public static String createPacemakerScript(Collection<User> users){
		final StringBuilder scriptBuilder = new StringBuilder();
		users.stream().forEach( u -> {
			StringBuilder sbu = new StringBuilder("\nINSERT INTO USER (ID,FIRSTNAME,LASTNAME,EMAIL,PASSWORD) VALUES(");
			sbu.append(u.id)
			.append(",'").append(u.firstName)
			.append("','").append(u.lastName)
			.append("','").append(u.email)
			.append("','").append(u.password).append("');");
			scriptBuilder.append(sbu.toString());
			final String userID = String.valueOf(u.id);
			u.activities.values().forEach( a -> {
				StringBuilder sba = new StringBuilder("\nINSERT INTO ACTIVITY (ID,TYPE,LOCATION,DISTANCE,DATE,DURATION,USER_ID) VALUES(");
				sba.append(a.id)
				.append(",'").append(a.type)
				.append("','").append(a.location)
				.append("',").append(a.distance)
				.append(",'").append(DateTimeUtils.convertLocalDateTimeToString(a.date).replace("T", " "))
				.append("','").append(DateTimeUtils.convertDurationToString(a.duration))
				.append("',").append(userID).append(");");
				scriptBuilder.append(sba.toString());
				final long activityID = a.id;
				a.route.forEach(l -> {					
					scriptBuilder.append("\nINSERT INTO LOCATION (ID,LATITUDE,LONGITUDE,ACTIVITY_ID) VALUES(" 
							+ l.id + "," + l.latitude + "," + l.longitude + "," + activityID + ");");
				});
				
			});
			
		});
		
		return scriptBuilder.toString();
	}
	
	

}
