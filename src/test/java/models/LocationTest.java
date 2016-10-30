package models;

import static org.junit.Assert.*;

import org.junit.Test;
import static models.Fixtures.locations;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsSame.sameInstance;

import static org.hamcrest.core.IsEqual.equalTo;

public class LocationTest {
	@Test
	public void testCreate() {
		assertEquals(0.01, 23.3, locations[0].latitude);
		assertEquals(0.01, 33.3, locations[0].longitude);
	}

	@Test
	public void testIds() {
		assertThat(locations[0].id, is(not(equalTo(locations[1].id))));
	}

	@Test
	public void testEquality() {
		Object x = locations[0];
		assertThat(locations[0], is(equalTo(x)));
		assertThat(locations[0], is(not(equalTo(locations[1]))));
		assertFalse(x.equals(new String("Hello World")));
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
		assertThat(sameLatLongA, is(equalTo(sameLatLongB)));
		
		assertNotEquals(sameLatLongA.hashCode(), sameLatLongB.hashCode());
		assertThat(sameLatLongA, is(not(sameInstance(sameLatLongB))));
	    
	}

	@Test
	public void testToString() {
		assertEquals("Location{" + locations[0].id + ", 23.3, 33.3}", locations[0].toString());
	}
}