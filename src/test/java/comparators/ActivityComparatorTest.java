package comparators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.ValidationException;
import models.Activity;
import utils.DateTimeUtils;
import comparators.ActivityDateComparator;
import comparators.ActivityDistanceComparator;
import comparators.ActivityDurationComparator;
import comparators.ActivityIdComparator;
import comparators.ActivityLocationComparator;
import comparators.ActivityTypeComparator;

import static org.hamcrest.core.Is.is;

public class ActivityComparatorTest {
	
	Activity crawlFridge;
	Activity walkSchool;
	Activity runShop;
	Activity jumpUniversity;
	Activity cartwheelCircus;
	List<Activity> activities;
	
	@Before
	public void setup() throws ValidationException {
		crawlFridge = new Activity ("crawl","fridge",10.1, 
				DateTimeUtils.convertStringToLocalDateTime("20:01:2016 00:00:00"), 
				DateTimeUtils.convertStringToDuration("00:30:00"));
		walkSchool = new Activity ("walk","school",2.2, 
				DateTimeUtils.convertStringToLocalDateTime("20:02:2016 00:00:00"), 
				DateTimeUtils.convertStringToDuration("05:00:00"));
		runShop = new Activity ("run","shop",4.1, 
				DateTimeUtils.convertStringToLocalDateTime("20:03:2016 00:00:00"), 
				DateTimeUtils.convertStringToDuration( "04:00:00"));
		jumpUniversity = new Activity ("jump","university",3.1, 
				DateTimeUtils.convertStringToLocalDateTime("20:04:2016 00:00:00"), 
				DateTimeUtils.convertStringToDuration( "00:30:05"));
		cartwheelCircus = new Activity ("cartwheel","circus",7.1, 
				DateTimeUtils.convertStringToLocalDateTime("20:04:2015 00:00:00"), 
				DateTimeUtils.convertStringToDuration( "04:30:01"));
		activities = new ArrayList<>();
		activities.add(crawlFridge);
		activities.add(walkSchool);
		activities.add(runShop);
		activities.add(jumpUniversity);
		activities.add(cartwheelCircus);
	}

	@After
	public void tearDown() {
		
	}
	
	
	/**
	 * Given no comparators have been applied
	 * When I check the array
	 * Then the activities are in the order they were added
	 */
	@Test
	public void validateInitialArray() {
		assertThat(activities.get(0), is(crawlFridge));
		assertThat(activities.get(1), is(walkSchool));
		assertThat(activities.get(2), is(runShop));
		assertThat(activities.get(3), is(jumpUniversity));
		assertThat(activities.get(4), is(cartwheelCircus));
	}
	
	/**
	 * Given I sort the activities array using an id comparator
	 * When I inspect the array
	 * Then the entries are sorted by id
	 */
	@Test
	public void sortActivitiesById(){
		Collections.sort(activities, new ActivityIdComparator());
		assertThat(activities.get(0), is(crawlFridge));
		assertThat(activities.get(1), is(walkSchool));
		assertThat(activities.get(2), is(runShop));
		assertThat(activities.get(3), is(jumpUniversity));
		assertThat(activities.get(4), is(cartwheelCircus));
	}
	
	/**
	 * Given I sort the activities array using a type comparator
	 * When I inspect the array
	 * Then the entries are sorted by type
	 */
	@Test
	public void sortActivitiesByType(){
		Collections.sort(activities, new ActivityTypeComparator());
		assertThat(activities.get(0), is(cartwheelCircus));
		assertThat(activities.get(1), is(crawlFridge));
		assertThat(activities.get(2), is(jumpUniversity));
		assertThat(activities.get(3), is(runShop));
		assertThat(activities.get(4), is(walkSchool));
	}
	
	/**
	 * Given I sort the activities array using a location comparator
	 * When I inspect the array
	 * Then the entries are sorted by location
	 */
	@Test
	public void sortActivitiesByLocation(){
		Collections.sort(activities, new ActivityLocationComparator());
		assertThat(activities.get(0), is(cartwheelCircus));
		assertThat(activities.get(1), is(crawlFridge));
		assertThat(activities.get(2), is(walkSchool));
		assertThat(activities.get(3), is(runShop));
		assertThat(activities.get(4), is(jumpUniversity));
	}
	
	/**
	 * Given I sort the activities array using a distance comparator
	 * When I inspect the array
	 * Then the entries are sorted by distance
	 */
	@Test
	public void sortActivitiesByDistance(){
		Collections.sort(activities, new ActivityDistanceComparator());
		assertThat(activities.get(0), is(walkSchool));
		assertThat(activities.get(1), is(jumpUniversity));
		assertThat(activities.get(2), is(runShop));
		assertThat(activities.get(3), is(cartwheelCircus));
		assertThat(activities.get(4), is(crawlFridge));
	}
	
	/**
	 * Given I sort the activities array using a duration comparator
	 * When I inspect the array
	 * Then the entries are sorted by duration
	 */
	@Test
	public void sortActivitiesByDuration(){
		Collections.sort(activities, new ActivityDurationComparator());
		assertThat(activities.get(0), is(crawlFridge));
		assertThat(activities.get(1), is(jumpUniversity));
		assertThat(activities.get(2), is(runShop));
		assertThat(activities.get(3), is(cartwheelCircus));
		assertThat(activities.get(4), is(walkSchool));
	}
	
	/**
	 * Given I sort the activities array using a date comparator
	 * When I inspect the array
	 * Then the entries are sorted by date
	 */
	@Test
	public void sortActivitiesByDate(){
		Collections.sort(activities, new ActivityDateComparator());
		assertThat(activities.get(0), is(cartwheelCircus));
		assertThat(activities.get(1), is(crawlFridge));
		assertThat(activities.get(2), is(walkSchool));
		assertThat(activities.get(3), is(runShop));
		assertThat(activities.get(4), is(jumpUniversity));
	}

	

}
