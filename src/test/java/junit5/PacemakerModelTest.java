package junit5;

//source:  https://github.com/junit-team/junit5-samples

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;


import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import models.Activity;
import models.Location;
import models.User;
import utils.DateTimeUtils;

@RunWith(JUnitPlatform.class)	
class PacemakerModelTest {
	
	@BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
    
    @Test
    @DisplayName("Hello JUnit 5!")
    void junit5Test(TestInfo testInfo){
    	 assertEquals(2, 1 + 1, "1 + 1 should equal 2");
         assertEquals("Hello JUnit 5!", testInfo.getDisplayName(), () -> "TestInfo is injected correctly");
    }

    @Test
    @DisplayName("Testing assert alls")
    void assertAllTest(TestInfo testInfo) {
    	User u = new User ("homer", "simpson", "homer@simpson.com", "secret");
    	assertAll( "user properties", () -> {
    		  assertEquals( "homer", u.firstName, "first name match");
    		  assertEquals( "simpson", u.lastName, "last name match" );
    		  assertEquals( "homer@simpson.com", u.email, "email match" );
    		  assertEquals ("User{" + u.id + ", homer, simpson, secret, homer@simpson.com, {}}", u.toString());
    	} );
    	Activity a = new Activity("walk", "fridge", 0.001,
    			DateTimeUtils.convertStringToLocalDateTime("12:10:2013 9:00:00"), 
    			DateTimeUtils.convertStringToDuration("01:02:03"));
    	assertAll( "activity properties", () -> {
  		  assertEquals( "walk", a.type, "type match");
  		  assertEquals( "fridge", a.location, "location match" );
  		  assertEquals( 0.001, a.distance, "distance match" );
  		  assertEquals( "2013-10-12T09:00:00", DateTimeUtils.convertLocalDateTimeToString(a.date), "date match" );
  		  assertEquals( "1:02:03", DateTimeUtils.convertDurationToString(a.duration), "duration match" );
  		  assertEquals( "PT1H2M3S", a.duration.toString(), "raw duration match" );
  		  assertEquals( "1 hour(s), 2 minute(s), and 3 second(s)", DateTimeUtils.convertDurationForDisplay(a.duration), "display duration match" );
    	} );
    	Location l = new Location(23.3, 33.3);
    	assertAll( "location properties", () -> {
    		  assertTrue( 23.3 == l.latitude);
    		  assertTrue( 33.3 == l.longitude);
      	} );

       
    }

}