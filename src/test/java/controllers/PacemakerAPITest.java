package controllers;

import static org.junit.Assert.*;

import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import controllers.PacemakerAPI;
import exceptions.ValidationException;
import models.Activity;
import models.Location;
import models.User;
import utils.BinarySerializer;
import utils.DateTimeUtils;
import utils.JSONSerializer;
import utils.XMLSerializer;

import static models.Fixtures.users;
import static models.Fixtures.activities;
import static models.Fixtures.locations;

public class PacemakerAPITest
{
	private PacemakerAPI pacemaker;

	@Before
	public void setup() throws ValidationException
	{
		pacemaker = new PacemakerAPI(null);
		for (User user : users)
		{
			pacemaker.createUser(user.firstName, user.lastName, user.email, user.password);
		}
	}

	@After
	public void tearDown()
	{
		pacemaker = null;
	}

	@Test
	public void testUser() throws ValidationException
	{
		assertEquals (users.length, pacemaker.getUsers().size());
		pacemaker.createUser("homer", "simpson", "homer@simpson.com", "secret");
		assertEquals (users.length+1, pacemaker.getUsers().size());
		assertEquals (users[0], pacemaker.getUserByEmail(users[0].email));
	}  

	@Test
	public void testEquals() throws ValidationException
	{
		User homer  = new User ("homer", "simpson", "homer@simpson.com",  "secret");
		User homer2 = new User ("homer", "simpson", "homer@simpson.com",  "secret"); 
		User bart   = new User ("bart", "simpson", "bartr@simpson.com",  "secret"); 

		assertEquals(homer, homer);
		assertEquals(homer, homer2);
		assertNotEquals(homer, bart);

		assertSame(homer, homer);
		assertNotSame(homer, homer2);
	}

	@Test
	public void testUsers()
	{
		assertEquals (users.length, pacemaker.getUsers().size());
		for (User user: users)
		{
			User eachUser = pacemaker.getUserByEmail(user.email);
			assertEquals (user, eachUser);
			assertNotSame(user, eachUser);
		}
	}
	
	@Test
	public void testGetUser(){
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		long id = marge.id;
		assertEquals(marge, pacemaker.getUser(id));
		assertNull(pacemaker.getUser((long) -3));
		
	}
	
	@Test
	public void testChangeFileFormat(){
		pacemaker.changeFileFormat("JSON");
		assertEquals(JSONSerializer.class, pacemaker.getSerializer().getClass());
		pacemaker.changeFileFormat("xml");
		assertEquals(XMLSerializer.class, pacemaker.getSerializer().getClass());
		pacemaker.changeFileFormat("Binary");
		assertEquals(BinarySerializer.class, pacemaker.getSerializer().getClass());
	}

	@Test
	public void testDeleteUsers()
	{
		assertEquals (users.length, pacemaker.getUsers().size());
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		pacemaker.deleteUser(marge);
		assertEquals (users.length-1, pacemaker.getUsers().size()); 
		assertTrue(users.length > 0);
		assertTrue(pacemaker.getUserIndex().size() > 0);
		
		pacemaker.deleteUsers();
		assertEquals(0,pacemaker.getUsers().size());
		assertEquals(0,pacemaker.getUserIndex().size());
		assertEquals(0,pacemaker.getEmailIndex().size());
	}

