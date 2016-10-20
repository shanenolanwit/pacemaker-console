package comparators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.ActivitySortFilter;
import models.Activity;
import models.User;
import utils.DateTimeUtils;
import comparators.ActivityDateComparator;
import comparators.ActivityDistanceComparator;
import comparators.ActivityDurationComparator;
import comparators.ActivityIdComparator;
import comparators.ActivityLocationComparator;
import comparators.ActivityTypeComparator;

public class UserComparatorTest {

	User u1;
	User u2;
	User u3;
	User u4;
	User u5;

	List<User> users;

	@Before
	public void setup() {
		u1 = new User("anna", "thompson", "annaT@gmail.com", "secret");
		u2 = new User("ann", "simpson", "someone@yahoo.com", "secret");
		u3 = new User("catherine", "nolan", "someone@gmail.com", "secret");
		u4 = new User("nicola", "goral", "awesome@gmail.com", "secret");
		u5 = new User("tanya", "anderson", "tandy@yahoo.com", "secret");
		users = new ArrayList<>();
		users.add(u1);
		users.add(u2);
		users.add(u3);
		users.add(u4);
		users.add(u5);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testInitialArray() {
		assertEquals(u1, users.get(0));
		assertEquals(u2, users.get(1));
		assertEquals(u3, users.get(2));
		assertEquals(u4, users.get(3));
		assertEquals(u5, users.get(4));
	}

	@Test
	public void testIdComparator() {
		Collections.sort(users, new UserIdComparator());
		assertEquals(u1, users.get(0));
		assertEquals(u2, users.get(1));
		assertEquals(u3, users.get(2));
		assertEquals(u4, users.get(3));
		assertEquals(u5, users.get(4));
	}

	@Test
	public void testFirstNameComparator() {
		Collections.sort(users, new UserFirstNameComparator());
		assertEquals(u2, users.get(0));
		assertEquals(u1, users.get(1));
		assertEquals(u3, users.get(2));
		assertEquals(u4, users.get(3));
		assertEquals(u5, users.get(4));
	}

	@Test
	public void testLastNameComparator() {
		Collections.sort(users, new UserLastNameComparator());
		assertEquals(u5, users.get(0));
		assertEquals(u4, users.get(1));
		assertEquals(u3, users.get(2));
		assertEquals(u2, users.get(3));
		assertEquals(u1, users.get(4));
	}

	@Test
	public void testEmailComparator() {
		Collections.sort(users, new UserEmailComparator());
		assertEquals(u1, users.get(0));
		assertEquals(u4, users.get(1));
		assertEquals(u3, users.get(2));
		assertEquals(u2, users.get(3));
		assertEquals(u5, users.get(4));
	}

}
