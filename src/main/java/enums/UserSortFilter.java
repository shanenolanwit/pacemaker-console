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
	
	public static List<User> sort(Collection<User> users, String sortBy){
		List<User> sortedUsers = new ArrayList<>(users);
		if(sortBy.toLowerCase().equals("firstName")){
			sortedUsers = users.stream().sorted((u1,u2)-> {
				return u1.firstName.compareTo(u2.firstName);
			}).collect(Collectors.toList());
		} else if(sortBy.toLowerCase().equals("lastName")){
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
