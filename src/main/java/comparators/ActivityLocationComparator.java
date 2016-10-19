package comparators;

import java.util.Comparator;
import models.Activity;

public class ActivityLocationComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		return o1.location.compareTo(o2.location);
	}



}
