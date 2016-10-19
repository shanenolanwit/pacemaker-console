package comparators;

import java.util.Comparator;
import models.Activity;

public class ActivityDistanceComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		return Double.compare(o1.distance, o2.distance);
	}



}
