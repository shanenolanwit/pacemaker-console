package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;

public class User implements Serializable {
	public static Long counter = 0l;
	public Long id;
	public String firstName;
	public String lastName;
	public String email;
	public String password;
	public Map<Long, Activity> activities = new HashMap<>();

	public User() {
	}

	public User(String firstName, String lastName, String email, String password) {
		this.id = counter++;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.lastName, this.firstName, this.email, this.password);
	}

	@Override
	public boolean equals(final Object obj) {
		boolean equal = false;
		if (obj instanceof User) {
			final User other = (User) obj;
			equal = Objects.equal(firstName, other.firstName)
					&& Objects.equal(lastName, other.lastName)
					&& Objects.equal(email, other.email)
					&& Objects.equal(password, other.password)
					&& Objects.equal(activities, other.activities);
		} else {
			System.out.println("Expected User, got "
					+ obj.getClass().getSimpleName());
		}
		return equal;
	}

	@Override
	public String toString() {
		return toStringHelper(this).addValue(id).addValue(firstName).addValue(lastName).addValue(password)
				.addValue(email).addValue(activities).toString();
	}
}