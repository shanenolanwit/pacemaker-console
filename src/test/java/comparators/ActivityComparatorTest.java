package comparators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.ActivitySortFilter;
import models.Activity;
import utils.DateTimeUtils;
import comparators.ActivityDateComparator;
import comparators.ActivityDistanceComparator;
import comparators.ActivityDurationComparator;
import comparators.ActivityIdComparator;
import comparators.ActivityLocationComparator;
import comparators.ActivityTypeComparator;

public class ActivityComparatorTest {
	
	Activity a1;
	Activity a2;
	Activity a3;
	Activity a4;
	Activity a5;
	List<Activity> activities;
	
	@Before
	public void setup() {
		a1 = new Activity ("crawl","fridge",10.1, 
				DateTimeUtils.convertStringToLocalDateTime("20:01:2016 00:00:00"), DateTimeUtils.convertStringToDuration("00:30:00"));
		a2 = new Activity ("walk","school",2.2, 
				DateTimeUtils.convertStringToLocalDateTime("20:02:2016 00:00:00"), DateTimeUtils.convertStringToDuration("05:00:00"));
		a3 = new Activity ("run","shop",4.1, 
				DateTimeUtils.convertStringToLocalDateTime("20:03:2016 00:00:00"), DateTimeUtils.convertStringToDuration( "04:00:00"));
		a4 = new Activity ("jump","university",3.1, 
				DateTimeUtils.convertStringToLocalDateTime("20:04:2016 00:00:00"), DateTimeUtils.convertStringToDuration( "00:30:05"));
		a5 = new Activity ("cartwheel","circus",7.1, 
				DateTimeUtils.convertStringToLocalDateTime("20:04:2015 00:00:00"), DateTimeUtils.convertStringToDuration( "04:30:01"));
		activities = new ArrayList<>();
		activities.add(a1);
		activities.add(a2);
		activities.add(a3);
		activities.add(a4);
		activities.add(a5);
	}

	@After
	public void tearDown() {
		
	}
	
	
	
	@Test
	public void testInitialArray() {
		assertEquals(a1,activities.get(0));
		assertEquals(a2,activities.get(1));
		assertEquals(a3,activities.get(2));
		assertEquals(a4,activities.get(3));
		assertEquals(a5,activities.get(4));
	}
	
	@Test
	public void testIdComparator(){
		Collections.sort(activities, new ActivityIdComparator());
		assertEquals(a1,activities.get(0));
		assertEquals(a2,activities.get(1));
		assertEquals(a3,activities.get(2));
		assertEquals(a4,activities.get(3));
		assertEquals(a5,activities.get(4));
	}
	
	@Test
	public void testTypeComparator(){
		Collections.sort(activities, new ActivityTypeComparator());
		assertEquals(a5,activities.get(0));
		assertEquals(a1,activities.get(1));
		assertEquals(a4,activities.get(2));
		assertEquals(a3,activities.get(3));
		assertEquals(a2,activities.get(4));
	}
	
	@Test
	public void testLocationComparator(){
		Collections.sort(activities, new ActivityLocationComparator());
		assertEquals(a5,activities.get(0));
		assertEquals(a1,activities.get(1));
		assertEquals(a2,activities.get(2));
		assertEquals(a3,activities.get(3));
		assertEquals(a4,activities.get(4));
	}
	
	@Test
	public void testDistanceComparator(){
		Collections.sort(activities, new ActivityDistanceComparator());
		assertEquals(a2,activities.get(0));
		assertEquals(a4,activities.get(1));
		assertEquals(a3,activities.get(2));
		assertEquals(a5,activities.get(3));
		assertEquals(a1,activities.get(4));
	}
	
	@Test
	public void testDurationComparator(){
		Collections.sort(activities, new ActivityDurationComparator());
		assertEquals(a1,activities.get(0));
		assertEquals(a4,activities.get(1));
		assertEquals(a3,activities.get(2));
		assertEquals(a5,activities.get(3));
		assertEquals(a2,activities.get(4));
	}
	
	@Test
	public void testDateComparator(){
		Collections.sort(activities, new ActivityDateComparator());
		assertEquals(a5,activities.get(0));
		assertEquals(a1,activities.get(1));
		assertEquals(a2,activities.get(2));
		assertEquals(a3,activities.get(3));
		assertEquals(a4,activities.get(4));
	}

	

}
