package comparators;

import java.util.Comparator;
import models.Activity;

public class ActivityDateComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		return o1.date.compareTo(o2.date);
	}



}
