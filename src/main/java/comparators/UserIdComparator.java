package comparators;

import java.util.Comparator;
import models.User;

public class UserIdComparator implements Comparator<User> {

	@Override
	public int compare(User o1, User o2) {
		return Long.compare(o1.id, o2.id);
	}



}
