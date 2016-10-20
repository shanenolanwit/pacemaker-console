package comparators;

import java.util.Comparator;
import models.Activity;

public class ActivityTypeComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		return o1.type.compareToIgnoreCase(o2.type);
	}



}
