package comparators;

import static org.junit.Assert.*;

import static org.hamcrest.core.Is.is;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.UserSortFilter;
import exceptions.ValidationException;
import models.User;

public class UserComparatorTest {

	User annaThompson;
	User annSimpson;
	User catherineNolan;
	User nicolaGoral;
	User tanyaAnderson;

	List<User> users;

	@Before
	public void setup() throws ValidationException {
		annaThompson = new User("anna", "thompson", "annaT@gmail.com", "secret");
		annSimpson = new User("ann", "simpson", "someone@yahoo.com", "secret");
		catherineNolan = new User("catherine", "nolan", "someone@gmail.com", "secret");
		nicolaGoral = new User("nicola", "goral", "awesome@gmail.com", "secret");
		tanyaAnderson = new User("tanya", "anderson", "tandy@yahoo.com", "secret");
		users = new ArrayList<>();
		users.add(annaThompson);
		users.add(annSimpson);
		users.add(catherineNolan);
		users.add(nicolaGoral);
		users.add(tanyaAnderson);
	}

	@After
	public void tearDown() {

	}

	/**
	 * Given no comparators have been applied
	 * When I check the array
	 * Then the users are in the order they were added
	 */
	@Test
	public void validateInitialArray() {
		assertThat(users.get(0), is(annaThompson));
		assertThat(users.get(1), is(annSimpson));
		assertThat(users.get(2), is(catherineNolan));
		assertThat(users.get(3), is(nicolaGoral));
		assertThat(users.get(4), is(tanyaAnderson));
	}

	/**
	 * Given I sort the users array using an id comparator
	 * When I inspect the array
	 * Then the entries are sorted by id
	 */
	@Test
	public void sortUsersById() {
		Collections.sort(users, new UserIdComparator());
		assertThat(users.get(0), is(annaThompson));
		assertThat(users.get(1), is(annSimpson));
		assertThat(users.get(2), is(catherineNolan));
		assertThat(users.get(3), is(nicolaGoral));
		assertThat(users.get(4), is(tanyaAnderson));
	}
	
	/**
	 * Given I sort the users array using the filters sort method and the id identifier
	 * When I inspect the array
	 * Then the entries are sorted by id
	 */
	@Test
	public void userFilterCanSortById() {
		List<User> lu = UserSortFilter.sort(users, "id");
		assertThat(lu.get(0), is(annaThompson));
		assertThat(lu.get(1), is(annSimpson));
		assertThat(lu.get(2), is(catherineNolan));
		assertThat(lu.get(3), is(nicolaGoral));
		assertThat(lu.get(4), is(tanyaAnderson));
	}

	
	/**
	 * Given I sort the users array using a first name comparator
	 * When I inspect the array
	 * Then the entries are sorted by first name
	 */
	@Test
	public void sortUsersByFirstName() {
		Collections.sort(users, new UserFirstNameComparator());
		assertThat(users.get(0), is(annSimpson));
		assertThat(users.get(1), is(annaThompson));
		assertThat(users.get(2), is(catherineNolan));
		assertThat(users.get(3), is(nicolaGoral));
		assertThat(users.get(4), is(tanyaAnderson));
	}
	
	/**
	 * Given I sort the users array using the filters sort method and the firstname identifier
	 * When I inspect the array
	 * Then the entries are sorted by firstname
	 */
	@Test
	public void userFilterCanSortByFirstName() {
		List<User> lu = UserSortFilter.sort(users, "firstname");
		assertThat(lu.get(0), is(annSimpson));
		assertThat(lu.get(1), is(annaThompson));
		assertThat(lu.get(2), is(catherineNolan));
		assertThat(lu.get(3), is(nicolaGoral));
		assertThat(lu.get(4), is(tanyaAnderson));
	}

	/**
	 * Given I sort the users array using a last name comparator
	 * When I inspect the array
	 * Then the entries are sorted by last name
	 */
	@Test
	public void sortUsersByLastName() {
		Collections.sort(users, new UserLastNameComparator());
		assertThat(users.get(0), is(tanyaAnderson));
		assertThat(users.get(1), is(nicolaGoral));
		assertThat(users.get(2), is(catherineNolan));
		assertThat(users.get(3), is(annSimpson));
		assertThat(users.get(4), is(annaThompson));
	}
	
	/**
	 * Given I sort the users array using the filters sort method and the lastname identifier
	 * When I inspect the array
	 * Then the entries are sorted by lastname
	 */
	@Test
	public void userFilterCanSortByLastName() {
		List<User> lu = UserSortFilter.sort(users, "lastname");		
		assertThat(lu.get(0), is(tanyaAnderson));
		assertThat(lu.get(1), is(nicolaGoral));
		assertThat(lu.get(2), is(catherineNolan));
		assertThat(lu.get(3), is(annSimpson));
		assertThat(lu.get(4), is(annaThompson));
	}

	/**
	 * Given I sort the users array using an email comparator
	 * When I inspect the array
	 * Then the entries are sorted by email address
	 */
	@Test
	public void sortUsersByEmailAddress() {
		Collections.sort(users, new UserEmailComparator());
		assertThat(users.get(0), is(annaThompson));
		assertThat(users.get(1), is(nicolaGoral));
		assertThat(users.get(2), is(catherineNolan));
		assertThat(users.get(3), is(annSimpson));
		assertThat(users.get(4), is(tanyaAnderson));
	}
	
	/**
	 * Given I sort the users array using the filters sort method and the email identifier
	 * When I inspect the array
	 * Then the entries are sorted by email
	 */
	@Test
	public void userFilterCanSortByEmailAddress() {
		List<User> lu = UserSortFilter.sort(users, "email");
		assertThat(lu.get(0), is(annaThompson));
		assertThat(lu.get(1), is(nicolaGoral));
		assertThat(lu.get(2), is(catherineNolan));
		assertThat(lu.get(3), is(annSimpson));
		assertThat(lu.get(4), is(tanyaAnderson));
	}

}
