package models;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import exceptions.ValidationException;
import utils.DateTimeUtils;

import static models.Fixtures.activities;

public class ActivityTest {
	

	@Test
	public void testCreate() throws ValidationException {
		Activity test = new Activity("walk", "fridge", 0.001,
				DateTimeUtils.convertStringToLocalDateTime("12:10:2013 9:00:00"),
				DateTimeUtils.convertStringToDuration("01:02:03"));
		assertEquals("walk", test.type);
		assertEquals("fridge", test.location);
		assertEquals(0.0001, 0.001, test.distance);
	}

	@Test
	public void testIds() {
		Set<Long> ids = new HashSet<>();
		for (Activity activity : activities) {
			ids.add(activity.id);
		}
		assertEquals(activities.length, ids.size());
	}

	@Test
	public void testEquality() {
		Object x = activities[0];
		assertTrue(activities[0].equals(x));
		assertFalse(activities[0].equals(activities[1]));
		assertFalse(activities[0].equals(new String("hello world")));
		
		Activity actA = new Activity();
		actA.id = (long) 44;
		actA.type = "walk";
		actA.location = "earth";
		actA.distance = 10.4;
		actA.date = activities[0].date;
		actA.duration = activities[0].duration;
		actA.route.add(new Location(20.2, 30.3));
		Activity actB = new Activity();
		actB.id = (long) 88;
		actB.type = "run";
		actB.location = "jupiter";
		actB.distance = 40.1;
		actB.date = activities[1].date;
		actB.duration = activities[1].duration;
		
		assertFalse(actA.equals(actB));
		actB.type = "walk";
		assertFalse(actA.equals(actB));
		actB.location = "earth";
		assertFalse(actA.equals(actB));
		actB.distance = 10.4;
		assertFalse(actA.equals(actB));
		actB.date = activities[0].date;
		assertFalse(actA.equals(actB));
		actB.duration = activities[0].duration;
		assertFalse(actA.equals(actB));
		actB.route.add(new Location(20.2, 30.3));
		
		assertTrue(actA.equals(actB));
		assertNotEquals(actA.hashCode(), actB.hashCode());
		
		
	}

	@Test
	public void testToString() throws ValidationException {
		Activity test = new Activity("walk", "fridge", 0.001,
				DateTimeUtils.convertStringToLocalDateTime("12:10:2013 9:00:00"),
				DateTimeUtils.convertStringToDuration("01:02:03"));
		assertEquals("Activity{" + test.id + ", walk, fridge, 0.001, 2013-10-12T09:00, 1:02:03, []}", test.toString());
	}
}