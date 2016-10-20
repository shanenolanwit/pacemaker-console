package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;

import com.google.common.base.Objects;

public class Location implements Serializable {

	static Long counter = 0l;
	public Long id;
	public Double longitude;
	public Double latitude;

	public Location() {
	}

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
			System.out.println("Expected Location, got " + obj.getClass().getSimpleName());
		}
		return equal;
	}

	@Override
	public String toString() {
		return toStringHelper(this).addValue(id).addValue(latitude).addValue(longitude).toString();
	}
}
