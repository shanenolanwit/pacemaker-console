package enums;

import java.util.Comparator;

import comparators.ActivityDateComparator;
import comparators.ActivityDistanceComparator;
import comparators.ActivityDurationComparator;
import comparators.ActivityIdComparator;
import comparators.ActivityLocationComparator;
import comparators.ActivityTypeComparator;
import models.Activity;
import utils.FileLogger;

/**
 * 
 * @author Shane Nolan
 *  Available Filters
 *  <ul>
 *  <li>{@link #DATE}</li>
 *  <li>{@link #DISTANCE}</li>
 *  <li>{@link #DURATION}</li>
 *  <li>{@link #TYPE}</li>
 *  <li>{@link #LOCATION}</li>
 *  <li>{@link #ID}</li>
 *  </ul>
 *
 */
public enum ActivitySortFilter {	
	
	/**
	 * @see models.Activity#date
	 */
	DATE(new ActivityDateComparator()),
	/**
	 * @see models.Activity#distance
	 */
	DISTANCE(new ActivityDistanceComparator()),
	/**
	 * @see models.Activity#duration
	 */
	DURATION(new ActivityDurationComparator()),
	/**
	 * @see models.Activity#type
	 */
	TYPE(new ActivityTypeComparator()),
	/**
	 * @see models.Activity#location
	 */
	LOCATION(new ActivityLocationComparator()),
	/**
	 * @see models.Activity#id
	 */
	ID(new ActivityIdComparator());
	
	private final Comparator<Activity> comparator;
	
	private ActivitySortFilter(Comparator<Activity> comparator){
		this.comparator = comparator;
	}

	public Comparator<Activity> getComparator() {
		return comparator;
	}
	
	/**
	 * Identifies which {@link enums.ActivitySortFilter} to use based on a given input
	 * @param identifier  A string representing a {@link enums.ActivitySortFilter}
	 * <p>The identifier is not case sensitive
	 * <br/>
	 * Valid identifiers are (DATE|DISTANCE|TYPE|DATE|DURATION|LOCATION|ID)
	 * @return  {@link enums.ActivitySortFilter} associated with the given identifier
	 */
	public static ActivitySortFilter identify(String identifier){
		return ActivitySortFilter.valueOf(identifier.toUpperCase());
	}

	/**
	 * Checks if a given identifier represents a valid {@link enums.ActivitySortFilter}
	 * @param identifier  A string representing the type of {@link enums.ActivitySortFilter}
	 * @return  boolean representing whether the identifier refers to a valid {@link enums.ActivitySortFilter}
	 */
	public static boolean exists(String identifier) {
		boolean exists = false;
		try{
			ActivitySortFilter.valueOf(identifier.toUpperCase());
			exists = true;
		} catch(IllegalArgumentException | NullPointerException e){
			FileLogger.getLogger().log(e.getMessage());
		}
		return exists;
	}
	
}
