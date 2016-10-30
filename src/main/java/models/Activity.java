package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.List;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.common.base.Objects;

import exceptions.ValidationException;
import utils.DateTimeUtils;
import utils.FileLogger;

/**
 * Represents a physical activity. Belongs to a {@link models.User}
 * @author Shane Nolan
 */
@SuppressWarnings("serial")
public class Activity implements Serializable {

	public static Long counter = new Long(0);
	public Long id;
	public String type;
	public String location;
	public double distance;
	public LocalDateTime date;
	public Duration duration;
	
	public List<Location> route = new ArrayList<>();

	public Activity() {
	}

	/**
	 * Standard activity constructor used by the pacemaker api
	 * @param type  String representing the type of activity
	 * @param location  String representing the general region where an activity took place
	 * @param distance  Double representing distance in km, traveled, while performing the activity
	 * @param date  LocalDateTime representing when the activity started
	 * @param duration  Duration representing how long the activity lasted
	 * @throws ValidationException exception representing any creation errors
	 */
	public Activity(String type, String location, double distance, 
			LocalDateTime date, Duration duration) throws ValidationException {
		if(!type.isEmpty()){
			this.type = type;
		} else {
			throw new ValidationException("Invalid type");
		} if(!location.isEmpty()){
			this.location = location;
		} else {
			throw new ValidationException("Invalid location");
		} if(distance > 0){
			this.distance = distance;
		} else {
			throw new ValidationException("Invalid distance");
		}
		
		this.date = date;
		this.duration = duration;
		this.id = counter++;
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
			FileLogger.getLogger().log("Expected Activity, got " + obj.getClass().getSimpleName());
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
