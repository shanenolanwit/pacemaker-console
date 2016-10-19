package models;

import utils.DateTimeUtils;

public class Fixtures
{
  public static User[] users =
  {
    new User ("marge", "simpson", "marge@simpson.com",  "secret"),
    new User ("lisa",  "simpson", "lisa@simpson.com",   "secret"),
    new User ("bart",  "simpson", "bart@simpson.com",   "secret"),
    new User ("maggie","simpson", "maggie@simpson.com", "secret")
  };

  public static Activity[] activities =
  {
    new Activity ("walk",  "fridge", 0.001, 
    		DateTimeUtils.convertStringToLocalDateTime("12:10:2013 9:00:00"), DateTimeUtils.convertStringToDuration("00:02:03")),
    new Activity ("walk",  "bar",    1.0, 
    		DateTimeUtils.convertStringToLocalDateTime("12:10:2014 9:00:00"), DateTimeUtils.convertStringToDuration("01:02:04")),
    new Activity ("run",   "work",   2.2, 
    		DateTimeUtils.convertStringToLocalDateTime("02:04:2015 09:00:00"), DateTimeUtils.convertStringToDuration("01:03:03")),
    new Activity ("walk",  "shop",   2.5, 
    		DateTimeUtils.convertStringToLocalDateTime("02:04:2015 10:30:00"), DateTimeUtils.convertStringToDuration("01:04:04")),
    new Activity ("cycle", "school", 4.5, 
    		DateTimeUtils.convertStringToLocalDateTime("12:10:2016 9:00:00"), DateTimeUtils.convertStringToDuration("04:02:03"))
  };

  public static Location[]locations =
  {
    new Location(23.3, 33.3),
    new Location(34.4, 45.2),  
    new Location(25.3, 34.3),
    new Location(44.4, 23.3)       
  };
  
} 