package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
	
	public static final String DATE_TIME_FORMAT = "dd:MM:yyyy H:mm:ss";
	
	public static String convertLocalDateTimeToString(LocalDateTime dateTime){
		 return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
	}
	
	public static LocalDateTime convertStringToLocalDateTime(String stringDateTime){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
		return LocalDateTime.parse(stringDateTime, formatter);
	}

}
