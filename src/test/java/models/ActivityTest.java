package models;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import utils.DateTimeUtils;

import static models.Fixtures.activities;

public class ActivityTest
{
	Activity test = new Activity ("walk",  "fridge", 0.001, DateTimeUtils.convertStringToLocalDateTime("12:10:2013 9:00:00"));

	@Test
	public void testCreate()
	{
		assertEquals ("walk",          test.type);
		assertEquals ("fridge",        test.location);
		assertEquals (0.0001, 0.001,   test.distance);    
	}

	@Test
	public void testIds()
	{
		Set<Long> ids = new HashSet<>();
		for (Activity activity : activities)
		{
			ids.add(activity.id);
		}
		assertEquals (activities.length, ids.size());
	}

	@Test
	public void testToString()
	{
		assertEquals ("Activity{" + test.id + ", walk, fridge, 0.001, 2013-10-12T09:00, []}", test.toString());
	}
}