package comparators;

import java.util.Comparator;
import models.Activity;

/**
 * Standard Comparator used for comparing two activities by their location
 * @see models.Activity
 * @author Shane Nolan
 */
public class ActivityLocationComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		return o1.location.compareToIgnoreCase(o2.location);
	}

}
