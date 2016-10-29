package comparators;

import java.util.Comparator;
import models.User;

/**
 * Standard Comparator used for comparing two users by their email address
 * @see models.User
 * @author Shane Nolan
 */
public class UserEmailComparator implements Comparator<User> {

	@Override
	public int compare(User o1, User o2) {
		return o1.email.compareToIgnoreCase(o2.email);
	}

}
