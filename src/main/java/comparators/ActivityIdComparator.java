package comparators;

import java.util.Comparator;
import models.Activity;

/**
 * Standard Comparator used for comparing two activities by their id
 * @see models.Activity
 * @author Shane Nolan
 */
public class ActivityIdComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		return Long.compare(o1.id, o2.id);
	}
	
}
