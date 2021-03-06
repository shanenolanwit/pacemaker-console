package comparators;

import java.util.Comparator;
import models.User;

/**
 * Standard Comparator used for comparing two users by their last name
 * @see models.User
 * @author Shane Nolan
 */
public class UserLastNameComparator implements Comparator<User> {

	@Override
	public int compare(User o1, User o2) {
		return o1.lastName.compareToIgnoreCase(o2.lastName);
	}

}
