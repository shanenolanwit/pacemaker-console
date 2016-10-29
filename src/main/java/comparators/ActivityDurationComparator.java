package comparators;

import java.util.Comparator;
import models.Activity;

/**
 * Standard Comparator used for comparing two activities by their duration
 * @see models.Activity
 * @author Shane Nolan
 */
public class ActivityDurationComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		return o1.duration.compareTo(o2.duration);
	}

}
