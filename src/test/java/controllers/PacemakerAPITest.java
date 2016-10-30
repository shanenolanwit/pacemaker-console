package controllers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import controllers.PacemakerAPI;
import exceptions.ValidationException;
import models.Activity;
import models.Location;
import models.User;
import utils.BinarySerializer;
import utils.DateTimeUtils;
import utils.JSONSerializer;
import utils.XMLSerializer;
import utils.YAMLSerializer;

import static models.Fixtures.users;
import static models.Fixtures.activities;
import static models.Fixtures.locations;

public class PacemakerAPITest {
	private PacemakerAPI pacemaker;

	@Before
	public void setup() throws ValidationException {
		pacemaker = new PacemakerAPI(null);
		for (User user : users) {
			pacemaker.createUser(user.firstName, user.lastName, user.email, user.password);
		}
	}

	@After
	public void tearDown() {
		pacemaker.deleteUsers();
		pacemaker = null;
	}

	/**
	 * Given the user count is a known size
	 * When I call create user with valid parameters 
	 * Then a user is created
	 * And the user count increases by 1
	 */
	@Test
	public void standardUserCreation() throws ValidationException {
		assertThat(pacemaker.getUsers().size(), is(equalTo(users.length)) );
		pacemaker.createUser("homer", "simpson", "homer@simpson.com", "secret");
		assertThat(pacemaker.getUsers().size(), is(equalTo(users.length+1)) );
		assertThat(pacemaker.getUserByEmail(users[0].email), is(notNullValue()));
	}
	
	/**
	 * Given the user count is a known size
	 * When I call create user with invalid parameters 
	 * Then a user is not created
	 * And the user count stays the same
	 */
	@Test(expected = ValidationException.class)
	public void invalidUserCreation() throws ValidationException {
		assertThat(pacemaker.getUsers().size(), is(equalTo(users.length)) );
		pacemaker.createUser("123", "456", "123456@simpson.com", "secret");
		fail("Exception should have been thrown by previous command");
	}
	
	/**
	 * Given I have a pacemaker instance with pre-populated users
	 * When I search for a user by their email address
	 * That user is returned
	 */
	@Test
	public void validEmailSearchesReturnValidUsers() {
		assertThat(pacemaker.getUsers().size(), is(users.length));
		assertThat(pacemaker.getUserByEmail("marge@simpson.com"), is(notNullValue()));
		assertThat(pacemaker.getUserByEmail("marge@simpson.com").firstName, is("marge"));
		for (User user : users) {
			User foundUser = pacemaker.getUserByEmail(user.email);
			assertThat(foundUser, allOf( 
					is(notNullValue()),
					is(equalTo(user)),
					is(not(sameInstance(user))) //ids not the same since static users not created by pacemaker
			));
		}
	}

	/**
	 * Given I have a pre-populated pacemaker instance
	 * When I perform email searches with invalid email addresses
	 * Then no user is returned
	 */
	@Test
	public void invalidEmailSearchesReturnNoUsers() {		
		assertThat(pacemaker.getUserByEmail("marge@simpson.com"), is(notNullValue()));
		assertThat(pacemaker.getUserByEmail("orange@fanta.com"), is(nullValue()));
		assertThat(pacemaker.getUserByEmail(""), is(nullValue()));
		assertThat(pacemaker.getUserByEmail(null), is(nullValue()));
	}
	
	/**
	 * Given I have a pacemaker instance with pre-populated users
	 * When I search for a user by their id
	 * That user is returned
	 * Note: Counter is incremented when users are created in fixtures and then again when entered into the app
	 */
	@Test
	public void validIdSearchesReturnValidUsers() {
		assertThat(pacemaker.getUsers().size(), is(users.length));
		assertThat(pacemaker.getUser(pacemaker.getUserByEmail("marge@simpson.com").id), is(notNullValue()));
		assertThat(pacemaker.getUser(pacemaker.getUserByEmail("marge@simpson.com").id).firstName, is("marge"));
		for (User user : users) {
			User foundUser = pacemaker.getUser(pacemaker.getUserByEmail(user.email).id);
			assertThat(foundUser, allOf( 
					is(notNullValue()),
					is(equalTo(user))
			));
		}
	}

