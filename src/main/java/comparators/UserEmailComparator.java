package comparators;

import java.util.Comparator;
import models.User;

public class UserEmailComparator implements Comparator<User> {

	@Override
	public int compare(User o1, User o2) {
		return o1.firstName.compareToIgnoreCase(o2.firstName);
	}



}
