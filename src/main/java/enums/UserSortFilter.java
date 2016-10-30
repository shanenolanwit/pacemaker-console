package enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import comparators.UserEmailComparator;
import comparators.UserFirstNameComparator;
import comparators.UserIdComparator;
import comparators.UserLastNameComparator;
import models.User;
import utils.FileLogger;

/**
 * UserSortFilter links enum values to comparators
 * @author Shane Nolan
 *  Available Filters
 *  <ul>
 *  <li>{@link #FIRSTNAME}</li>
 *  <li>{@link #LASTNAME}</li>
 *  <li>{@link #EMAIL}</li>
 *  <li>{@link #ID}</li>
 *  </ul>
 *
 */
public enum UserSortFilter {
	
	/**
	 * @see models.User#firstName
	 */
	FIRSTNAME(new UserFirstNameComparator()),
	/**
	 * @see models.User#lastName
	 */
	LASTNAME(new UserLastNameComparator()),
	/**
	 * @see models.User#email
	 */
	EMAIL(new UserEmailComparator()),
	/**
	 * @see models.User#id
	 */
	ID(new UserIdComparator());
	
	private final Comparator<User> comparator;
	
	private UserSortFilter(Comparator<User> comparator){
		this.comparator = comparator;
	}

	public Comparator<User> getComparator() {
		return comparator;
	}
	
	/**
	 * Identifies which {@link enums.UserSortFilter} to use based on a given input
	 * @param identifier  A string representing a {@link enums.UserSortFilter}
	 * <p>The identifier is not case sensitive
	 * Valid identifiers are (ID|FIRSTNAME|LASTNAME|EMAIL)
	 * @return  {@link enums.UserSortFilter} associated with the given identifier
	 */
	public static UserSortFilter identify(String identifier){
		return UserSortFilter.valueOf(identifier.toUpperCase());
	}

	/**
	 * Checks if a given identifier represents a valid {@link enums.UserSortFilter}
	 * @param identifier  A string representing the type of {@link enums.UserSortFilter}
	 * @return  boolean representing whether the identifier refers to a valid {@link enums.UserSortFilter}
	 */
	public static boolean exists(String identifier) {
		boolean exists = false;
		try{
			UserSortFilter.valueOf(identifier.toUpperCase());
			exists = true;
		} catch(IllegalArgumentException | NullPointerException e){
			FileLogger.getLogger().log(e.getMessage());
		}
		return exists;
	}
	
	/**
	 * Uses the factory pattern to determine a sorting algorithm. Defaults to sorting by id.
	 * @param users unsorted collection of users
	 * @param sortBy sort strategy
	 * @return collection of users sorted using the specified strategy
	 */
	public static List<User> sort(Collection<User> users, String sortBy){
		List<User> sortedUsers = new ArrayList<>(users);
		if(sortBy.toLowerCase().equals("firstname")){
			sortedUsers = users.stream().sorted((u1,u2)-> {
				return u1.firstName.compareTo(u2.firstName);
			}).collect(Collectors.toList());
		} else if(sortBy.toLowerCase().equals("lastname")){
			sortedUsers = users.stream().sorted((u1,u2)-> {
				return u1.lastName.compareTo(u2.lastName);
			}).collect(Collectors.toList());
		} else if(sortBy.toLowerCase().equals("email")){
			sortedUsers = users.stream().sorted((u1,u2)-> {
				return u1.email.compareTo(u2.email);
			}).collect(Collectors.toList());
		} else {
			sortedUsers = users.stream().sorted((u1,u2)-> {
				return u1.id.compareTo(u2.id);
			}).collect(Collectors.toList());
		}
		
		return sortedUsers;
	}
	
}
