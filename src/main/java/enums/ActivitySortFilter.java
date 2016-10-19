package enums;

import java.util.Comparator;

import comparators.ActivityDateComparator;
import comparators.ActivityDistanceComparator;
import comparators.ActivityDurationComparator;
import comparators.ActivityIdComparator;
import comparators.ActivityLocationComparator;
import comparators.ActivityTypeComparator;
import models.Activity;


public enum ActivitySortFilter {	
	
	DATE(new ActivityDateComparator()),
	DISTANCE(new ActivityDistanceComparator()),
	DURATION(new ActivityDurationComparator()),
	TYPE(new ActivityTypeComparator()),
	LOCATION(new ActivityLocationComparator()),
	ID(new ActivityIdComparator());
	
	private final Comparator<Activity> comparator;
	
	private ActivitySortFilter(Comparator<Activity> comparator){
		this.comparator = comparator;
	}

	public Comparator<Activity> getComparator() {
		return comparator;
	}
	
	public static ActivitySortFilter identify(String identifier){
		return ActivitySortFilter.valueOf(identifier.toUpperCase());
	}

	public static boolean exists(String sortBy) {
		boolean exists = false;
		try{
			ActivitySortFilter.valueOf(sortBy.toUpperCase());
			exists = true;
		} catch(IllegalArgumentException | NullPointerException e){
			
		}
		return exists;
	}
	
}