	@Test
	public void testAddActivity() throws ValidationException
	{
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		Activity activity = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location, 
				activities[0].distance, activities[0].date, activities[0].duration);
		Activity returnedActivity = pacemaker.getActivity(activity.id);
		assertEquals(activities[0],  returnedActivity);
		assertNotSame(activities[0], returnedActivity);
	}  
	
	@Test
	public void testListActivities() throws ValidationException
	{
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		Activity activity = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location, 
				activities[0].distance, activities[0].date, activities[0].duration);
		
		Activity activity2 = pacemaker.createActivity(marge.id, activities[2].type, activities[2].location, 
				activities[2].distance, activities[2].date, activities[2].duration);
		
		Activity activity4 = pacemaker.createActivity(marge.id, activities[4].type, activities[4].location, 
				activities[4].distance, activities[4].date, activities[4].duration);
		
		Collection<Activity> acts = pacemaker.listActivities(marge.id);
		assertEquals(3, acts.size());
		assertEquals(activity, acts.toArray()[0]);
		assertEquals(activity2, acts.toArray()[1]);
		assertEquals(activity4, acts.toArray()[2]);
		
		acts = pacemaker.listActivities(marge.id, "TYPE");
		assertEquals(3, acts.size());
		assertEquals(activity4, acts.toArray()[0]);
		assertEquals(activity2, acts.toArray()[1]);
		assertEquals(activity, acts.toArray()[2]);
		
		acts = pacemaker.listActivities(marge.id, "DURATION");
		assertEquals(3, acts.size());
		assertEquals(activity, acts.toArray()[0]);
		assertEquals(activity2, acts.toArray()[1]);
		assertEquals(activity4, acts.toArray()[2]);
		
		acts = pacemaker.listActivities(marge.id, "LOCATION");
		assertEquals(3, acts.size());
		assertEquals(activity, acts.toArray()[0]);
		assertEquals(activity4, acts.toArray()[1]);
		assertEquals(activity2, acts.toArray()[2]);
		
		acts = pacemaker.listActivities(marge.id, "DATE");
		assertEquals(3, acts.size());
		assertEquals(activity, acts.toArray()[0]);
		assertEquals(activity2, acts.toArray()[1]);
		assertEquals(activity4, acts.toArray()[2]);
		
		acts = pacemaker.listActivities(marge.id, "DISTANCE");
		assertEquals(3, acts.size());
		assertEquals(activity, acts.toArray()[0]);
		assertEquals(activity2, acts.toArray()[1]);
		assertEquals(activity4, acts.toArray()[2]);
		
		acts = pacemaker.listActivitiesBetweenDates(activity.date, activity4.date);
		assertEquals(3, acts.size());
		
		acts = pacemaker.listActivitiesBetweenDates(activity.date, activity2.date);
		assertEquals(2, acts.size());
		
		acts = pacemaker.listActivitiesBetweenDates(activity.date, activity.date);
		assertEquals(1, acts.size());
		
		acts = pacemaker.listActivitiesBetweenDates(
				DateTimeUtils.convertStringToLocalDateTime("12:10:2012 8:00:00"), 
				DateTimeUtils.convertStringToLocalDateTime("12:10:2013 8:45:00"));
		assertEquals(0, acts.size());
		
		acts = pacemaker.listActivitiesBetweenDates(
				DateTimeUtils.convertStringToLocalDateTime("12:10:2012 8:00:00"), 
				DateTimeUtils.convertStringToLocalDateTime("12:10:2013 9:45:00"));
		assertEquals(1, acts.size());
		
		acts = pacemaker.listActivitiesBetweenDates(
				DateTimeUtils.convertStringToLocalDateTime("12:10:2016 9:10:00"), 
				DateTimeUtils.convertStringToLocalDateTime("12:10:2017 9:00:00"));
		assertEquals(0, acts.size());
		
		acts = pacemaker.listActivitiesBetweenDates(
				DateTimeUtils.convertStringToLocalDateTime("12:10:2016 8:10:00"), 
				DateTimeUtils.convertStringToLocalDateTime("12:10:2017 9:00:00"));
		assertEquals(1, acts.size());
		
		//Dates in wrong order
		acts = pacemaker.listActivitiesBetweenDates(
				DateTimeUtils.convertStringToLocalDateTime("12:10:2017 9:00:00"),
				DateTimeUtils.convertStringToLocalDateTime("12:10:2016 8:10:00"));
		assertEquals(0, acts.size());
	}  
	
	@Test
	public void testListUsers() throws ValidationException{		
		pacemaker = new PacemakerAPI(null);
		pacemaker.createUser("ann","yoman","redroom@gmail.com","secret");
		pacemaker.createUser("zara","brett","seven@gmail.com","secret");
		pacemaker.createUser("triona","doyle","blah@gmail.com","secret");
		pacemaker.createUser("nicola","goral","ng@gmail.com","secret");
		
		User user0 = pacemaker.getUserByEmail("redroom@gmail.com");
		User user1 = pacemaker.getUserByEmail("seven@gmail.com");
		User user2 = pacemaker.getUserByEmail("blah@gmail.com");
		User user3 = pacemaker.getUserByEmail("ng@gmail.com");

		Collection<User> users = pacemaker.listUsers("ID");
		assertEquals(4, users.size());
		
		assertEquals(user0, users.toArray()[0]);		
		assertEquals(user1, users.toArray()[1]);		
		assertEquals(user2, users.toArray()[2]);		
		assertEquals(user3, users.toArray()[3]);
		
		users = pacemaker.listUsers("FIRSTNAME");
		assertEquals(4, users.size());
		assertEquals(user0, users.toArray()[0]);		
		assertEquals(user3, users.toArray()[1]);		
		assertEquals(user2, users.toArray()[2]);		
		assertEquals(user1, users.toArray()[3]);
		
		users = pacemaker.listUsers("LASTNAME");
		assertEquals(4, users.size());
		assertEquals(user1, users.toArray()[0]);		
		assertEquals(user2, users.toArray()[1]);		
		assertEquals(user3, users.toArray()[2]);		
		assertEquals(user0, users.toArray()[3]);
		
		users = pacemaker.listUsers("EMAIL");
		assertEquals(4, users.size());
		assertEquals(user2, users.toArray()[0]);		
		assertEquals(user3, users.toArray()[1]);		
		assertEquals(user0, users.toArray()[2]);		
		assertEquals(user1, users.toArray()[3]);
	}  


	@Test
	public void testAddActivityWithSingleLocation() throws ValidationException
	{
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		Long activityId = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location, 
				activities[0].distance, activities[0].date, activities[0].duration).id;

		pacemaker.addLocation(activityId, locations[0].latitude, locations[0].longitude);

		Activity activity = pacemaker.getActivity(activityId);
		assertEquals (1, activity.route.size());
		assertEquals(0.0001, locations[0].latitude,  activity.route.get(0).latitude);
		assertEquals(0.0001, locations[0].longitude, activity.route.get(0).longitude);   
	}	
	
	@Test
	  public void testAddActivityWithMultipleLocation() throws ValidationException
	  {
	    User marge = pacemaker.getUserByEmail("marge@simpson.com");
	    Long activityId = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location, 
	    		activities[0].distance, activities[0].date, activities[0].duration).id;

	    for (Location location : locations)
	    {
	      pacemaker.addLocation(activityId, location.latitude, location.longitude);      
	    }
	    
	    Activity activity = pacemaker.getActivity(activityId);
	    assertEquals (locations.length, activity.route.size());
	    int i = 0;
	    for (Location location : activity.route)
	    {
	      assertEquals(location, locations[i]);
	      i++;
	    }
	    
	    
	  }

}