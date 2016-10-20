package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.List;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.common.base.Objects;

import utils.DateTimeUtils;

public class Activity implements Serializable {

	public static Long counter = 0l;
	public Long id;
	public String type;
	public String location;
	public double distance;
	public LocalDateTime date;
	public Duration duration;
	
	public List<Location> route = new ArrayList<>();

	public Activity() {
	}

	public Activity(String type, String location, double distance, LocalDateTime date, Duration duration) {
		this.id = counter++;
		this.type = type;
		this.location = location;
		this.distance = distance;
		this.date = date;
		this.duration = duration;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean equal = false;
		if (obj instanceof Activity) {
			final Activity other = (Activity) obj;
			equal = Objects.equal(type, other.type)
					&& Objects.equal(location, other.location)
					&& Objects.equal(distance, other.distance)
					&& Objects.equal(date, other.date)
					&& Objects.equal(duration, other.duration)
					&& Objects.equal(route, other.route);
		} else {
			System.out.println("Expected Location, got " + obj.getClass().getSimpleName());
		}
		return equal;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.id, this.type, this.location, this.distance, this.date, this.duration);
	}

	@Override
	public String toString() {
		return toStringHelper(this).addValue(id).addValue(type).addValue(location)
				.addValue(distance).addValue(date).addValue(DateTimeUtils.convertDurationToString(duration)).addValue(route)
				.toString();
	}
}
