package models;

import static org.junit.Assert.*;

import org.junit.Test;
import static models.Fixtures.locations;

public class LocationTest {
	@Test
	public void testCreate() {
		assertEquals(0.01, 23.3, locations[0].latitude);
		assertEquals(0.01, 33.3, locations[0].longitude);
	}

	@Test
	public void testIds() {
		assertNotEquals(locations[0].id, locations[1].id);
	}

	@Test
	public void testEquality() {
		Object x = locations[0];
		assertTrue(locations[0].equals(x));
		assertFalse(locations[0].equals(locations[1]));
		assertFalse(locations[0].equals(new String("Hello World")));
		Location sameLatA = new Location(23.3, 33.3);
		Location sameLatB = new Location(23.3, 33.4);
		Location sameLongA = new Location(25.0, 33.3);
		Location sameLongB = new Location(25.5, 33.3);
		assertFalse(sameLatA.equals(sameLatB));
		assertFalse(sameLongA.equals(sameLongB));
		
		Location sameLatLongA = new Location();
		Location sameLatLongB = new Location();
		sameLatLongA.id = (long) 44;
		sameLatLongB.id = (long) 88;
		sameLatLongA.latitude = 25.5;
		sameLatLongA.longitude = 33.5;
		sameLatLongB.latitude = 25.5;
		sameLatLongB.longitude = 33.5;
		
		assertTrue(sameLatLongA.equals(sameLatLongB));
		assertNotEquals(sameLatLongA.hashCode(), sameLatLongB.hashCode());
	    
	}

	@Test
	public void testToString() {
		assertEquals("Location{" + locations[0].id + ", 23.3, 33.3}", locations[0].toString());
	}
}