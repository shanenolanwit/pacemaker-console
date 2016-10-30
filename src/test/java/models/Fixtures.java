package models;

import exceptions.ValidationException;
import utils.DateTimeUtils;

public class Fixtures
{
  public static User[] users = createUsers();
  
  private static User[] createUsers()
  {
	  User[] ua = new User[4];
	  try{
		  ua[0] = new User ("marge", "simpson", "marge@simpson.com",  "secret");
		  ua[1] = new User ("lisa",  "simpson", "lisa@simpson.com",   "secret");
		  ua[2] = new User ("bart",  "simpson", "bart@simpson.com",   "secret");
		  ua[3] = new User ("maggie","simpson", "maggie@simpson.com", "secret");
	  }catch(ValidationException e){
		  
	  }
    return ua;
  };

  public static Activity[] activities = createActivities();
 
  
  private static Activity[] createActivities()
  {
	  Activity[] aa = new Activity[5];
	  try{
		   aa[0] = new Activity ("walk",  "fridge", 0.001, 
		    		DateTimeUtils.convertStringToLocalDateTime("12:10:2013 9:00:00"), DateTimeUtils.convertStringToDuration("00:02:03"));
		   aa[1] = new Activity ("walk",  "bar",    1.0, 
		    		DateTimeUtils.convertStringToLocalDateTime("12:10:2014 9:00:00"), DateTimeUtils.convertStringToDuration("01:02:04"));
		   aa[2] = new Activity ("run",   "work",   2.2, 
		    		DateTimeUtils.convertStringToLocalDateTime("02:04:2015 09:00:00"), DateTimeUtils.convertStringToDuration("01:03:03"));
		   aa[3] = new Activity ("walk",  "shop",   2.5, 
		    		DateTimeUtils.convertStringToLocalDateTime("02:04:2015 10:30:00"), DateTimeUtils.convertStringToDuration("01:04:04"));
		   aa[4] = new Activity ("cycle", "school", 4.5, 
		    		DateTimeUtils.convertStringToLocalDateTime("12:10:2016 9:00:00"), DateTimeUtils.convertStringToDuration("04:02:03"));
	  }catch(ValidationException e){
		  
	  }
    return aa;
  };

  public static Location[]locations =
  {
    new Location(23.3, 33.3),
    new Location(34.4, 45.2),  
    new Location(25.3, 34.3),
    new Location(44.4, 23.3)       
  };
  
} 