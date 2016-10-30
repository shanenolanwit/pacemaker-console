package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Objects;

import exceptions.ValidationException;
import utils.FileLogger;

/**
 * User represents a human pacemaker user
 * @author Shane Nolan
 */
@SuppressWarnings("serial")
public class User implements Serializable {
	
	/**
	 * First name should start with a letter, should be at least 1 character long, 
	 * may contain spaces or hyphens but not at the start of the name
	 */
	public final static String FIRSTNAME_VALIDATION_PATTERN = "^[a-zA-Z][\\s-a-zA-Z]+$";
	/**
	 * Last name should start with a letter, should be at least 1 character long, 
	 * may contain apostrophes, spaces or hyphens but not at the start of the name
	 */
	public final static String LASTNAME_VALIDATION_PATTERN = "^[a-zA-Z][\\s-'a-zA-Z]+$";
	/**
	 * Email requirements based on official RFC and solutions from StackOverflow
	 */
	public final static String EMAIL_VALIDATION_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
	
	public static Long counter = new Long(0);
	public Long id;
	public String firstName;
	public String lastName;
	public String email;
	public String password;
	public Map<Long, Activity> activities = new HashMap<>();

	public User() {
	}

	/**
	 * Standard conctructor used by pacemaker api
	 * @param firstName  String representing users first name
	 * @param lastName  String representing users last name
	 * @param email  String representing users email address
	 * @param password  String representing users password
	 * @throws ValidationException exception representing any creation errors
	 */
	public User(String firstName, String lastName, String email, String password) throws ValidationException {
		if(Pattern.matches(User.FIRSTNAME_VALIDATION_PATTERN, firstName)){
			this.firstName = firstName;
		} else {
			throw new ValidationException("Invalid first name");
		}
		if(Pattern.matches(User.LASTNAME_VALIDATION_PATTERN, lastName)){
			this.lastName = lastName;
		} else {
			throw new ValidationException("Invalid last name");
		}
		if(Pattern.matches(User.EMAIL_VALIDATION_PATTERN, email)){
			this.email = email;
		} else {
			throw new ValidationException("Invalid email address");
		}
		if(password.length() > 0){
			this.password = password;
		} else {
			throw new ValidationException("Invalid password");
		} 
		
		this.id = counter++;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.id, this.lastName, this.firstName, this.email, this.password);
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
			FileLogger.getLogger().log("Expected User, got "
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