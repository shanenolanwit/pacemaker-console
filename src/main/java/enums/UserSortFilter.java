package enums;

import java.util.Comparator;

import comparators.UserEmailComparator;
import comparators.UserFirstNameComparator;
import comparators.UserIdComparator;
import comparators.UserLastNameComparator;
import models.User;


public enum UserSortFilter {	
	
	FIRSTNAME(new UserFirstNameComparator()),
	LASTNAME(new UserLastNameComparator()),
	EMAIL(new UserEmailComparator()),
	ID(new UserIdComparator());
	
	private final Comparator<User> comparator;
	
	private UserSortFilter(Comparator<User> comparator){
		this.comparator = comparator;
	}

	public Comparator<User> getComparator() {
		return comparator;
	}
	
	public static UserSortFilter identify(String identifier){
		return UserSortFilter.valueOf(identifier.toUpperCase());
	}

	public static boolean exists(String sortBy) {
		boolean exists = false;
		try{
			UserSortFilter.valueOf(sortBy.toUpperCase());
			exists = true;
		} catch(IllegalArgumentException | NullPointerException e){
			
		}
		return exists;
	}
	
}
