package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;

import com.google.common.base.Objects;

import utils.FileLogger;

/**
 * Location representing a point on a map
 * @author Shane Nolan
 */
@SuppressWarnings("serial")
public class Location implements Serializable {

	public static Long counter = new Long(0);
	public Long id;
	public Double longitude;
	public Double latitude;

	public Location() {
	}

	/**
	 * Standard constructor used by pacemaker api
	 * @param latitude  Double representing latitude
	 * @param longitude  Double represnting longitude
	 */
	public Location(Double latitude, Double longitude) {
		this.id = counter++;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.id, this.longitude, this.latitude);
	}

	@Override
	public boolean equals(final Object obj) {
		boolean equal = false;
		if (obj instanceof Location) {
			final Location other = (Location) obj;
			equal = Objects.equal(latitude, other.latitude)
					&& Objects.equal(longitude, other.longitude);
		} else {
			FileLogger.getLogger().log("Expected Location, got " + obj.getClass().getSimpleName());
		}
		return equal;
	}

	@Override
	public String toString() {
		return toStringHelper(this).addValue(id).addValue(latitude).addValue(longitude).toString();
	}
}
