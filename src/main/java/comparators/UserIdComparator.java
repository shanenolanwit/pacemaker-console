package comparators;

import java.util.Comparator;
import models.User;

/**
 * Standard Comparator used for comparing two users by their id
 * @see models.User
 * @author Shane Nolan
 */
public class UserIdComparator implements Comparator<User> {

	@Override
	public int compare(User o1, User o2) {
		return Long.compare(o1.id, o2.id);
	}
	
}
