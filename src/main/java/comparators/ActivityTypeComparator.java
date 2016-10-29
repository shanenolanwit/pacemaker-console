package comparators;

import java.util.Comparator;
import models.Activity;

/**
 * Standard Comparator used for comparing two activities by their type
 * @see models.Activity
 * @author Shane Nolan
 */
public class ActivityTypeComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		return o1.type.compareToIgnoreCase(o2.type);
	}

}
