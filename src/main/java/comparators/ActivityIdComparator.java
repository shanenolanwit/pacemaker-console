package comparators;

import java.util.Comparator;
import models.Activity;

public class ActivityIdComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		return Long.compare(o1.id, o2.id);
	}



}
