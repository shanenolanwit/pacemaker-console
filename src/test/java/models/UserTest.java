package models;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import static models.Fixtures.users;
import static models.Fixtures.activities;

public class UserTest {
	User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

	@Test
	public void testCreate() {
		assertEquals("homer", homer.firstName);
		assertEquals("simpson", homer.lastName);
		assertEquals("homer@simpson.com", homer.email);
		assertEquals("secret", homer.password);
	}

	@Test
	public void testIds() {
		Set<Long> ids = new HashSet<>();
		for (User user : users) {
			ids.add(user.id);
		}
		assertEquals(users.length, ids.size());
	}

	@Test
	public void testEquality() {
		Object x = users[0];
		//Comparing identical objetcs
		assertTrue(users[0].equals(x));
		//Comparing like objects
		assertFalse(users[0].equals(users[1]));
		//Comparing unlike objects
		assertFalse(users[0].equals(new String("Hello World")));
		
		//Testing default constructor and equality
		User userA = new User();
		userA.id = (long) 44;
		userA.firstName = "homer";
		userA.lastName = "simpson";
		userA.email = "homer@simpson.com";
		userA.password = "secret";
		userA.activities.put((long) 20, activities[0]);
		User userB = new User();
		userB.id = (long) 88;
		userB.firstName = "remoh";
		userB.lastName = "nospmis";
		userB.email = "remoh@nospmis.com";
		userB.password = "terces";
		assertFalse(userA.equals(userB));
		userB.firstName = "homer";
		assertFalse(userA.equals(userB));
		userB.lastName = "simpson";
		assertFalse(userA.equals(userB));
		userB.email = "homer@simpson.com";
		assertFalse(userA.equals(userB));
		userB.password = "secret";
		assertFalse(userA.equals(userB));
		userB.activities.put((long) 20, activities[0]);		
		//Testing equals
		assertTrue(userA.equals(userB));
		//Testing hashcode
		assertNotEquals(userA.hashCode(), userB.hashCode());
	}

	@Test
	public void testToString() {
		assertEquals("User{" + homer.id + ", homer, simpson, secret, homer@simpson.com, {}}", homer.toString());
	}

}