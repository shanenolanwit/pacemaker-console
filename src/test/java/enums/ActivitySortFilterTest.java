package enums;

import static org.junit.Assert.*;

import org.junit.Test;

import comparators.ActivityDateComparator;
import comparators.ActivityDistanceComparator;
import comparators.ActivityDurationComparator;
import comparators.ActivityIdComparator;
import comparators.ActivityLocationComparator;
import comparators.ActivityTypeComparator;

public class ActivitySortFilterTest {

	@Test
	public void testFilterIdentifier() {
		assertEquals(ActivityLocationComparator.class,ActivitySortFilter.identify("location").getComparator().getClass());
		assertEquals(ActivityLocationComparator.class,ActivitySortFilter.identify("lOcAtIOn").getComparator().getClass());
		assertEquals(ActivityLocationComparator.class,ActivitySortFilter.identify("LOCATION").getComparator().getClass());
		
		assertEquals(ActivityDurationComparator.class,ActivitySortFilter.identify("DurAtioN").getComparator().getClass());
		assertEquals(ActivityDurationComparator.class,ActivitySortFilter.identify("DURATION").getComparator().getClass());
		assertEquals(ActivityDurationComparator.class,ActivitySortFilter.identify("duration").getComparator().getClass());
		
		assertEquals(ActivityDistanceComparator.class,ActivitySortFilter.identify("DISTANCE").getComparator().getClass());
		assertEquals(ActivityDistanceComparator.class,ActivitySortFilter.identify("distance").getComparator().getClass());
		assertEquals(ActivityDistanceComparator.class,ActivitySortFilter.identify("distANCE").getComparator().getClass());
		
		assertEquals(ActivityTypeComparator.class,ActivitySortFilter.identify("type").getComparator().getClass());
		assertEquals(ActivityTypeComparator.class,ActivitySortFilter.identify("TYPE").getComparator().getClass());
		assertEquals(ActivityTypeComparator.class,ActivitySortFilter.identify("tyPE").getComparator().getClass());
		
		assertEquals(ActivityIdComparator.class,ActivitySortFilter.identify("id").getComparator().getClass());
		assertEquals(ActivityIdComparator.class,ActivitySortFilter.identify("iD").getComparator().getClass());
		assertEquals(ActivityIdComparator.class,ActivitySortFilter.identify("ID").getComparator().getClass());
		
		assertEquals(ActivityDateComparator.class,ActivitySortFilter.identify("DATE").getComparator().getClass());
		assertEquals(ActivityDateComparator.class,ActivitySortFilter.identify("DAte").getComparator().getClass());
		assertEquals(ActivityDateComparator.class,ActivitySortFilter.identify("date").getComparator().getClass());
		
	}
	
	@Test
	public void testExistance(){
		assertTrue(ActivitySortFilter.exists("DATE"));
		assertTrue(ActivitySortFilter.exists("LOCATION"));
		assertTrue(ActivitySortFilter.exists("Duration"));
		assertTrue(ActivitySortFilter.exists("ID"));
		assertTrue(ActivitySortFilter.exists("type"));
		assertFalse(ActivitySortFilter.exists("blah"));
		assertFalse(ActivitySortFilter.exists(""));
		assertFalse(ActivitySortFilter.exists(null));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBadIdentifierException(){
		ActivitySortFilter.identify("j").getComparator().getClass();
		fail("Previous instruction should have thrown an exception");
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullIdentifierException(){
		ActivitySortFilter.identify(null).getComparator().getClass();
		fail("Previous instruction should have thrown an exception");
	}


}
