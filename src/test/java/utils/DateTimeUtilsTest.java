package utils;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.junit.Before;
import org.junit.Test;

import models.Activity;

public class DateTimeUtilsTest {
	
	@Before
	public void setUp(){
		Activity a = new Activity("walk", "fridge", 0.001,
    			DateTimeUtils.convertStringToLocalDateTime("12:10:2013 9:00:00"), 
    			DateTimeUtils.convertStringToDuration("01:02:03"));
	}

	@Test
	public void testConvertLocalDateTimeToString() {
		LocalDateTime dateTime = LocalDateTime.of(2012, 12, 25, 9, 0);
		assertEquals("2012-12-25T09:00:00", DateTimeUtils.convertLocalDateTimeToString(dateTime));
	}

	@Test
	public void testConvertStringToLocalDateTime() {
		LocalDateTime dateTime = LocalDateTime.of(2012, 12, 25, 9, 0);
		assertEquals(dateTime, DateTimeUtils.convertStringToLocalDateTime("25:12:2012 9:00:00"));
	}
	
	@Test(expected = DateTimeParseException.class)
	public void testConvertInvalidStringToLocalDateTime() {
		assertEquals(null, DateTimeUtils.convertStringToLocalDateTime("52:12:2012 9:00:00"));
	}

	@Test
	public void testIsValidDate() {
		assertTrue(DateTimeUtils.isValidDate("25:12:2012 9:00:00"));
		assertFalse(DateTimeUtils.isValidDate("25:12:2012"));
		assertFalse(DateTimeUtils.isValidDate("25-12-2012 9:00:00"));
		assertFalse(DateTimeUtils.isValidDate("25-12-2012 9-00-00"));
		assertFalse(DateTimeUtils.isValidDate("52:12:2012 9:00:00"));
		assertFalse(DateTimeUtils.isValidDate("25:13:2012 9:00:00"));
		assertFalse(DateTimeUtils.isValidDate("25:12:2012 99:00:00"));
		assertFalse(DateTimeUtils.isValidDate("25:12:2012 9:404:00"));
		assertFalse(DateTimeUtils.isValidDate("25:12:2012 9:00:69"));
		assertFalse(DateTimeUtils.isValidDate("25:12 9:00:00"));
		assertFalse(DateTimeUtils.isValidDate("hello world"));
		assertFalse(DateTimeUtils.isValidDate(null));
	}

	@Test
	public void testIsValidDuration() {
		assertTrue(DateTimeUtils.isValidDuration("1:1:1"));
		assertTrue(DateTimeUtils.isValidDuration("19:199:1"));
		assertFalse(DateTimeUtils.isValidDuration("5:5"));
		assertFalse(DateTimeUtils.isValidDuration("5:5:5:5"));
		assertFalse(DateTimeUtils.isValidDuration("20minutes"));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConvertInvalidStringDuration() {
		assertEquals(null, DateTimeUtils.isValidDuration(null));
	}

	@Test
	public void testConvertStringToDuration() {
		Duration durationH = Duration.ofHours(2);
		Duration durationM = Duration.ofMinutes(4);
		Duration durationS = Duration.ofSeconds(20);
		assertEquals(durationH, DateTimeUtils.convertStringToDuration("02:00:00"));
		assertEquals(durationH, DateTimeUtils.convertStringToDuration("2:0:0"));
		assertEquals(durationH, DateTimeUtils.convertStringToDuration("0:120:0"));
		assertEquals(durationM, DateTimeUtils.convertStringToDuration("0:04:0"));
		assertEquals(durationM, DateTimeUtils.convertStringToDuration("0:4:00"));
		assertEquals(durationM, DateTimeUtils.convertStringToDuration("00:00:240"));
		assertEquals(durationS, DateTimeUtils.convertStringToDuration("00:00:20"));
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testConvertInvalidStringDurationConversion() {
		assertEquals(null, DateTimeUtils.convertStringToDuration("09:00"));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConvertNullStringDurationConversion() {
		assertEquals(null, DateTimeUtils.convertStringToDuration(null));
	}

	@Test
	public void testConvertDurationToString() {
		Duration dH = Duration.ofHours(2).plusMinutes(10).plusSeconds(40);
		Duration dM = Duration.ofMinutes(10).plusSeconds(40);
		Duration dM2 = Duration.ofMinutes(100).plusSeconds(0);
		Duration dS = Duration.ofSeconds(40);
		assertEquals("2:10:40", DateTimeUtils.convertDurationToString(dH));
		assertEquals("0:10:40", DateTimeUtils.convertDurationToString(dM));
		assertEquals("0:00:40", DateTimeUtils.convertDurationToString(dS));
		assertEquals("1:40:00", DateTimeUtils.convertDurationToString(dM2));
		
	}

	@Test
	public void testConvertDurationForDisplay() {
		Duration dH = Duration.ofHours(2).plusMinutes(10).plusSeconds(40);
		Duration dM = Duration.ofMinutes(10).plusSeconds(40);
		Duration dM2 = Duration.ofMinutes(100).plusSeconds(0);
		Duration dS = Duration.ofSeconds(40);
		assertEquals("2 hour(s), 10 minute(s), and 40 second(s)", DateTimeUtils.convertDurationForDisplay(dH));
		assertEquals("10 minute(s), and 40 second(s)", DateTimeUtils.convertDurationForDisplay(dM));
		assertEquals("40 second(s)", DateTimeUtils.convertDurationForDisplay(dS));
		assertEquals("1 hour(s), 40 minute(s), and 0 second(s)", DateTimeUtils.convertDurationForDisplay(dM2));
	}

}
