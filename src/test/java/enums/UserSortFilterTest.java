package enums;

import static org.junit.Assert.*;

import org.junit.Test;

import comparators.ActivityDateComparator;
import comparators.ActivityDistanceComparator;
import comparators.ActivityDurationComparator;
import comparators.ActivityIdComparator;
import comparators.ActivityLocationComparator;
import comparators.ActivityTypeComparator;
import comparators.UserEmailComparator;
import comparators.UserFirstNameComparator;
import comparators.UserIdComparator;
import comparators.UserLastNameComparator;

public class UserSortFilterTest {

	@Test
	public void testFilterIdentifier() {
		assertEquals(UserIdComparator.class,UserSortFilter.identify("id").getComparator().getClass());
		assertEquals(UserIdComparator.class,UserSortFilter.identify("ID").getComparator().getClass());
		assertEquals(UserIdComparator.class,UserSortFilter.identify("iD").getComparator().getClass());
		
		assertEquals(UserFirstNameComparator.class,UserSortFilter.identify("firstname").getComparator().getClass());
		assertEquals(UserFirstNameComparator.class,UserSortFilter.identify("FIRSTNAME").getComparator().getClass());
		assertEquals(UserFirstNameComparator.class,UserSortFilter.identify("firstNAME").getComparator().getClass());
		
		assertEquals(UserLastNameComparator.class,UserSortFilter.identify("lastName").getComparator().getClass());
		assertEquals(UserLastNameComparator.class,UserSortFilter.identify("lastname").getComparator().getClass());
		assertEquals(UserLastNameComparator.class,UserSortFilter.identify("LASTNAME").getComparator().getClass());
		
		assertEquals(UserEmailComparator.class,UserSortFilter.identify("EMAIL").getComparator().getClass());
		assertEquals(UserEmailComparator.class,UserSortFilter.identify("eMail").getComparator().getClass());
		assertEquals(UserEmailComparator.class,UserSortFilter.identify("email").getComparator().getClass());	
		
	}
	
	@Test
	public void testExistance(){
		assertTrue(UserSortFilter.exists("id"));
		assertTrue(UserSortFilter.exists("firstname"));
		assertTrue(UserSortFilter.exists("lastname"));
		assertTrue(UserSortFilter.exists("email"));		
		assertFalse(UserSortFilter.exists("blah"));
		assertFalse(UserSortFilter.exists(""));
		assertFalse(UserSortFilter.exists(null));
	}
	
	@Test(expected = Exception.class)
	public void testBadIdentifierException(){
		assertEquals(null,UserSortFilter.identify("j").getComparator().getClass());
	}
	
	@Test(expected = Exception.class)
	public void testNullIdentifierException(){
		assertEquals(null,UserSortFilter.identify(null).getComparator().getClass());
	}


}