	/**
	 * Given I have a pre-populated pacemaker instance
	 * When I perform email searches with invalid ids
	 * Then no user is returned
	 */
	@Test
	public void invalidIdSearchesReturnNoUsers() {
		assertThat(pacemaker.getUser(pacemaker.getUserByEmail("marge@simpson.com").id), is(notNullValue()));
		assertThat(pacemaker.getUser(Long.MIN_VALUE), is(nullValue()));
		assertThat(pacemaker.getUser(Long.MAX_VALUE), is(nullValue()));
		assertThat(pacemaker.getUser(null), is(nullValue()));
	}
	
    /**
     * When I change the file format to a valid type
     * Then the serializer type is updated accordingly
     */
	@Test
	public void validChangeFileFormatUpdatesSerializer() {
		pacemaker.changeFileFormat("JSON");
		assertEquals(JSONSerializer.class, pacemaker.getSerializer().getClass());
		pacemaker.changeFileFormat("xml");
		assertEquals(XMLSerializer.class, pacemaker.getSerializer().getClass());
		pacemaker.changeFileFormat("Binary");
		assertEquals(BinarySerializer.class, pacemaker.getSerializer().getClass());
		pacemaker.changeFileFormat("YamL");
		assertEquals(YAMLSerializer.class, pacemaker.getSerializer().getClass());
	}
	
    /**
     * When I change the file format to an invalid type
     * Then the serializer type is not updated
     */
	@Test(expected = IllegalArgumentException.class)
	public void invalidChangeFileFormatUpdatesSerializer() {
		pacemaker.changeFileFormat("shane");
		fail("Previous argument should have caused an exception");
	}

	/**
	 * Given a user exists in pacemaker
	 * When I delete that user
	 * All associated indexes are updated
	 */
	@Test
	public void deletingUserUpdatesAllIndexes() throws ValidationException {
		assertThat(pacemaker.getUsers().size(), is(users.length));
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		pacemaker.createActivity(marge.id, activities[0].type, activities[0].location, activities[0].distance,
				activities[0].date, activities[0].duration);
		int originalUserIndexCount = pacemaker.getUserIndex().size();
		int originalEmaiIndexCount = pacemaker.getEmailIndex().size();
		int originalActivityIndexCount = pacemaker.getActivitiesIndex().size();
		assertTrue(pacemaker.getActivitiesIndex().size() > 0);
		assertTrue(pacemaker.getUserIndex().keySet().contains(marge.id));
		pacemaker.deleteUser(marge);
		assertThat(pacemaker.getUsers().size(), is(users.length-1));
		assertThat(pacemaker.getUserIndex().size(), is(originalUserIndexCount-1));
		assertThat(pacemaker.getEmailIndex().size(), is(originalEmaiIndexCount-1));
		assertThat(pacemaker.getActivitiesIndex().size(), is(originalActivityIndexCount-1));
	}
	
	/**
	 * Given users exist in pacemaker
	 * When I delete all user
	 * All associated indexes are updated
	 */
	@Test
	public void deletingAllUsersUpdatesAllIndexes() throws ValidationException {
		assertThat(pacemaker.getUsers().size(), is(users.length));
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		pacemaker.createActivity(marge.id, activities[0].type, activities[0].location, activities[0].distance,
				activities[0].date, activities[0].duration);
		
		assertThat(pacemaker.getUsers().size(), is(not(equalTo(0))));
		assertThat(pacemaker.getUserIndex().size(), is(not(equalTo(0))));
		assertThat(pacemaker.getEmailIndex().size(), is(not(equalTo(0))));
		assertThat(pacemaker.getActivitiesIndex().size(), is(not(equalTo(0))));

		pacemaker.deleteUsers();
		
		assertThat(pacemaker.getUsers().size(), is(equalTo(0)));
		assertThat(pacemaker.getUserIndex().size(), is(equalTo(0)));
		assertThat(pacemaker.getEmailIndex().size(), is(equalTo(0)));
		assertThat(pacemaker.getActivitiesIndex().size(), is(equalTo(0)));
	}

	/**
	 * Given I add an activity to a valid user
	 * That activity is added to pacemaker
	 * And the associated indexes are updated accordingly
	 */
	@Test
	public void testAddActivity() throws ValidationException {
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		Activity activity = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location,
				activities[0].distance, activities[0].date, activities[0].duration);
		Activity returnedActivity = pacemaker.getActivity(activity.id);
		assertThat(returnedActivity, allOf( 
				is(notNullValue()),
				is(equalTo(activities[0])),
				is(not(sameInstance(activities[0]))) //ids not the same since static activities not created by pacemaker
		));
	}

	/**
	 * Given a known collection of activities
	 * When I list the activities using a known sorting method
	 * Then the activities are listed in the correct order
	 */
	@Test
	public void testListActivities() throws ValidationException {
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		Activity walkFridge = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location,
				activities[0].distance, activities[0].date, activities[0].duration);

		Activity runWork = pacemaker.createActivity(marge.id, activities[2].type, activities[2].location,
				activities[2].distance, activities[2].date, activities[2].duration);

		Activity cycleSchool = pacemaker.createActivity(marge.id, activities[4].type, activities[4].location,
				activities[4].distance, activities[4].date, activities[4].duration);

		Collection<Activity> acts = pacemaker.listActivities(marge.id);
		assertEquals(3, acts.size());
		assertEquals(walkFridge, acts.toArray()[0]);
		assertEquals(runWork, acts.toArray()[1]);
		assertEquals(cycleSchool, acts.toArray()[2]);

		acts = pacemaker.listActivities(marge.id, "TYPE");
		assertEquals(3, acts.size());
		assertEquals(cycleSchool, acts.toArray()[0]);
		assertEquals(runWork, acts.toArray()[1]);
		assertEquals(walkFridge, acts.toArray()[2]);

		acts = pacemaker.listActivities(marge.id, "DURATION");
		assertEquals(3, acts.size());
		assertEquals(walkFridge, acts.toArray()[0]);
		assertEquals(runWork, acts.toArray()[1]);
		assertEquals(cycleSchool, acts.toArray()[2]);

		acts = pacemaker.listActivities(marge.id, "LOCATION");
		assertEquals(3, acts.size());
		assertEquals(walkFridge, acts.toArray()[0]);
		assertEquals(cycleSchool, acts.toArray()[1]);
		assertEquals(runWork, acts.toArray()[2]);

		acts = pacemaker.listActivities(marge.id, "DATE");
		assertEquals(3, acts.size());
		assertEquals(walkFridge, acts.toArray()[0]);
		assertEquals(runWork, acts.toArray()[1]);
		assertEquals(cycleSchool, acts.toArray()[2]);

		acts = pacemaker.listActivities(marge.id, "DISTANCE");
		assertEquals(3, acts.size());
		assertEquals(walkFridge, acts.toArray()[0]);
		assertEquals(runWork, acts.toArray()[1]);
		assertEquals(cycleSchool, acts.toArray()[2]);
	}
	
	/**
	 * Given a known collection of activities
	 * When I list the activities between two dates
	 * Then the relevant activities are listed
	 */
	@Test
	public void testListActivitiesBetweenDates() throws ValidationException {
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		Activity walkFridge = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location,
				activities[0].distance, activities[0].date, activities[0].duration);

		Activity runWork = pacemaker.createActivity(marge.id, activities[2].type, activities[2].location,
				activities[2].distance, activities[2].date, activities[2].duration);

		Activity cycleSchool = pacemaker.createActivity(marge.id, activities[4].type, activities[4].location,
				activities[4].distance, activities[4].date, activities[4].duration);

		Collection<Activity> acts = new ArrayList<Activity>();

		acts = pacemaker.listActivitiesBetweenDates(walkFridge.date, cycleSchool.date);
		assertThat(acts.size(), is(3));		

		acts = pacemaker.listActivitiesBetweenDates(walkFridge.date, runWork.date);
		assertThat(acts.size(), is(2));

		acts = pacemaker.listActivitiesBetweenDates(walkFridge.date, walkFridge.date);
		assertThat(acts.size(), is(1));

		acts = pacemaker.listActivitiesBetweenDates(DateTimeUtils.convertStringToLocalDateTime("12:10:2012 8:00:00"),
				DateTimeUtils.convertStringToLocalDateTime("12:10:2013 8:45:00"));
		assertThat(acts.size(), is(0));

		acts = pacemaker.listActivitiesBetweenDates(DateTimeUtils.convertStringToLocalDateTime("12:10:2012 8:00:00"),
				DateTimeUtils.convertStringToLocalDateTime("12:10:2013 9:45:00"));
		assertThat(acts.size(), is(1));

		acts = pacemaker.listActivitiesBetweenDates(DateTimeUtils.convertStringToLocalDateTime("12:10:2016 9:10:00"),
				DateTimeUtils.convertStringToLocalDateTime("12:10:2017 9:00:00"));
		assertThat(acts.size(), is(0));

		acts = pacemaker.listActivitiesBetweenDates(DateTimeUtils.convertStringToLocalDateTime("12:10:2016 8:10:00"),
				DateTimeUtils.convertStringToLocalDateTime("12:10:2017 9:00:00"));
		assertThat(acts.size(), is(1));

		// Dates in wrong order
		acts = pacemaker.listActivitiesBetweenDates(DateTimeUtils.convertStringToLocalDateTime("12:10:2017 9:00:00"),
				DateTimeUtils.convertStringToLocalDateTime("12:10:2016 8:10:00"));
		assertThat(acts.size(), is(0));
	}


	/**
	 * Given a known collection of users
	 * When I list the users using a known sorting method
	 * Then the users are listed in the correct order
	 */
	@Test
	public void testListUsers() throws ValidationException {
		pacemaker = new PacemakerAPI(null);
		pacemaker.createUser("ann", "yoman", "redroom@gmail.com", "secret");
		pacemaker.createUser("zara", "brett", "seven@gmail.com", "secret");
		pacemaker.createUser("triona", "doyle", "blah@gmail.com", "secret");
		pacemaker.createUser("nicola", "goral", "ng@gmail.com", "secret");

		User annYoman = pacemaker.getUserByEmail("redroom@gmail.com");
		User zaraBrett = pacemaker.getUserByEmail("seven@gmail.com");
		User trionaDoyle = pacemaker.getUserByEmail("blah@gmail.com");
		User nicolaGoral = pacemaker.getUserByEmail("ng@gmail.com");

		Collection<User> users = pacemaker.listUsers("ID");
		assertEquals(4, users.size());

		assertEquals(annYoman, users.toArray()[0]);
		assertEquals(zaraBrett, users.toArray()[1]);
		assertEquals(trionaDoyle, users.toArray()[2]);
		assertEquals(nicolaGoral, users.toArray()[3]);

		users = pacemaker.listUsers("FIRSTNAME");
		assertEquals(4, users.size());
		assertEquals(annYoman, users.toArray()[0]);
		assertEquals(nicolaGoral, users.toArray()[1]);
		assertEquals(trionaDoyle, users.toArray()[2]);
		assertEquals(zaraBrett, users.toArray()[3]);

		users = pacemaker.listUsers("LASTNAME");
		assertEquals(4, users.size());
		assertEquals(zaraBrett, users.toArray()[0]);
		assertEquals(trionaDoyle, users.toArray()[1]);
		assertEquals(nicolaGoral, users.toArray()[2]);
		assertEquals(annYoman, users.toArray()[3]);

		users = pacemaker.listUsers("EMAIL");
		assertEquals(4, users.size());
		assertEquals(trionaDoyle, users.toArray()[0]);
		assertEquals(nicolaGoral, users.toArray()[1]);
		assertEquals(annYoman, users.toArray()[2]);
		assertEquals(zaraBrett, users.toArray()[3]);
	}

	/**
	 * When I add an Location to a given activity
	 * The activities route is updated
	 */
	@Test
	public void testAddActivityWithSingleLocation() throws ValidationException {
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		Long activityId = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location,
				activities[0].distance, activities[0].date, activities[0].duration).id;
		Activity activity = pacemaker.getActivity(activityId);
		assertThat(activity.route.size(),is(equalTo(0)));
		
		pacemaker.addLocation(activityId, locations[0].latitude, locations[0].longitude);

		activity = pacemaker.getActivity(activityId);
		
		assertThat(activity.route.size(),is(equalTo(1)));
		assertEquals(0.0001, locations[0].latitude, activity.route.get(0).latitude);
		assertEquals(0.0001, locations[0].longitude, activity.route.get(0).longitude);
	}

	/**
	 * When I add multiple Locations to a given activity
	 * The activities route is updated
	 */
	@Test
	public void testAddActivityWithMultipleLocation() throws ValidationException {
		User marge = pacemaker.getUserByEmail("marge@simpson.com");
		Long activityId = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location,
				activities[0].distance, activities[0].date, activities[0].duration).id;

		for (Location location : locations) {
			pacemaker.addLocation(activityId, location.latitude, location.longitude);
		}

		Activity activity = pacemaker.getActivity(activityId);
		assertEquals(locations.length, activity.route.size());
		int i = 0;
		for (Location location : activity.route) {
			assertThat(locations[i], is(equalTo(location)) );
			i++;
		}

	}

}